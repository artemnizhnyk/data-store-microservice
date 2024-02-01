package com.artemnizhnyk.service;

import com.artemnizhnyk.model.Data;
import com.artemnizhnyk.model.MeasurementType;
import com.artemnizhnyk.model.Summary;
import com.artemnizhnyk.model.SummaryType;
import com.artemnizhnyk.model.exception.SensorNotFoundException;
import com.artemnizhnyk.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class SummaryServiceImpl implements SummaryService {

    private final SummaryRepository summaryRepository;

    @Override
    public Summary get(
            final long sensorId,
            final Set<MeasurementType> measurementTypes,
            final Set<SummaryType> summaryTypes
    ) {
        return summaryRepository.findBySensorId(
                sensorId,
                measurementTypes == null ? Set.of(MeasurementType.values()) : measurementTypes,
                summaryTypes == null ? Set.of(SummaryType.values()) : summaryTypes
        ).orElseThrow(SensorNotFoundException::new);
    }

    @Override
    public void handle(final Data data) {
        summaryRepository.handle(data);
    }
}
