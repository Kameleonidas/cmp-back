package pl.gov.cmp.cemetery.controller.protocol.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TermType;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class CemeteryResponse {

    private Long id;
    private String name;
    private String facilityType;
    private Long cemeteryTypeId;
    private String type;
    private String otherType;
    private String status;
    private String source;
    private String sourceCode;
    private String religion;
    private String otherReligion;
    private String registrationNumber;
    private TermType openTermType;
    private TermType closeTermType;
    private LocalDate createDate;
    private String openDate;
    private String closeDate;
    private boolean substitutePerformance;
    private boolean perpetualUse;
    private boolean churchPerpetualUser;
    private boolean churchOwner;
    private boolean churchRegulatedByLaw;
    private boolean managerExists;
    private boolean userAdminExists;
    private LocalDate liquidationDate;
    private LocalDate plannedLiquidationDate;
    private String email;
    private String phoneNumber;
    private CemeteryAddressResponse locationAddress;
    private CemeteryAddressResponse contactAddress;
    private String description;
    private boolean published;
    private CemeteryGeometryResponse cemeteryGeometry;
    private CemeteryPerpetualUserResponse cemeteryPerpetualUser;
    private CemeteryOwnerResponse owner;
    private CemeteryManagerResponse manager;
    @JsonIgnore
    private CemeteryUserAdminResponse userAdmin;
    @JsonIgnore
    private List<CemeteryAttachmentResponse> attachmentFiles;

}
