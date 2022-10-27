package pl.gov.cmp.application.controller.protocol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.application.controller.protocol.request.*;
import pl.gov.cmp.application.controller.protocol.response.ApplicationAddressResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryApplicantResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryManagerResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryOwnerResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryPerpetualUserResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryRepresentativeResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryUserAdminResponse;
import pl.gov.cmp.application.model.dto.*;
import pl.gov.cmp.application.model.enums.ApplicationStatus;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationCemeteryProtocolMapper {

    @Mapping(target="rejectionReasonDescription", ignore=true)
    ApplicationCemeteryDto toApplicationCemeteryDto(ApplicationCemeteryRequest request);

    List<ApplicationFileAttachmentDto> toFileAttachmentDto(List<FileAttachmentRequest> fileAttachmentRequest);

    @Mapping(target = "application.appType", source = "application.appType.name")
    @Mapping(source = "application.appStatus", target = "application.appStatus", qualifiedByName = "enumToString")
    ApplicationCemeteryResponse toApplicationCemeteryResponse(ApplicationCemeteryDto applicationCemeteryDto);

    ApplicationAddressDto toApplicationAddressDto(ApplicationAddressRequest address);

    ApplicationAddressResponse toApplicationAddressResponse(ApplicationAddressDto addressDto);

    ApplicationCemeteryApplicantDto toApplicationCemeteryApplicantDto(ApplicationCemeteryApplicantRequest applicant);

    ApplicationCemeteryApplicantResponse toApplicationCemeteryApplicantResponse(ApplicationCemeteryApplicantDto applicant);

    ApplicationCemeteryManagerDto toApplicationCemeteryManagerDto(ApplicationCemeteryManagerRequest manager);

    ApplicationCemeteryManagerResponse toApplicationCemeteryManagerResponse(ApplicationCemeteryManagerDto manager);

    ApplicationCemeteryOwnerDto toApplicationCemeteryOwnerDto(ApplicationCemeteryOwnerRequest owner);

    ApplicationCemeteryOwnerResponse toApplicationCemeteryOwnerResponse(ApplicationCemeteryOwnerDto owner);

    ApplicationCemeteryPerpetualUserDto toApplicationCemeteryPerpetualUserDto(ApplicationCemeteryPerpetualUserRequest perpetualUser);

    ApplicationCemeteryPerpetualUserResponse toApplicationCemeteryPerpetualUserResponse(ApplicationCemeteryPerpetualUserDto perpetualUser);

    ApplicationCemeteryRepresentativeDto toApplicationCemeteryRepresentativeDto(ApplicationCemeteryRepresentativeRequest representative);

    ApplicationCemeteryRepresentativeResponse toApplicationCemeteryRepresentativeResponse(ApplicationCemeteryRepresentativeDto representative);

    ApplicationCemeteryUserAdminDto toApplicationCemeteryUserAdminDto(ApplicationCemeteryUserAdminRequest userAdmin);

    ApplicationCemeteryUserAdminResponse toApplicationCemeteryUserAdminResponse(ApplicationCemeteryUserAdminDto userAdmin);

    @Named("enumToString")
    default String getStatusName(ApplicationStatus status) {
        return status != null ? status.getName() : null;
    }
}
