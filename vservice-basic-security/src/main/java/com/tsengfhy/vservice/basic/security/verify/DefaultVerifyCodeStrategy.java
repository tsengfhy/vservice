package com.tsengfhy.vservice.basic.security.verify;

import com.tsengfhy.vservice.basic.constant.Random;
import com.tsengfhy.vservice.basic.utils.CountUtils;
import com.tsengfhy.vservice.basic.utils.MessageSourceUtils;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.CredentialsExpiredException;

import java.util.Optional;

@Setter
public class DefaultVerifyCodeStrategy implements VerifyCodeStrategy {

    private Random codeType;
    private int codeLength = 6;
    private int limitTimes = 3;

    private Cache cache;

    public void setCacheManager(CacheManager cacheManager) {
        cache = cacheManager.getCache(com.tsengfhy.vservice.basic.constant.Cache.VERIFY_CODE.name());
    }

    @Override
    public String generate(String type, String username) {
        Optional<Random> optional = Optional.ofNullable(codeType);
        String verifyCode = RandomStringUtils.random(codeLength, optional.map(Random.LETTER::equals).orElse(true), optional.map(Random.NUMBER::equals).orElse(true));

        String key = new StringBuilder().append(type).append(":").append(username).toString();
        cache.put(key, verifyCode);
        CountUtils.clear(com.tsengfhy.vservice.basic.constant.Cache.VERIFY_CODE.name(), key);
        return verifyCode;
    }

    @Override
    public void clear(String type, String username) {
        String key = new StringBuilder().append(type).append(":").append(username).toString();
        cache.evict(key);
        CountUtils.clear(com.tsengfhy.vservice.basic.constant.Cache.VERIFY_CODE.name(), key);
    }

    @Override
    public boolean matches(String type, String username, String verifyCode) throws CredentialsExpiredException {

        String key = new StringBuilder().append(type).append(":").append(username).toString();
        boolean result = Optional.ofNullable(cache.get(key)).map(valueWrapper -> String.valueOf(valueWrapper.get()).equals(verifyCode))
                .orElseThrow(() -> new CredentialsExpiredException(MessageSourceUtils.getMessage("Security.verifyCodeExpired", new Object[]{username}, "User " + username + "'s verify code have expired")));

        if (result) {
            CountUtils.clear(com.tsengfhy.vservice.basic.constant.Cache.VERIFY_CODE.name(), key);
        } else if (limitTimes > 0) {
            long count = CountUtils.add(com.tsengfhy.vservice.basic.constant.Cache.VERIFY_CODE.name(), key);
            if (count >= limitTimes) {
                this.clear(type, username);
            }
        }

        return result;
    }
}
