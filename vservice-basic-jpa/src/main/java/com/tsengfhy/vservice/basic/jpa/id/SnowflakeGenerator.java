package com.tsengfhy.vservice.basic.jpa.id;

import com.tsengfhy.vservice.basic.id.SnowflakeId;

import java.io.Serializable;

public class SnowflakeGenerator extends AbstractUnforcedIdentifierGenerator {

    @Override
    public Serializable generate() {
        return SnowflakeId.generate();
    }
}
