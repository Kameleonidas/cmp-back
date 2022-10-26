package pl.gov.cmp.application.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.application.model.dto.ApplicationDraftDto;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryDraftEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationDraftDtoMapper {

    List<ApplicationDraftDto> toApplicationDraftDtoList(List<ApplicationCemeteryDraftEntity> content);
}
