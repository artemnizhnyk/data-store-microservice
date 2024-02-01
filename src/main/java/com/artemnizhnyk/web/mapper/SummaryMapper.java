package com.artemnizhnyk.web.mapper;

import com.artemnizhnyk.model.Summary;
import com.artemnizhnyk.web.dto.SummaryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SummaryMapper extends Mappable<Summary, SummaryDto> {
}
