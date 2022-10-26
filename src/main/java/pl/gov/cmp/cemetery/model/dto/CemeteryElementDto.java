package pl.gov.cmp.cemetery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.TermType;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CemeteryElementDto {

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
