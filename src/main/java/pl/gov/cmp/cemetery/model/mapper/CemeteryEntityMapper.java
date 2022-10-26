package pl.gov.cmp.cemetery.model.mapper;

import pl.gov.cmp.application.model.entity.*;
import pl.gov.cmp.cemetery.model.entity.*;
import pl.gov.cmp.dictionary.model.entity.CemeteryOwnerCategoryDictionaryEntity;
import pl.gov.cmp.dictionary.model.entity.CemeterySourceDictionaryEntity;
import pl.gov.cmp.dictionary.model.entity.CemeteryTypeDictionaryEntity;
import pl.gov.cmp.dictionary.model.entity.ChurchReligionDictionaryEntity;
import pl.gov.cmp.file.model.entity.ApplicationAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationCemeteryAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationImageEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.gov.cmp.dictionary.model.entity.CemeteryFacilityTypeDictionaryEntity.createWithId;

public interface CemeteryEntityMapper {

    static CemeteryEntity toCemetery(ApplicationCemeteryEntity applicationCemeteryEntity, CemeterySourceDictionaryEntity cemeterySourceDictionaryEntity, ChurchReligionDictionaryEntity religion, CemeteryOwnerCategoryDictionaryEntity cemeteryOwnerCategory) {
        if (applicationCemeteryEntity == null) {
            return null;
        }
        var locationAddress = toCemeteryAddressEntity(applicationCemeteryEntity.getLocationAddress());
        var concatAddress = toCemeteryAddressEntity(applicationCemeteryEntity.getContactAddress());
        var cemeteryPerpetualUser = toApplicationCemeteryPerpetualUserEntity(applicationCemeteryEntity.getPerpetualUser());
        var cemeteryOwner = toCemeteryOwnerEntity(applicationCemeteryEntity.getOwner());
        var cemeteryManager = toCemeteryManagerEntity(applicationCemeteryEntity.getManager());
        var cemeteryUserAdmin = toApplicationCemeteryUserAdminEntity(applicationCemeteryEntity.getUserAdmin());
        var cemeteryAttachments = toCemeteryAttachmentEntity(applicationCemeteryEntity.getCemeteryAttachmentFiles(), applicationCemeteryEntity.getApplicationAttachmentFiles(), applicationCemeteryEntity.getImage());

        var facilityType = Optional.ofNullable(applicationCemeteryEntity.getFacilityType())
                .map(facility -> createWithId(facility.getId())).orElse(null);
        
        CemeteryEntity cemeteryEntity = CemeteryEntity
                .builder()
                .name(applicationCemeteryEntity.getObjectName())
                .cemeteryTypeId(applicationCemeteryEntity.getCemeteryTypeId())
                .type(CemeteryTypeDictionaryEntity.createWithId(applicationCemeteryEntity.getCemeteryTypeId()))
                .churchOwner(applicationCemeteryEntity.isChurchOwner())
                .closeDate(applicationCemeteryEntity.getCloseDate())
                .churchPerpetualUser(applicationCemeteryEntity.isChurchPerpetualUser())
                .churchRegulatedByLaw(applicationCemeteryEntity.isChurchRegulatedByLaw())
                .closeTermType(applicationCemeteryEntity.getCloseTermType())
                .openTermType(applicationCemeteryEntity.getOpenTermType())
                .createDate(LocalDateTime.now())
                .openDate(applicationCemeteryEntity.getOpenDate())
                .closeDate(applicationCemeteryEntity.getCloseDate())
                .userAdminExists(applicationCemeteryEntity.isUserAdminExists())
                .userAdminExists(applicationCemeteryEntity.isUserAdminExists())
                .perpetualUse(applicationCemeteryEntity.isPerpetualUse())
                .managerExists(applicationCemeteryEntity.isManagerExists())
                .religion(religion)
                .otherReligion(applicationCemeteryEntity.getOtherReligion())
                .description(applicationCemeteryEntity.getObjectDescription())
                .email(applicationCemeteryEntity.getContactEmail())
                .source(cemeterySourceDictionaryEntity)
                .facilityType(facilityType)
                .phoneNumber(applicationCemeteryEntity.getContactPhoneNumber())
                .phoneNumber(applicationCemeteryEntity.getContactPhoneNumber())
                .status(applicationCemeteryEntity.getCemeteryStatus())
                .otherType(applicationCemeteryEntity.getOtherType())
                .substitutePerformance(applicationCemeteryEntity.isSubstitutePerformance())
                .locationAddress(locationAddress)
                .contactAddress(concatAddress)
                .ownerCategory(cemeteryOwnerCategory)
                .owner(cemeteryOwner)
                .cemeteryPerpetualUser(cemeteryPerpetualUser)
                .manager(cemeteryManager)
                .userAdmin(cemeteryUserAdmin)
                .attachmentFiles(new HashSet<>(cemeteryAttachments))
                .build();
        cemeteryAttachments.forEach(cemeteryAttachmentArg -> cemeteryAttachmentArg.setCemetery(cemeteryEntity));
        return cemeteryEntity;
    }

    static List<CemeteryAttachmentEntity> toCemeteryAttachmentEntity(List<ApplicationCemeteryAttachmentEntity> cemeteryAttachmentFiles, List<ApplicationAttachmentEntity> applicationAttachmentFiles, List<ApplicationImageEntity> images) {
        List<CemeteryAttachmentEntity> attachments = new ArrayList<>();

        var cemeteryAttachments = cemeteryAttachmentFiles
                .stream()
                .map(attachment -> CemeteryAttachmentEntity
                        .builder()
                        .fileHashedName(attachment.getFileHashedName())
                        .size(attachment.getSize())
                        .fileName(attachment.getFileName())
                        .build())
                .collect(Collectors.toList());

        var applicationAttachments = applicationAttachmentFiles
                .stream()
                .map(attachment -> CemeteryAttachmentEntity
                        .builder()
                        .fileHashedName(attachment.getFileHashedName())
                        .size(attachment.getSize())
                        .fileName(attachment.getFileName())
                        .build())
                .collect(Collectors.toList());

        var imagesAttachments = images
                .stream()
                .map(attachment -> CemeteryAttachmentEntity
                        .builder()
                        .fileHashedName(attachment.getFileHashedName())
                        .size(attachment.getSize())
                        .build())
                .collect(Collectors.toList());

        attachments.addAll(cemeteryAttachments);
        attachments.addAll(applicationAttachments);
        attachments.addAll(imagesAttachments);
        return attachments;
    }


    static CemeteryAddressEntity toCemeteryAddressEntity (ApplicationAddressEntity applicationAddressEntity) {
        if(applicationAddressEntity == null) {
            return null;
        }
        return CemeteryAddressEntity
                .builder()
                .commune(applicationAddressEntity.getCommune())
                .communeTercCode(applicationAddressEntity.getCommuneTercCode())
                .place(applicationAddressEntity.getPlace())
                .voivodeship(applicationAddressEntity.getVoivodeship())
                .voivodeshipTercCode(applicationAddressEntity.getVoivodeshipTercCode())
                .districtTercCode(applicationAddressEntity.getDistrictTercCode())
                .placeSimcCode(applicationAddressEntity.getPlaceSimcCode())
                .district(applicationAddressEntity.getDistrict())
                .postName(applicationAddressEntity.getPostName())
                .street(applicationAddressEntity.getStreet())
                .number(applicationAddressEntity.getNumber())
                .zipCode(applicationAddressEntity.getZipCode())
                .build();
    }

    static CemeteryManagerEntity toCemeteryManagerEntity(ApplicationCemeteryManagerEntity applicationCemeteryManagerEntity) {
        if(applicationCemeteryManagerEntity == null) {
            return null;
        }
        return CemeteryManagerEntity
                .builder()
                .email(applicationCemeteryManagerEntity.getEmail())
                .firstName(applicationCemeteryManagerEntity.getFirstName())
                .name(applicationCemeteryManagerEntity.getName())
                .lastName(applicationCemeteryManagerEntity.getLastName())
                .nip(applicationCemeteryManagerEntity.getNip())
                .type(applicationCemeteryManagerEntity.getType())
                .regon(applicationCemeteryManagerEntity.getRegon())
                .representative(toCemeteryManagerEntity(applicationCemeteryManagerEntity.getRepresentative()))
                .build();
    }


    static CemeteryRepresentativeEntity toCemeteryManagerEntity(ApplicationCemeteryRepresentativeEntity applicationCemeteryRepresentativeEntity) {
        if(applicationCemeteryRepresentativeEntity == null) {
            return null;
        }
        return CemeteryRepresentativeEntity
                .builder()
                .firstName(applicationCemeteryRepresentativeEntity.getFirstName())
                .lastName(applicationCemeteryRepresentativeEntity.getLastName())
                .type(applicationCemeteryRepresentativeEntity.getType())
                .email(applicationCemeteryRepresentativeEntity.getEmail())
                .build();
    }

    static CemeteryPerpetualUserEntity toApplicationCemeteryPerpetualUserEntity(ApplicationCemeteryPerpetualUserEntity applicationCemeteryPerpetualUserEntity) {
        if(applicationCemeteryPerpetualUserEntity == null) {
            return null;
        }
        return CemeteryPerpetualUserEntity
                        .builder()
                        .email(applicationCemeteryPerpetualUserEntity.getEmail())
                        .firstName(applicationCemeteryPerpetualUserEntity.getFirstName())
                        .lastName(applicationCemeteryPerpetualUserEntity.getLastName())
                        .name(applicationCemeteryPerpetualUserEntity.getName())
                        .nip(applicationCemeteryPerpetualUserEntity.getNip())
                        .religionId(applicationCemeteryPerpetualUserEntity.getReligionId())
                        .perpetualChurchRegulatedByLawId(applicationCemeteryPerpetualUserEntity.getPerpetualChurchRegulatedByLawId())
                        .perpetualChurchNotRegulatedByLawId(applicationCemeteryPerpetualUserEntity.getPerpetualChurchNotRegulatedByLawId())
                        .perpetualChurchesRegulatedByLawOrNo(applicationCemeteryPerpetualUserEntity.getPerpetualChurchesRegulatedByLawOrNo())
                        .nameOfParishPerpetualUse(applicationCemeteryPerpetualUserEntity.getNameOfParishPerpetualUse())
                        .type(applicationCemeteryPerpetualUserEntity.getType())
                        .regon(applicationCemeteryPerpetualUserEntity.getRegon())
                        .representative(toCemeteryRepresentativeEntity(applicationCemeteryPerpetualUserEntity.getRepresentative()))
                        .build();
    }

    static CemeteryRepresentativeEntity toCemeteryRepresentativeEntity(ApplicationCemeteryRepresentativeEntity cemeteryRepresentativeEntity) {
        if(cemeteryRepresentativeEntity == null) {
            return null;
        }
        return CemeteryRepresentativeEntity
                        .builder()
                        .type(cemeteryRepresentativeEntity.getType())
                        .email(cemeteryRepresentativeEntity.getEmail())
                        .firstName(cemeteryRepresentativeEntity.getFirstName())
                        .lastName(cemeteryRepresentativeEntity.getLastName())
                        .build();
    }

    static CemeteryOwnerEntity toCemeteryOwnerEntity(ApplicationCemeteryOwnerEntity applicationCemeteryOwnerEntity) {
        if(applicationCemeteryOwnerEntity == null) {
            return null;
        }
        var communityName = toCommunityNameEntity(applicationCemeteryOwnerEntity.getCommunityName());
        var ownerCommunityName = toOwnerCommunityNameEntity(applicationCemeteryOwnerEntity.getOwnerCommunityName());
        var nameLocalGovernmentUnit = toNameLocalGovernmentUnit(applicationCemeteryOwnerEntity.getCommunityName());
        return CemeteryOwnerEntity
                .builder()
                .address(toCemeteryAddressEntity(applicationCemeteryOwnerEntity.getAddress()))
                .email(applicationCemeteryOwnerEntity.getEmail())
                .firstName(applicationCemeteryOwnerEntity.getFirstName())
                .lastName(applicationCemeteryOwnerEntity.getLastName())
                .communityName(communityName)
                .applicationIsOwner(applicationCemeteryOwnerEntity.getApplicationIsOwner())
                .ownerCompanyName(applicationCemeteryOwnerEntity.getOwnerCompanyName())
                .unitWithoutLegalPersonalityName(applicationCemeteryOwnerEntity.getUnitWithoutLegalPersonalityName())
                .churchNotRegulatedByLawId(applicationCemeteryOwnerEntity.getChurchNotRegulatedByLawId())
                .churchRegulatedByLawId(applicationCemeteryOwnerEntity.getChurchRegulatedByLawId())
                .nameOfParish(applicationCemeteryOwnerEntity.getNameOfParish())
                .perpetualOwnerTypeId(applicationCemeteryOwnerEntity.getPerpetualOwnerTypeId())
                .userInsteadOwnerOrPerpetualUser(applicationCemeteryOwnerEntity.getUserInsteadOwnerOrPerpetualUser())
                .nameAssociationLocalGovernment(applicationCemeteryOwnerEntity.getNameAssociationLocalGovernment())
                .workEmailInstitutionObjectOwner(applicationCemeteryOwnerEntity.getWorkEmailInstitutionObjectOwner())
                .ownerCommunityName(ownerCommunityName)
                .nameLocalGovernmentUnit(nameLocalGovernmentUnit)
                .representative(toCemeteryRepresentativeEntity(applicationCemeteryOwnerEntity.getRepresentative()))
                .name(applicationCemeteryOwnerEntity.getName())
                .nip(applicationCemeteryOwnerEntity.getNip())
                .regon(applicationCemeteryOwnerEntity.getRegon())
                .ownerCategoryId(applicationCemeteryOwnerEntity.getOwnerCategoryId())
                .territorialUnitType(applicationCemeteryOwnerEntity.getTerritorialUnitType())
                .build();

    }

    static CemeteryNameLocalGovernmentUnitEntity toNameLocalGovernmentUnit(ApplicationCommunityNameEntity communityName) {
        if(communityName == null) {
            return null;
        }
        return CemeteryNameLocalGovernmentUnitEntity
                .builder()
                .name(communityName.getName())
                .levelName(communityName.getLevelName())
                .codeSimc(communityName.getCodeSimc())
                .source(communityName.getSource())
                .build();
    }

    static CemeteryOwnerCommunityNameEntity toOwnerCommunityNameEntity(ApplicationOwnerCommunityNameEntity ownerCommunityName) {
        if(ownerCommunityName == null) {
            return null;
        }
        return CemeteryOwnerCommunityNameEntity
                .builder()
                .name(ownerCommunityName.getName())
                .levelName(ownerCommunityName.getLevelName())
                .codeSimc(ownerCommunityName.getCodeSimc())
                .source(ownerCommunityName.getSource())
                .build();
    }

    static CemeteryCommunityNameEntity toCommunityNameEntity(ApplicationCommunityNameEntity communityName) {
        if(communityName == null) {
            return null;
        }
        return CemeteryCommunityNameEntity
                .builder()
                .name(communityName.getName())
                .levelName(communityName.getLevelName())
                .codeSimc(communityName.getCodeSimc())
                .source(communityName.getSource())
                .build();
    }

     static CemeteryUserAdminEntity toApplicationCemeteryUserAdminEntity(ApplicationCemeteryUserAdminEntity applicationCemeteryUserAdminEntity) {
        if(applicationCemeteryUserAdminEntity == null) {
            return null;
        }
        return CemeteryUserAdminEntity
                .builder()
                .email(applicationCemeteryUserAdminEntity.getEmail())
                .firstName(applicationCemeteryUserAdminEntity.getFirstName())
                .adminDataTheSameAsObjManagerOrPerpUserOrObjOwner(applicationCemeteryUserAdminEntity.getAdminDataTheSameAsObjManagerOrPerpUserOrObjOwner())
                .lastName(applicationCemeteryUserAdminEntity.getLastName())
                .build();
    }
}
