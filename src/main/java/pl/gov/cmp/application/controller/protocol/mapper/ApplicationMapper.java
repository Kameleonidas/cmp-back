package pl.gov.cmp.application.controller.protocol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import pl.gov.cmp.application.controller.protocol.request.ApplicationPageRequest;
import pl.gov.cmp.application.controller.protocol.response.ApplicationPageResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationResponse;
import pl.gov.cmp.application.model.dto.*;
import pl.gov.cmp.application.model.enums.ApplicationType;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryCemeteryFacilityTypeDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ApplicationMapper {

    @Mapping(target = "userId",ignore = true)
    ApplicationCriteriaDto toApplicationCriteriaDto(ApplicationPageRequest request);

    default ApplicationPageResponse toApplicationResponse(Page<ApplicationDto> page, ApplicationCriteriaDto criteria) {
        return ApplicationPageResponse.builder()
                .elements(toApplicationElements(page.getContent()))
                .pageIndex(criteria.getPageIndex())
                .sortColumn(criteria.getSortColumn())
                .sortOrder(criteria.getSortOrder())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    default List<ApplicationResponse> toApplicationElements(List<ApplicationDto> applications) {
        return Optional.of(applications.stream()
                .map(this::applicationDtoToApplicationResponse)
                .collect(Collectors.toList())).orElse(null);
    }

    default ApplicationResponse applicationDtoToApplicationResponse(ApplicationDto applicationDto) {
        if ( applicationDto == null ) {
            return null;
        }

        ApplicationResponse applicationResponse = new ApplicationResponse();

        applicationResponse.setId( applicationDto.getId() );
        applicationResponse.setAppNumber( applicationDto.getAppNumber() );
        applicationResponse.setAppType( applicationDto.getAppType().getName() );
        applicationResponse.setAppStatus( applicationDto.getAppStatus() );
        applicationResponse.setObjectName( applicationDto.getObjectName() );
        applicationResponse.setCreateDate( applicationDto.getCreateDate() );
        applicationResponse.setUpdateDate( applicationDto.getUpdateDate() );

        UserAccountDto user = Optional.ofNullable(applicationDto.getOperator()).orElse(new UserAccountDto(null, "", "", "", "","", null, Collections.emptyList()));
        applicationResponse.setUserFirstName(user.getFirstName());
        applicationResponse.setUserLastName(user.getLastName());

        if(applicationDto.getApplication() != null){

            ApplicationCemeteryApplicantDto applicant = Optional.ofNullable(applicationDto.getApplication().getApplicant()).orElse(new ApplicationCemeteryApplicantDto(null, null, "", "", "", ""));
            applicationResponse.setApplicantFirstName(applicant.getFirstName());
            applicationResponse.setApplicantLastName(applicant.getLastName());

            DictionaryCemeteryFacilityTypeDto facilityType = Optional.ofNullable(applicationDto.getApplication().getFacilityType()).orElse(new DictionaryCemeteryFacilityTypeDto(null, "", ""));
            applicationResponse.setCemeteryFacilityType(facilityType.getName());

            applicationResponse.setCemeteryAppId( applicationDto.getApplication().getId() );
        } else {
            applicationResponse.setApplicantFirstName("");
            applicationResponse.setApplicantLastName("");
            applicationResponse.setCemeteryFacilityType("");
            applicationResponse.setCemeteryAppId(0L);
        }

        return applicationResponse;
    }

    default ApplicationCriteriaDto toUserApplicationCriteriaDto(ApplicationPageRequest request, Long userId) {
        return ApplicationCriteriaDto.builder()
            .userId(userId)
            .pageSize(request.getPageSize())
            .pageIndex(request.getPageIndex())
            .sortColumn(request.getSortColumn())
            .sortOrder(request.getSortOrder())
            .appNumber(request.getAppNumber())
            .appType(Enum.valueOf( ApplicationType.class, request.getAppType()))
            .appStatus(request.getAppStatus())
            .applicantFirstName(request.getApplicantFirstName())
            .applicantLastName(request.getApplicantLastName())
            .userFirstName(request.getUserFirstName())
            .userLastName(request.getUserLastName())
            .cemeteryFacilityType(request.getCemeteryFacilityType())
            .objectName(request.getObjectName())
            .createdDateFrom(request.getCreatedDateFrom())
            .createdDateTo(request.getCreatedDateTo())
            .modificationDateFrom(request.getModificationDateFrom())
            .modificationDateTo(request.getModificationDateTo())
            .build();
    }
}
