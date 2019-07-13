package com.tsengfhy.vservice.basic.redis.repository;

import com.google.common.base.CaseFormat;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.util.Assert;

import java.util.Optional;

public class SimpleKeyspaceConfiguration extends KeyspaceConfiguration {

    @Override
    public boolean hasSettingsFor(Class<?> type) {
        Assert.notNull(type, "Type to lookup must not be null!");
        return true;
    }

    @Override
    public KeyspaceConfiguration.KeyspaceSettings getKeyspaceSettings(Class<?> type) {

        return Optional.ofNullable(type)
                .map(super::getKeyspaceSettings)
                .orElseGet(() -> {
                    KeyspaceConfiguration.KeyspaceSettings settings = new KeyspaceConfiguration.KeyspaceSettings(type, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, type.getSimpleName()));
                    super.addKeyspaceSettings(settings);
                    return settings;
                });
    }
}
