package pl.gov.cmp.application.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.application.model.dto.ApplicationDto;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationDtoMapper {

    List<ApplicationDto> toApplicationDtoList(List<ApplicationEntity> content);

    @Mapping(source = "appStatus", target = "appStatus", qualifiedByName = "enumToString")
    ApplicationDto applicationEntityToApplicationDto(ApplicationEntity applicationEntity);

    @Named("enumToString")
    default String getStatusName(ApplicationStatus status) {
        return status != null ? status.getName() : null;
    }
}
