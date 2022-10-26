package pl.gov.cmp.application.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TerritorialUnitType;

@Builder
@Getter
@Setter
public class ApplicationCemeteryOwnerDto {

    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String nip;
    private String regon;
    private String email;
    private TerritorialUnitType territorialUnitType;
    private Long ownerCategoryId;
    private Long ownerSubcategoryId;
    private Long religionId;
    private Boolean applicationIsOwner;
    private String ownerCompanyName;
    private String userInsteadOwnerOrPerpetualUser;
    private String nameAssociationLocalGovernment;
    private String workEmailInstitutionObjectOwner;
    private String unitWithoutLegalPersonalityName;
    private Long churchNotRegulatedByLawId;
    private Long churchRegulatedByLawId;
    private String nameOfParish;
    private Long perpetualOwnerTypeId;
    private ApplicationCommunityNameDto communityName;
    private ApplicationOwnerCommunityNameDto ownerCommunityName;
    private ApplicationNameLocalGovernmentUnitDto nameLocalGovernmentUnit;
    private ApplicationAddressDto address;
    private ApplicationCemeteryRepresentativeDto representative;

}
