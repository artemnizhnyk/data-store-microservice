package com.artemnizhnyk.web.dto;

import com.artemnizhnyk.model.MeasurementType;
import com.artemnizhnyk.model.Summary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class SummaryDto {

    private long sensorId;
    private Map<MeasurementType, List<Summary.SummaryEntry>> values;
}