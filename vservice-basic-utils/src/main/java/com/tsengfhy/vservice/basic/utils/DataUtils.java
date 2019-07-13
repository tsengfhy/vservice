package com.tsengfhy.vservice.basic.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public final class DataUtils {

    @Setter
    private static StringEncryptor stringEncryptor;

    private static final Pattern HTML_PATTERN = Pattern.compile("<\\s*\\/?\\s*[a-zA-z_]([^>]*?[\"][^\"]*[\"])*[^>\"]*>$");

    public static boolean isHtml(String text) {
        return HTML_PATTERN.matcher(text).find();
    }

    public static String toJSON(Object data, SerializerFeature... features) {

        List<SerializerFeature> list = new ArrayList<>(Arrays.asList(features));
        list.add(SerializerFeature.DisableCircularReferenceDetect);
        list.add(SerializerFeature.WriteDateUseDateFormat);
        list.add(SerializerFeature.PrettyFormat);

        return JSON.toJSONString(data, list.toArray(new SerializerFeature[list.size()]));
    }

    public static <T> T toObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    public static String encrypt(String data) {
        return Optional.ofNullable(stringEncryptor)
                .orElseThrow(() -> new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{"DataUtils.encrypt()"}, "Method DataUtils.encrypt() not support")))
                .encrypt(data);
    }

    public static String decrypt(String data) {
        return Optional.ofNullable(stringEncryptor)
                .orElseThrow(() -> new UnsupportedOperationException(MessageSourceUtils.getMessage("Common.methodNotSupport", new Object[]{"DataUtils.decrypt()"}, "Method DataUtils.decrypt() not support")))
                .decrypt(data);
    }

    private static final Pattern SORT_PATTERN = Pattern.compile("^(\\s*\\w+\\s*(\\+|-)?\\s*,)*(\\s*\\w+\\s*(\\+|-)?\\s*)$");

    public static Sort toSort(String sort) {
        if (!StringUtils.isNotBlank(sort) || !SORT_PATTERN.matcher(sort).find()) {
            log.warn("Sort string '{}' is empty or don`t match, use empty Sort as result", sort);
            return Sort.by(Collections.emptyList());
        }

        List<Sort.Order> orders = Arrays.stream(sort.split(","))
                .map(item -> {
                    if (item.indexOf("-") > 0) {
                        return new Sort.Order(Sort.Direction.DESC, item.trim().replaceAll("-", ""));
                    } else {
                        return new Sort.Order(Sort.Direction.ASC, item.trim().replaceAll("\\+", ""));
                    }
                })
                .collect(Collectors.toList());

        return Sort.by(orders);
    }
}
