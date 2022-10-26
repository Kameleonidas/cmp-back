package pl.gov.cmp.cemetery.controller.protocol.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.gov.cmp.application.model.enums.TerritorialUnitType;
import pl.gov.cmp.cemetery.model.dto.CommunityNameDto;
import pl.gov.cmp.cemetery.model.dto.NameLocalGovernmentUnitDto;
import pl.gov.cmp.cemetery.model.dto.OwnerCommunityNameDto;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CemeteryOwnerResponse {

    private Long id;
    private String name;
    @JsonIgnore
    private String firstName;
    @JsonIgnore
    private String lastName;
    private String nip;
    private String regon;
    @JsonIgnore
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
    private CemeteryAddressResponse address;
    private CemeteryRepresentativeResponse representative;

}
