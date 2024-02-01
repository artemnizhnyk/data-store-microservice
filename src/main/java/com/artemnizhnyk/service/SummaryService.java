package com.artemnizhnyk.service;

import com.artemnizhnyk.model.MeasurementType;
import com.artemnizhnyk.model.Summary;
import com.artemnizhnyk.model.SummaryType;

import java.util.Set;

public interface SummaryService {

    Summary get(final long sensorId,
                final Set<MeasurementType> measurementTypes,
                final Set<SummaryType> summaryTypes);
}
