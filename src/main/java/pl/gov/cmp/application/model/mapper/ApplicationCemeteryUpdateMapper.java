package pl.gov.cmp.application.model.mapper;

import org.mapstruct.*;
import pl.gov.cmp.application.model.dto.*;
import pl.gov.cmp.application.model.entity.*;
import pl.gov.cmp.cemetery.model.entity.CemeteryOwnerEntity;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.model.entity.ApplicationAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationImageEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ApplicationCemeteryUpdateMapper {

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationAttachmentFiles", ignore = true)
    @Mapping(target = "cemeteryAttachmentFiles", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "facilityType", ignore = true)
    void updateApplicationCemeteryEntity(@MappingTarget ApplicationCemeteryEntity applicationCemeteryOwnerEntity, ApplicationCemeteryDto applicationCemeteryOwnerDto);

    @Mapping(target = "id", ignore = true)
    void updateApplicationAddressEntity(@MappingTarget ApplicationAddressEntity applicationAddressEntity, ApplicationAddressDto address);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationCemeteryApplicantEntity(@MappingTarget ApplicationCemeteryApplicantEntity applicationCemeteryApplicantEntity, ApplicationCemeteryApplicantDto applicant);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationCemeteryPerpetualUserEntity(@MappingTarget ApplicationCemeteryPerpetualUserEntity applicationCemeteryPerpetualUserEntity, ApplicationCemeteryPerpetualUserDto perpetualUser);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationCemeteryOwnerEntity(@MappingTarget ApplicationCemeteryOwnerEntity applicationCemeteryOwnerEntity, ApplicationCemeteryOwnerDto owner);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationCemeteryManagerEntity(@MappingTarget ApplicationCemeteryManagerEntity applicationCemeteryManagerEntity, ApplicationCemeteryManagerDto manager);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationCemeteryRepresentativeEntity(@MappingTarget ApplicationCemeteryRepresentativeEntity applicationCemeteryRepresentativeEntity, ApplicationCemeteryRepresentativeDto representative);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationCemeteryUserAdminEntity(@MappingTarget ApplicationCemeteryUserAdminEntity applicationCemeteryUserAdminEntity, ApplicationCemeteryUserAdminDto userAdmin);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationAttachmentEntity(@MappingTarget  ApplicationAttachmentEntity applicationAttachmentEntity, FileResourceDto fileResourceDto);

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateApplicationImageEntity(@MappingTarget ApplicationImageEntity applicationImageEntity, FileResourceDto fileResourceDto);

    @Mapping(target = "id", ignore = true)
    void updateApplicationCommunityNameEntity(@MappingTarget ApplicationCommunityNameEntity applicationCommunityNameEntity, ApplicationCommunityNameDto applicationCommunityNameDto);

    @Mapping(target = "id", ignore = true)
    void updateApplicationNameLocalGovernmentUnitEntity(@MappingTarget ApplicationNameLocalGovernmentUnitEntity applicationNameLocalGovernmentUnitEntity, ApplicationNameLocalGovernmentUnitDto applicationNameLocalGovernmentUnitDto);

    @Mapping(target = "id", ignore = true)
    void updateApplicationOwnerCommunityNameEntity(@MappingTarget ApplicationOwnerCommunityNameEntity applicationOwnerCommunityNameEntity, ApplicationOwnerCommunityNameDto applicationOwnerCommunityNameDto);
    
}
