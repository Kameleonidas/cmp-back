package pl.gov.cmp.application.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;
import pl.gov.cmp.dictionary.model.dto.DictionaryCemeteryFacilityTypeDto;

import java.util.List;

@Builder
@Getter
@Setter
public class ApplicationCemeteryDto {

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
    private String rejectionReasonDescription;
    private Long cemeteryTypeId;
    private DictionaryCemeteryFacilityTypeDto facilityType;
    private Long religionId;
    private Long boundCemeteryId;

    private ApplicationAddressDto locationAddress;
    private ApplicationAddressDto contactAddress;

    private ApplicationDetailsDto application;
    private ApplicationCemeteryApplicantDto applicant;
    private ApplicationCemeteryPerpetualUserDto perpetualUser;
    private ApplicationCemeteryOwnerDto owner;
    private ApplicationCemeteryManagerDto manager;
    private ApplicationCemeteryUserAdminDto userAdmin;
    private List<ApplicationFileAttachmentDto> applicationAttachmentFiles;
    private List<CemeteryAttachmentDto> cemeteryAttachmentFiles;
}
