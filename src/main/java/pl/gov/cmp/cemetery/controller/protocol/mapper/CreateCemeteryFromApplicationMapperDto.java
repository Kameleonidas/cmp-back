package pl.gov.cmp.cemetery.controller.protocol.mapper;

import pl.gov.cmp.cemetery.model.dto.*;
import pl.gov.cmp.cemetery.model.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface CreateCemeteryFromApplicationMapperDto {

    public static CemeteryDto toCemeteryDto(CemeteryEntity cemeteryEntity) {
        if (cemeteryEntity == null) {
            return null;
        }
        var locationAddress =  toCemeteryAddressDto(cemeteryEntity.getLocationAddress());
        var contactAddress =  toCemeteryAddressDto(cemeteryEntity.getContactAddress());
        var cemeteryPerpetualUser = toApplicationCemeteryPerpetualUserDto(cemeteryEntity.getCemeteryPerpetualUser());
        var cemeteryOwner = toCemeteryOwnerDto(cemeteryEntity.getOwner());
        var cemeteryManager = toCemeteryManagerDto(cemeteryEntity.getManager());
        var cemeteryUserAdmin = toApplicationCemeteryUserAdminDto(cemeteryEntity.getUserAdmin());
        var cemeteryAttachment = toCemeteryAttachmentDto(new ArrayList<>(cemeteryEntity.getAttachmentFiles()));
        return CemeteryDto
                .builder()
                .id(cemeteryEntity.getId())
                .name(cemeteryEntity.getObjectName())
                .cemeteryTypeId(cemeteryEntity.getCemeteryTypeId())
                .churchOwner(cemeteryEntity.getChurchOwner())
                .closeDate(cemeteryEntity.getCloseDate())
                .churchPerpetualUser(cemeteryEntity.getChurchRegulatedByLaw())
                .closeTermType(cemeteryEntity.getCloseTermType())
                .openTermType(cemeteryEntity.getOpenTermType())
                .createDate(LocalDate.now())
                .openDate(cemeteryEntity.getOpenDate())
                .closeDate(cemeteryEntity.getCloseDate())
                .description(cemeteryEntity.getDescription())
                .email(cemeteryEntity.getEmail())
                .facilityType(cemeteryEntity.getFacilityType().getName())
                .phoneNumber(cemeteryEntity.getPhoneNumber())
                .status(cemeteryEntity.getStatus().toString())
                .otherType(cemeteryEntity.getOtherType())
                .substitutePerformance(cemeteryEntity.getSubstitutePerformance())
                .locationAddress(locationAddress)
                .contactAddress(contactAddress)
                .owner(cemeteryOwner)
                .cemeteryPerpetualUser(cemeteryPerpetualUser)
                .owner(cemeteryOwner)
                .manager(cemeteryManager)
                .userAdmin(cemeteryUserAdmin)
                .attachmentFiles(cemeteryAttachment)
                .build();

    }

    static List<CemeteryAttachmentDto> toCemeteryAttachmentDto(List<CemeteryAttachmentEntity> cemeteryAttachmentFiles) {
        return cemeteryAttachmentFiles
                .stream()
                .map(attachment -> CemeteryAttachmentDto
                        .builder()
                        .fileHashedName(attachment.getFileHashedName())
                        .size(attachment.getSize())
                        .fileName(attachment.getFileName())
                        .build())
                .collect(Collectors.toList());
    }


    public static CemeteryAddressDto toCemeteryAddressDto (CemeteryAddressEntity cemeteryAddressEntity) {
        if(cemeteryAddressEntity == null) {
            return null;
        }
        return CemeteryAddressDto
                .builder()
                .commune(cemeteryAddressEntity.getCommune())
                .communeTercCode(cemeteryAddressEntity.getCommuneTercCode())
                .place(cemeteryAddressEntity.getPlace())
                .voivodeship(cemeteryAddressEntity.getVoivodeship())
                .voivodeshipTercCode(cemeteryAddressEntity.getVoivodeshipTercCode())
                .districtTercCode(cemeteryAddressEntity.getDistrictTercCode())
                .placeSimcCode(cemeteryAddressEntity.getPlaceSimcCode())
                .district(cemeteryAddressEntity.getDistrict())
                .postName(cemeteryAddressEntity.getPostName())
                .street(cemeteryAddressEntity.getStreet())
                .number(cemeteryAddressEntity.getNumber())
                .zipCode(cemeteryAddressEntity.getZipCode())
                .build();
    }

    public static CemeteryManagerDto toCemeteryManagerDto(CemeteryManagerEntity cemeteryCemeteryManagerEntity) {
        if(cemeteryCemeteryManagerEntity == null) {
            return null;
        }
        return CemeteryManagerDto
                .builder()
                .email(cemeteryCemeteryManagerEntity.getEmail())
                .firstName(cemeteryCemeteryManagerEntity.getFirstName())
                .name(cemeteryCemeteryManagerEntity.getName())
                .lastName(cemeteryCemeteryManagerEntity.getLastName())
                .nip(cemeteryCemeteryManagerEntity.getNip())
                .type(cemeteryCemeteryManagerEntity.getType())
                .regon(cemeteryCemeteryManagerEntity.getRegon())
                .representative(toCemeteryManagerDto(cemeteryCemeteryManagerEntity.getRepresentative()))
                .build();
    }


    public static CemeteryRepresentativeDto toCemeteryManagerDto(CemeteryRepresentativeEntity cemeteryRepresentativeEntity) {
        if(cemeteryRepresentativeEntity == null) {
            return null;
        }
        return CemeteryRepresentativeDto
                .builder()
                .firstName(cemeteryRepresentativeEntity.getFirstName())
                .lastName(cemeteryRepresentativeEntity.getLastName())
                .type(cemeteryRepresentativeEntity.getType())
                .email(cemeteryRepresentativeEntity.getEmail())
                .nameRepresentationPersonObjectManagerLegalPerson(cemeteryRepresentativeEntity.getNameRepresentationPersonObjectManagerLegalPerson())
                .surnameRepresentationPersonObjectManagerLegalPerson(cemeteryRepresentativeEntity.getSurnameRepresentationPersonObjectManagerLegalPerson())
                .emailRepresentationPersonObjectManagerLegalPerson(cemeteryRepresentativeEntity.getEmailRepresentationPersonObjectManagerLegalPerson())
                .build();
    }

    public static CemeteryPerpetualUserDto toApplicationCemeteryPerpetualUserDto(CemeteryPerpetualUserEntity cemeteryPerpetualUserEntity) {
        if(cemeteryPerpetualUserEntity == null) {
            return null;
        }
        return
                CemeteryPerpetualUserDto
                        .builder()
                        .email(cemeteryPerpetualUserEntity.getEmail())
                        .firstName(cemeteryPerpetualUserEntity.getFirstName())
                        .lastName(cemeteryPerpetualUserEntity.getLastName())
                        .name(cemeteryPerpetualUserEntity.getName())
                        .nip(cemeteryPerpetualUserEntity.getNip())
                        .religionId(cemeteryPerpetualUserEntity.getReligionId())
                        .type(cemeteryPerpetualUserEntity.getType())
                        .regon(cemeteryPerpetualUserEntity.getRegon())
                        .representative(toCemeteryRepresentativeDto(cemeteryPerpetualUserEntity.getRepresentative()))
                        .nameOfParishPerpetualUse(cemeteryPerpetualUserEntity.getNameOfParishPerpetualUse())
                        .perpetualChurchesRegulatedByLawOrNo(cemeteryPerpetualUserEntity.getPerpetualChurchesRegulatedByLawOrNo())
                        .perpetualChurchRegulatedByLawId(cemeteryPerpetualUserEntity.getPerpetualChurchRegulatedByLawId())
                        .build();
    }

    public static CemeteryRepresentativeDto toCemeteryRepresentativeDto(CemeteryRepresentativeEntity cemeteryRepresentativeEntity) {
        if(cemeteryRepresentativeEntity == null) {
            return null;
        }
        return
                CemeteryRepresentativeDto
                        .builder()
                        .type(cemeteryRepresentativeEntity.getType())
                        .email(cemeteryRepresentativeEntity.getEmail())
                        .firstName(cemeteryRepresentativeEntity.getFirstName())
                        .lastName(cemeteryRepresentativeEntity.getLastName())
                        .build();
    }

    public static CemeteryOwnerDto toCemeteryOwnerDto(CemeteryOwnerEntity cemeteryOwnerEntity) {
        if(cemeteryOwnerEntity == null) {
            return null;
        }
        return CemeteryOwnerDto
                .builder()
                .address(toCemeteryAddressDto(cemeteryOwnerEntity.getAddress()))
                .email(cemeteryOwnerEntity.getEmail())
                .firstName(cemeteryOwnerEntity.getFirstName())
                .lastName(cemeteryOwnerEntity.getLastName())
                .representative(toCemeteryRepresentativeDto(cemeteryOwnerEntity.getRepresentative()))
                .name(cemeteryOwnerEntity.getName())
                .nip(cemeteryOwnerEntity.getNip())
                .regon(cemeteryOwnerEntity.getRegon())
                .ownerCategoryId(cemeteryOwnerEntity.getOwnerCategoryId())
                .territorialUnitType(cemeteryOwnerEntity.getTerritorialUnitType())
                .build();
    }

    public static CemeteryUserAdminDto toApplicationCemeteryUserAdminDto(CemeteryUserAdminEntity cemeteryCemeteryUserAdminEntity) {
        if(cemeteryCemeteryUserAdminEntity == null) {
            return null;
        }
        return CemeteryUserAdminDto
                .builder()
                .email(cemeteryCemeteryUserAdminEntity.getEmail())
                .firstName(cemeteryCemeteryUserAdminEntity.getFirstName())
                .lastName(cemeteryCemeteryUserAdminEntity.getLastName())
                .adminDataTheSameAsObjManagerOrPerpUserOrObjOwner(cemeteryCemeteryUserAdminEntity.getAdminDataTheSameAsObjManagerOrPerpUserOrObjOwner())
                .build();
    }


}
