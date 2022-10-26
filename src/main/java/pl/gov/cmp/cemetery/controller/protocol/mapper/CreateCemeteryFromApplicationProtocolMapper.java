package pl.gov.cmp.cemetery.controller.protocol.mapper;

import pl.gov.cmp.cemetery.controller.protocol.response.*;
import pl.gov.cmp.cemetery.model.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface CreateCemeteryFromApplicationProtocolMapper {

    public static CemeteryResponse toCemeteryResponse(CemeteryDto cemeteryDto) {
        var locationAddress =  toCemeteryAddressResponse(cemeteryDto.getLocationAddress());
        var contactAddress =  toCemeteryAddressResponse(cemeteryDto.getContactAddress());
        var cemeteryPerpetualUser = toApplicationCemeteryPerpetualUserResponse(cemeteryDto.getCemeteryPerpetualUser());
        var cemeteryOwner = toCemeteryOwnerResponse(cemeteryDto.getOwner());
        var cemeteryManager = toCemeteryManagerResponse(cemeteryDto.getManager());
        var cemeteryUserAdmin = toApplicationCemeteryUserAdminResponse(cemeteryDto.getUserAdmin());
        var cemeteryAttachment = toCemeteryAttachmentResponse(cemeteryDto.getAttachmentFiles());
        return CemeteryResponse
                .builder()
                .name(cemeteryDto.getName())
                .cemeteryTypeId(cemeteryDto.getCemeteryTypeId())
                .churchOwner(cemeteryDto.isChurchOwner())
                .closeDate(cemeteryDto.getCloseDate())
                .churchPerpetualUser(cemeteryDto.isChurchRegulatedByLaw())
                .closeTermType(cemeteryDto.getCloseTermType())
                .openTermType(cemeteryDto.getOpenTermType())
                .createDate(LocalDate.now())
                .openDate(cemeteryDto.getOpenDate())
                .closeDate(cemeteryDto.getCloseDate())
                .description(cemeteryDto.getDescription())
                .email(cemeteryDto.getEmail())
                .facilityType(cemeteryDto.getFacilityType())
                .phoneNumber(cemeteryDto.getPhoneNumber())
                .status(cemeteryDto.getStatus())
                .otherType(cemeteryDto.getOtherType())
                .substitutePerformance(cemeteryDto.isSubstitutePerformance())
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

    static List<CemeteryAttachmentResponse> toCemeteryAttachmentResponse(List<CemeteryAttachmentDto> cemeteryAttachmentFiles) {
        return cemeteryAttachmentFiles
                .stream()
                .map(attachment -> CemeteryAttachmentResponse
                        .builder()
                        .fileHashedName(attachment.getFileHashedName())
                        .size(attachment.getSize())
                        .fileName(attachment.getFileName())
                        .build())
                .collect(Collectors.toList());
    }


    public static CemeteryAddressResponse toCemeteryAddressResponse (CemeteryAddressDto cemeteryAddressDto) {
        if(cemeteryAddressDto == null) {
            return null;
        }
        return CemeteryAddressResponse
                .builder()
                .commune(cemeteryAddressDto.getCommune())
                .communeTercCode(cemeteryAddressDto.getCommuneTercCode())
                .place(cemeteryAddressDto.getPlace())
                .voivodeship(cemeteryAddressDto.getVoivodeship())
                .voivodeshipTercCode(cemeteryAddressDto.getVoivodeshipTercCode())
                .districtTercCode(cemeteryAddressDto.getDistrictTercCode())
                .placeSimcCode(cemeteryAddressDto.getPlaceSimcCode())
                .district(cemeteryAddressDto.getDistrict())
                .postName(cemeteryAddressDto.getPostName())
                .street(cemeteryAddressDto.getStreet())
                .number(cemeteryAddressDto.getNumber())
                .zipCode(cemeteryAddressDto.getZipCode())
                .build();
    }

    public static CemeteryManagerResponse toCemeteryManagerResponse(CemeteryManagerDto cemeteryCemeteryManagerDto) {
        if(cemeteryCemeteryManagerDto == null) {
            return null;
        }
        return CemeteryManagerResponse
                .builder()
                .email(cemeteryCemeteryManagerDto.getEmail())
                .firstName(cemeteryCemeteryManagerDto.getFirstName())
                .name(cemeteryCemeteryManagerDto.getName())
                .lastName(cemeteryCemeteryManagerDto.getLastName())
                .nip(cemeteryCemeteryManagerDto.getNip())
                .type(cemeteryCemeteryManagerDto.getType())
                .regon(cemeteryCemeteryManagerDto.getRegon())
                .representative(toCemeteryManagerResponse(cemeteryCemeteryManagerDto.getRepresentative()))
                .build();
    }


    public static CemeteryRepresentativeResponse toCemeteryManagerResponse(CemeteryRepresentativeDto cemeteryRepresentativeDto) {
        if(cemeteryRepresentativeDto == null) {
            return null;
        }
        return CemeteryRepresentativeResponse
                .builder()
                .firstName(cemeteryRepresentativeDto.getFirstName())
                .lastName(cemeteryRepresentativeDto.getLastName())
                .type(cemeteryRepresentativeDto.getType())
                .email(cemeteryRepresentativeDto.getEmail())
                .build();
    }

    public static CemeteryPerpetualUserResponse toApplicationCemeteryPerpetualUserResponse(CemeteryPerpetualUserDto cemeteryPerpetualUserDto) {
        if(cemeteryPerpetualUserDto == null) {
            return null;
        }
        return CemeteryPerpetualUserResponse
                        .builder()
                        .email(cemeteryPerpetualUserDto.getEmail())
                        .firstName(cemeteryPerpetualUserDto.getFirstName())
                        .lastName(cemeteryPerpetualUserDto.getLastName())
                        .name(cemeteryPerpetualUserDto.getName())
                        .nip(cemeteryPerpetualUserDto.getNip())
                        .religionId(cemeteryPerpetualUserDto.getReligionId())
                        .perpetualChurchRegulatedByLawId(cemeteryPerpetualUserDto.getPerpetualChurchRegulatedByLawId())
                        .perpetualChurchNotRegulatedByLawId(cemeteryPerpetualUserDto.getPerpetualChurchNotRegulatedByLawId())
                        .perpetualChurchesRegulatedByLawOrNo(cemeteryPerpetualUserDto.getPerpetualChurchesRegulatedByLawOrNo())
                        .nameOfParishPerpetualUse(cemeteryPerpetualUserDto.getNameOfParishPerpetualUse())
                        .type(cemeteryPerpetualUserDto.getType())
                        .regon(cemeteryPerpetualUserDto.getRegon())
                        .representative(toCemeteryRepresentativeResponse(cemeteryPerpetualUserDto.getRepresentative()))
                        .build();
    }

    public static CemeteryRepresentativeResponse toCemeteryRepresentativeResponse(CemeteryRepresentativeDto cemeteryRepresentativeDto) {
        if(cemeteryRepresentativeDto == null) {
            return null;
        }
        return CemeteryRepresentativeResponse
                        .builder()
                        .type(cemeteryRepresentativeDto.getType())
                        .email(cemeteryRepresentativeDto.getEmail())
                        .firstName(cemeteryRepresentativeDto.getFirstName())
                        .lastName(cemeteryRepresentativeDto.getLastName())
                        .nameRepresentationPersonObjectManagerLegalPerson(cemeteryRepresentativeDto.getNameRepresentationPersonObjectManagerLegalPerson())
                        .surnameRepresentationPersonObjectManagerLegalPerson(cemeteryRepresentativeDto.getSurnameRepresentationPersonObjectManagerLegalPerson())
                        .emailRepresentationPersonObjectManagerLegalPerson(cemeteryRepresentativeDto.getEmailRepresentationPersonObjectManagerLegalPerson())
                        .build();
    }

    public static CemeteryOwnerResponse toCemeteryOwnerResponse(CemeteryOwnerDto cemeteryOwnerDto) {
        if(cemeteryOwnerDto == null) {
            return null;
        }
        return CemeteryOwnerResponse
                .builder()
                .address(toCemeteryAddressResponse(cemeteryOwnerDto.getAddress()))
                .email(cemeteryOwnerDto.getEmail())
                .firstName(cemeteryOwnerDto.getFirstName())
                .lastName(cemeteryOwnerDto.getLastName())
                .representative(toCemeteryRepresentativeResponse(cemeteryOwnerDto.getRepresentative()))
                .name(cemeteryOwnerDto.getName())
                .nip(cemeteryOwnerDto.getNip())
                .regon(cemeteryOwnerDto.getRegon())
                .ownerCategoryId(cemeteryOwnerDto.getOwnerCategoryId())
                .territorialUnitType(cemeteryOwnerDto.getTerritorialUnitType())
                .build();
    }

    public static CemeteryUserAdminResponse toApplicationCemeteryUserAdminResponse(CemeteryUserAdminDto cemeteryCemeteryUserAdminDto) {
        if(cemeteryCemeteryUserAdminDto == null) {
            return null;
        }
        return CemeteryUserAdminResponse
                .builder()
                .email(cemeteryCemeteryUserAdminDto.getEmail())
                .firstName(cemeteryCemeteryUserAdminDto.getFirstName())
                .lastName(cemeteryCemeteryUserAdminDto.getLastName())
                .adminDataTheSameAsObjManagerOrPerpUserOrObjOwner(cemeteryCemeteryUserAdminDto.getAdminDataTheSameAsObjManagerOrPerpUserOrObjOwner())
                .build();
    }


}
