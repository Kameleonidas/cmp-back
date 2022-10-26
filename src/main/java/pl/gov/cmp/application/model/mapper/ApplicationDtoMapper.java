package pl.gov.cmp.application.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.application.model.dto.ApplicationDto;
import pl.gov.cmp.application.model.entity.ApplicationEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationDtoMapper {

    List<ApplicationDto> toApplicationDtoList(List<ApplicationEntity> content);

}
