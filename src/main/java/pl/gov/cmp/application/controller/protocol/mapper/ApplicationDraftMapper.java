package pl.gov.cmp.application.controller.protocol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryDraftPageRequest;
import pl.gov.cmp.application.controller.protocol.response.ApplicationDraftPageResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationDraftResponse;
import pl.gov.cmp.application.model.dto.ApplicationCemeteryDraftCriteriaDto;
import pl.gov.cmp.application.model.dto.ApplicationDraftDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ApplicationDraftMapper {

    ApplicationCemeteryDraftCriteriaDto toApplicationDraftCriteriaDto(ApplicationCemeteryDraftPageRequest request);

    default ApplicationDraftPageResponse toApplicationDraftResponse(Page<ApplicationDraftDto> page, ApplicationCemeteryDraftCriteriaDto criteria) {
        return ApplicationDraftPageResponse.builder()
                .elements(toApplicationElements(page.getContent()))
                .pageIndex(criteria.getPageIndex())
                .sortColumn(criteria.getSortColumn())
                .sortOrder(criteria.getSortOrder())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    default List<ApplicationDraftResponse> toApplicationElements(List<ApplicationDraftDto> applications) {
        return Optional.of(applications.stream()
                .map(this::applicationDraftDtoToApplicationDraftResponse)
                .collect(Collectors.toList())).orElse(null);
    }

    default ApplicationDraftResponse applicationDraftDtoToApplicationDraftResponse(ApplicationDraftDto dto) {

        if (dto == null) {
            return null;
        }

        ApplicationDraftResponse applicationResponse = new ApplicationDraftResponse();
        applicationResponse.setDraft(dto.getDraft());
        applicationResponse.setUpdateDate(dto.getUpdateDate());
        applicationResponse.setCreateDate(dto.getCreateDate());
        applicationResponse.setUserAccountId(dto.getUserAccountId());
        applicationResponse.setId(dto.getId());
        applicationResponse.setDraftName(dto.getDraftName());
        return applicationResponse;

    }
}
