package com.tsengfhy.vservice.basic.id;

public abstract class SnowflakeId {

    private static long sequence = 0L;
    private static final long sequenceBits = 12L;
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

    private static long workerId = 0L;
    private static final long workerIdBits = 5L;
    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private static final long workerIdShift = sequenceBits;

    private static long dataCenterId = 0L;
    private static final long dataCenterIdBits = 5L;
    private static final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);
    private static final long dataCenterIdShift = sequenceBits + workerIdBits;

    private static final long startTimestamp = 1514736000000L;    //2018-01-01
    private static long lastTimestamp = startTimestamp;
    private static final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    public static void init(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0L) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }

        if (dataCenterId > maxDataCenterId || dataCenterId < 0L) {
            throw new IllegalArgumentException(String.format("dataCenter Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        SnowflakeId.workerId = workerId;
        SnowflakeId.dataCenterId = dataCenterId;
    }

    public static synchronized long generate() {
        long timestamp = System.currentTimeMillis();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        } else if (timestamp == lastTimestamp) {
            sequence = (sequence + 1L) & sequenceMask;
            if (sequence == 0L) {
                timestamp = getNextTimestamp(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - startTimestamp) << timestampLeftShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    private static long getNextTimestamp(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
