package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ApplicationCemeteryRequest {

    @Schema(description="The cemetery status")
    private CemeteryStatus cemeteryStatus;
    @Schema(description="The name of the object")
    @NotBlank
    private String objectName;
    @Schema(description="The description of the object")
    // @NotBlank
    private String objectDescription;
    @Schema(description="The email for contact")
    @NotBlank
    private String contactEmail;
    @Schema(description="The phone number for the contact")
    // @NotBlank
    private String contactPhoneNumber;
    @Schema(description="The date format, associated with open date" , example = "YEAR, MONTH, DATE, YEARS_RANGE" )
    @NotNull
    private TermType openTermType;
    @Schema(description="The date format, associated with close date", example = "YEAR, MONTH, DATE, YEARS_RANGE" )
    @NotNull
    private TermType closeTermType;
    @Schema(description="The date of opening the object")
    private String openDate;
    @Schema(description="The date of closing the object")
    private String closeDate;
    private String openTerm;
    private String closeTerm;
    private String otherType;
    @Schema(description="The id of religion, value from religion dictionary", minimum = "1", maximum = "189")
    private String otherReligion;
    @Schema(description="Name of the fields indicated by the operator which requires correction", defaultValue = "false")
    private String fieldsToBeCompleted;
    private boolean substitutePerformance;
    private boolean perpetualUse;
    @Schema(description="Indicate whether the church is regulated by law", defaultValue = "false")
    private boolean churchPerpetualUser;
    @Schema(description="Indicate whether the owner is a church or religious union", defaultValue = "false")
    private boolean churchOwner;
    @Schema(description="Indicate whether the church is regulated by law", defaultValue = "false")
    private boolean churchRegulatedByLaw;
    @Schema(description="Indicate whether manager of object exists", defaultValue = "false")
    private boolean managerExists;
    @Schema(description="Indicate whether user admin exists", defaultValue = "false")
    private boolean userAdminExists;
    @Schema(description="Cemetery type id, value from cemetery dictionary", defaultValue = "false", minimum = "1", maximum = "4")
    private Long cemeteryTypeId;
    private DictionaryCemeteryFacilityTypeRequest facilityType;
    @Schema(description="The id of religion. Value from cemetery dictionary", minimum = "1", maximum = "4")
    private Long religionId;
    @Schema(description="The id of bound cemetery")
    private Long boundCemeteryId;
    @Schema(description="The location associated with the object")
    private ApplicationAddressRequest locationAddress;
    @Schema(description="The location associated with the object")
    private ApplicationAddressRequest contactAddress;
    @Schema(description="The information about applicant")
    private ApplicationCemeteryApplicantRequest applicant;
    @Schema(description="The information about cemetery owner")
    private ApplicationCemeteryPerpetualUserRequest perpetualUser;
    @Schema(description="The information about cemetery owner")
    private ApplicationCemeteryOwnerRequest owner;
    @Schema(description="The information about cemetery manager")
    private ApplicationCemeteryManagerRequest manager;
    @Schema(description="The information about user administrator")
    @Valid
    private ApplicationCemeteryUserAdminRequest userAdmin;
    @Schema(description="The files attached to the application")
    private List<FileAttachmentRequest> applicationAttachmentFiles;
    @Schema(description="The files attached to the cemetery")
    @Valid
    private List<FileAttachmentRequest> cemeteryAttachmentFiles;

}
