package com.artemnizhnyk.repository;

import com.artemnizhnyk.model.MeasurementType;
import com.artemnizhnyk.model.Summary;
import com.artemnizhnyk.model.SummaryType;

import java.util.Optional;
import java.util.Set;

public interface SummaryRepository {

    Optional<Summary> findBySensorId(final long sensorId,
                                     final Set<MeasurementType> measurementTypes,
                                     final Set<SummaryType> summaryTypes);
}
