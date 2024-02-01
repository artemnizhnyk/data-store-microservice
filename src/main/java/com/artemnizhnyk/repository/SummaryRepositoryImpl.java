package com.artemnizhnyk.repository;

import com.artemnizhnyk.config.RedisSchema;
import com.artemnizhnyk.model.Data;
import com.artemnizhnyk.model.MeasurementType;
import com.artemnizhnyk.model.Summary;
import com.artemnizhnyk.model.SummaryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Repository
public class SummaryRepositoryImpl implements SummaryRepository {

    private final JedisPool jedisPool;

    @Override
    public Optional<Summary> findBySensorId(
            final long sensorId,
            final Set<MeasurementType> measurementTypes,
            final Set<SummaryType> summaryTypes
    ) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (!jedis.sismember(
                    RedisSchema.sensorKeys(),
                    String.valueOf(sensorId)
            )) {
                return Optional.empty();
            }
            if (measurementTypes.isEmpty() && !summaryTypes.isEmpty()) {
                return getSummary(
                        sensorId,
                        Set.of(MeasurementType.values()),
                        summaryTypes,
                        jedis
                );
            } else if (!measurementTypes.isEmpty() && summaryTypes.isEmpty()) {
                return getSummary(
                        sensorId,
                        measurementTypes,
                        Set.of(SummaryType.values()),
                        jedis
                );
            } else {
                return getSummary(
                        sensorId,
                        measurementTypes,
                        summaryTypes,
                        jedis
                );
            }
        }
    }

    private Optional<Summary> getSummary(
            final long sensorId,
            final Set<MeasurementType> measurementTypes,
            final Set<SummaryType> summaryTypes,
            final Jedis jedis
    ) {
        Summary summary = new Summary();
        summary.setSensorId(sensorId);
        for (MeasurementType mType : measurementTypes) {
            for (SummaryType sType : summaryTypes) {
                Summary.SummaryEntry entry = new Summary.SummaryEntry();
                entry.setType(sType);
                String value = jedis.hget(
                        RedisSchema.summaryKey(sensorId, mType),
                        sType.name().toLowerCase()
                );
                if (value != null) {
                    entry.setValue(Double.parseDouble(value));
                }
                String counter = jedis.hget(
                        RedisSchema.summaryKey(sensorId, mType),
                        "counter"
                );
                if (counter != null) {
                    entry.setCounter(Long.parseLong(counter));
                }
                summary.addValue(mType, entry);
            }
        }
        return Optional.of(summary);
    }

    @Override
    public void handle(final Data data) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (!jedis.sismember(
                    RedisSchema.sensorKeys(),
                    String.valueOf(data.getSensorId())
            )) {
                jedis.sadd(
                        RedisSchema.sensorKeys(),
                        String.valueOf(data.getSensorId())
                );
            }
            updateMinValue(data, jedis);
            updateMaxValue(data, jedis);
            updateSumAndAvgValue(data, jedis);
        }
    }

    private void updateMinValue(final Data data, final Jedis jedis) {
        String key = RedisSchema.summaryKey(data.getSensorId(), data.getMeasurementType());
        String value = jedis.hget(key, SummaryType.MIN.name().toLowerCase());
        if (value == null || data.getMeasurement() < Double.parseDouble(value)) {
            jedis.hset(
                    key,
                    SummaryType.MIN.name().toLowerCase(),
                    String.valueOf(data.getMeasurement())
            );
        }
    }

    private void updateMaxValue(final Data data, final Jedis jedis) {
        String key = RedisSchema.summaryKey(data.getSensorId(), data.getMeasurementType());
        String value = jedis.hget(key, SummaryType.MAX.name().toLowerCase());
        if (value == null || data.getMeasurement() > Double.parseDouble(value)) {
            jedis.hset(
                    key,
                    SummaryType.MAX.name().toLowerCase(),
                    String.valueOf(data.getMeasurement())
            );
        }
    }

    private void updateSumAndAvgValue(final Data data, final Jedis jedis) {
        updateSumValue(data, jedis);
        String key = RedisSchema.summaryKey(data.getSensorId(), data.getMeasurementType());
        String counter = jedis.hget(key, "counter");

        if (counter == null) {
            counter = String.valueOf(jedis.hset(key, "counter", String.valueOf(1)));
        } else {
            counter = String.valueOf(jedis.hincrBy(key, "counter", 1));
        }

        String sum = jedis.hget(key, SummaryType.SUM.name().toLowerCase());
        jedis.hset(
                key,
                SummaryType.AVG.name().toLowerCase(),
                String.valueOf(Double.parseDouble(sum) / Double.parseDouble(counter)));
    }

    private void updateSumValue(final Data data, final Jedis jedis) {
        String key = RedisSchema.summaryKey(data.getSensorId(), data.getMeasurementType());
        String value = jedis.hget(key, SummaryType.SUM.name().toLowerCase());
        if (value == null) {
            jedis.hset(
                    key,
                    SummaryType.SUM.name().toLowerCase(),
                    String.valueOf(data.getMeasurement())
            );
        }else {
            jedis.hincrByFloat(
                    key,
                    SummaryType.SUM.name().toLowerCase(),
                    data.getMeasurement()
            );
        }
    }
}
