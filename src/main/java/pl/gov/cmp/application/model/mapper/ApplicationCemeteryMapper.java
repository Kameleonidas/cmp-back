package pl.gov.cmp.application.model.mapper;

import org.mapstruct.*;
import pl.gov.cmp.application.model.dto.*;
import pl.gov.cmp.application.model.entity.ApplicationAddressEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryApplicantEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryManagerEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryOwnerEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryPerpetualUserEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryRepresentativeEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryUserAdminEntity;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.model.entity.ApplicationAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationCemeteryAttachmentEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ApplicationCemeteryMapper {

    @Mapping(target = "application", ignore = true)
    @Mapping(target = "applicationAttachmentFiles", ignore = true)
    @Mapping(target = "cemeteryAttachmentFiles", ignore = true)
    @Mapping(target = "image", ignore = true)
    ApplicationCemeteryEntity toApplicationCemeteryEntity(ApplicationCemeteryDto applicationCemetery);

    @Mapping(target = "rejectionReasonDescription", expression = "java(this.getRejectionReasonDescription(applicationCemetery))")
    @Mapping(target = "cemeteryAttachmentFiles", ignore = true)
    ApplicationCemeteryDto toApplicationCemeteryDto(ApplicationCemeteryEntity applicationCemetery);

    default String getRejectionReasonDescription(ApplicationCemeteryEntity applicationCemetery) {
        if(applicationCemetery.getApplication().getRejectionReasonDescription() != null) {
            return applicationCemetery.getApplication().getRejectionReasonDescription();
        } else {
            return null;
        }
    }

    @IterableMapping(qualifiedByName="toApplicationAttachmentEntityWithoutField")
    List<ApplicationAttachmentEntity> toApplicationAttachmentEntity(List<ApplicationFileAttachmentDto> fileAttachmentDto);

    @Named("toApplicationAttachmentEntityWithoutField")
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "id", ignore = true)
    ApplicationAttachmentEntity toApplicationAttachmentEntityWithoutField(ApplicationFileAttachmentDto fileAttachmentDto);

    @IterableMapping(qualifiedByName="toCemeteryEntityWithoutField")
    List<ApplicationCemeteryAttachmentEntity> toApplicationCemeteryAttachmentEntity(List<CemeteryAttachmentDto> fileAttachmentDto);

    @Named("toCemeteryEntityWithoutField")
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "id", ignore = true)
    ApplicationCemeteryAttachmentEntity toApplicationCemeteryEntityWithoutField(CemeteryAttachmentDto cemeteryAttachmentDto);

    ApplicationAddressEntity toApplicationAddressEntity(ApplicationAddressDto address);

    ApplicationAddressDto toApplicationAddressDto(ApplicationAddressEntity address);

    @Mapping(target = "application", ignore = true)
    ApplicationCemeteryApplicantEntity toApplicationCemeteryApplicantEntity(ApplicationCemeteryApplicantDto applicant);

    ApplicationCemeteryApplicantDto toApplicationCemeteryApplicantDto(ApplicationCemeteryApplicantEntity applicant);

    @Mapping(target = "application", ignore = true)
    ApplicationCemeteryManagerEntity toApplicationCemeteryManagerEntity(ApplicationCemeteryManagerDto manager);

    ApplicationCemeteryManagerDto toApplicationCemeteryManagerDto(ApplicationCemeteryManagerEntity manager);

    @Mapping(target = "application", ignore = true)
    ApplicationCemeteryOwnerEntity toApplicationCemeteryOwnerEntity(ApplicationCemeteryOwnerDto owner);

    ApplicationCemeteryOwnerDto toApplicationCemeteryOwnerDto(ApplicationCemeteryOwnerEntity owner);

    @Mapping(target = "application", ignore = true)
    ApplicationCemeteryPerpetualUserEntity toApplicationCemeteryPerpetualUserEntity(ApplicationCemeteryPerpetualUserDto perpetualUser);

    ApplicationCemeteryPerpetualUserDto toApplicationCemeteryPerpetualUserDto(ApplicationCemeteryPerpetualUserEntity perpetualUser);

    @Mapping(target = "application", ignore = true)
    ApplicationCemeteryRepresentativeEntity toApplicationCemeteryRepresentativeEntity(ApplicationCemeteryRepresentativeDto representative);

    ApplicationCemeteryRepresentativeDto toApplicationCemeteryRepresentativeDto(ApplicationCemeteryRepresentativeEntity representative);

    @Mapping(target = "application", ignore = true)
    ApplicationCemeteryUserAdminEntity toApplicationCemeteryUserAdminEntity(ApplicationCemeteryUserAdminDto userAdmin);

    ApplicationCemeteryUserAdminDto toApplicationCemeteryUserAdminDto(ApplicationCemeteryUserAdminEntity userAdmin);

    @Mapping(target = "application", ignore = true)
    ApplicationAttachmentEntity toApplicationAttachmentEntity(FileResourceDto fileResourceDto);

}
