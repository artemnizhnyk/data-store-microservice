package com.artemnizhnyk.config;

import com.artemnizhnyk.model.MeasurementType;

public class RedisSchema {

    //set
    public static String sensorKeys() {
        return KeyHelper.getKey("sensors");
    }

    //hash with summary types
    public static String summaryKey(
            final long sensorId,
            final MeasurementType measurementType
    ) {
        return KeyHelper.getKey("sensors:" + sensorId + ":" + measurementType.name().toLowerCase());
    }
}
