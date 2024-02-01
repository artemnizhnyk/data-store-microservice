package com.artemnizhnyk.web.dto;

import com.artemnizhnyk.model.MeasurementType;
import com.artemnizhnyk.model.Summary;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SummaryDto {

    private long sensorId;
    private Map<MeasurementType, List<Summary.SummaryEntry>> values;
}