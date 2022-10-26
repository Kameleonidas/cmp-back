package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TerritorialUnitType;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ApplicationCemeteryOwnerRequest {
    @Schema(description="The owner name")
    private String name;
    @Schema(description="The owner first name")
    private String firstName;
    @Schema(description="The owner last name")
    private String lastName;
    @Schema(description="The owner nip number")
    private String nip;
    @Schema(description="The owner regon number")
    private String regon;
    @Schema(description="The owner email number")
    private String email;
    @Schema(description="The owner territorial unit type",example = "VOIVODESHIP, DISTRICT, COMMUNITY, UNKNOWN")
    private TerritorialUnitType territorialUnitType;
    @Schema(description="The owner category id. Value from cemetery owner dictionary", minimum = "1", maximum = "12")
    private Long ownerCategoryId;
    @Schema(description="The owner sub category id. Value from cemetery owner dictionary", minimum = "1", maximum = "12")
    private Long ownerSubcategoryId;
    @Schema(description="The id of religion. Value from cemetery dictionary", minimum = "1", maximum = "4")
    private Long religionId;
    @Schema(description="Indicate whether applicant is owner", defaultValue = "false")
    private Boolean applicationIsOwner;
    @Schema(description="Owner company name", defaultValue = "false")
    private String ownerCompanyName;
    @Schema(description="Application applied by the community in substitute performance for the owner/perpetual user", defaultValue = "false")
    private String userInsteadOwnerOrPerpetualUser;
    @Schema(description="Name of the association of local government units", defaultValue = "false")
    private String nameAssociationLocalGovernment;
    @Schema(description="Email for institution object owner", defaultValue = "false")
    private String workEmailInstitutionObjectOwner;
    @Schema(description="Name of the organizational unit without legal personality", defaultValue = "false")
    private String unitWithoutLegalPersonalityName;
    @Schema(description="Id of church regulated by law. Value from churches regulated by law", defaultValue = "false")
    private Long churchNotRegulatedByLawId;
    @Schema(description="Id of church regulated by law. Value from churches regulated by law", defaultValue = "false")
    private Long churchRegulatedByLawId;
    @Schema(description="Name of parish", defaultValue = "false")
    private String nameOfParish;
    @Schema(description="Perpetual owner type id", defaultValue = "false")
    private Long perpetualOwnerTypeId;
    @Schema(description="The name of community name", defaultValue = "false")
    private ApplicationCommunityNameRequest communityName;
    @Schema(description="The name of owner community name", defaultValue = "false")
    private ApplicationOwnerCommunityNameRequest ownerCommunityName;
    @Schema(description="The name of owner community name", defaultValue = "false")
    private ApplicationNameLocalGovernmentUnitRequest nameLocalGovernmentUnit;
    @Schema(description="The owner address", defaultValue = "false")
    private ApplicationAddressRequest address;
    @Schema(description="Information about representative owner object", defaultValue = "false")
    private ApplicationCemeteryRepresentativeRequest representative;

}
