package com.artemnizhnyk.web.mapper;

import com.artemnizhnyk.model.Summary;
import com.artemnizhnyk.web.dto.SummaryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SummaryMapper extends Mappable<Summary, SummaryDto> {

    @Override
    default SummaryDto toDto(Summary entity) {
        if ( entity == null ) {
            return null;
        }

        SummaryDto summaryDto = new SummaryDto();
        summaryDto.setSensorId(entity.getSensorId());
        summaryDto.setValues(entity.getValues());
        return summaryDto;
    }
}
