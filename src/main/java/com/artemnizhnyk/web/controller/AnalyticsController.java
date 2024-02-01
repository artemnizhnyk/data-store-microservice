package com.artemnizhnyk.web.controller;

import com.artemnizhnyk.model.MeasurementType;
import com.artemnizhnyk.model.Summary;
import com.artemnizhnyk.model.SummaryType;
import com.artemnizhnyk.service.SummaryService;
import com.artemnizhnyk.web.dto.SummaryDto;
import com.artemnizhnyk.web.mapper.SummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("api/v1/analytics")
@RestController
public class AnalyticsController {

    private final SummaryService summaryService;
    private final SummaryMapper summaryMapper;

    @GetMapping("summary/{sensorId}")
    public SummaryDto getSummary(@PathVariable final long sensorId,
                                 @RequestParam(value = "mt", required = false) Set<MeasurementType> measurementTypes,
                                 @RequestParam(value = "st", required = false) Set<SummaryType> summaryTypes
    ) {
        Summary summary = summaryService.get(sensorId, measurementTypes, summaryTypes);
        return summaryMapper.toDto(summary);
    }
}
