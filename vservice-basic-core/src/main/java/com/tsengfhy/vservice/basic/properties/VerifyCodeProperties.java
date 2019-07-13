package com.tsengfhy.vservice.basic.properties;

import com.tsengfhy.vservice.basic.constant.Random;
import lombok.Data;

@Data
public class VerifyCodeProperties {

    private Random type;

    private int length = 6;
    private int expiredMillis = 300000;
    private int expiredTimes = 3;
}
