package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TermType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CemeteryElementResponse {

    private Long id;
    private String name;
    private String facilityType;
    private String type;
    private String status;
    private String source;
    private String religion;
    private String ownerCategory;
    private String voivodeship;
    private String district;
    private String commune;
    private String place;
    private TermType openTermType;
    private TermType closeTermType;
    private LocalDate createDate;
    private String openDate;
    private String closeDate;
    private Boolean substitutePerformance;
    private Boolean perpetualUse;
    private Boolean churchPerpetualUser;
    private Boolean churchOwner;
    private Boolean churchRegulatedByLaw;
    private Boolean managerExists;
    private Boolean userAdminExists;
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
    private CemeteryUserAdminResponse userAdmin;
    private List<CemeteryAttachmentResponse> attachmentFiles;

}
