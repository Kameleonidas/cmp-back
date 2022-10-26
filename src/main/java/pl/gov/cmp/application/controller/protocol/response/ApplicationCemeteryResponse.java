package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;
import pl.gov.cmp.dictionary.model.dto.DictionaryCemeteryFacilityTypeDto;

import java.util.List;

@Getter
@Setter
public class ApplicationCemeteryResponse {

    private Long id;
    private CemeteryStatus cemeteryStatus;
    private String objectName;
    private String objectDescription;
    private String contactEmail;
    private String contactPhoneNumber;
    private TermType openTermType;
    private TermType closeTermType;
    private String openDate;
    private String closeDate;
    private String openTerm;
    private String closeTerm;
    private String otherType;
    private String otherReligion;
    private String fieldsToBeCompleted;

    private boolean substitutePerformance;
    private boolean perpetualUse;
    private boolean churchPerpetualUser;
    private boolean churchOwner;
    private boolean churchRegulatedByLaw;
    private boolean managerExists;
    private boolean userAdminExists;

    private Long cemeteryTypeId;
    private DictionaryCemeteryFacilityTypeDto facilityType;
    private Long religionId;
    private Long boundCemeteryId;
    private String rejectionReasonDescription;
    private ApplicationAddressResponse locationAddress;
    private ApplicationAddressResponse contactAddress;

    private ApplicationResponse application;
    private ApplicationCemeteryApplicantResponse applicant;
    private ApplicationCemeteryPerpetualUserResponse perpetualUser;
    private ApplicationCemeteryOwnerResponse owner;
    private ApplicationCemeteryManagerResponse manager;
    private ApplicationCemeteryUserAdminResponse userAdmin;
    private List<ApplicationFileAttachmentResponse> applicationAttachmentFiles;

}
