package pl.gov.cmp.history.model.maper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.history.model.dto.HistoryOperationDto;
import pl.gov.cmp.history.model.entity.HistoryOperationEntity;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface HistoryOperationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "operationDate", ignore = true)
    HistoryOperationEntity toHistoryOperationEntity(HistoryOperationDto historyOperation);

}
