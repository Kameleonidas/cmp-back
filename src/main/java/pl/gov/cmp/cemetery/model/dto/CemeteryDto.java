package pl.gov.cmp.cemetery.model.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import pl.gov.cmp.application.model.enums.TermType;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CemeteryDto {

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
    private CemeteryAddressDto locationAddress;
    private CemeteryAddressDto contactAddress;
    private String description;
    private boolean published;
    private CemeteryGeometryDto cemeteryGeometry;
    private CemeteryPerpetualUserDto cemeteryPerpetualUser;
    private CemeteryOwnerDto owner;
    private CemeteryManagerDto manager;
    private CemeteryUserAdminDto userAdmin;
    private List<CemeteryAttachmentDto> attachmentFiles;
}
