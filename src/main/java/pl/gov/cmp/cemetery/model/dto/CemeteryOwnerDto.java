package pl.gov.cmp.cemetery.model.dto;

import lombok.*;
import pl.gov.cmp.application.model.enums.TerritorialUnitType;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CemeteryOwnerDto {
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
    private CommunityNameDto communityName;
    private OwnerCommunityNameDto ownerCommunityName;
    private NameLocalGovernmentUnitDto nameLocalGovernmentUnit;
    private CemeteryAddressDto address;
    private CemeteryRepresentativeDto representative;
}
