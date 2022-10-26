package pl.gov.cmp.cemetery.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryAddressSimpleResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryManagerSimpleResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryOwnerSimpleResponse;

@Builder
@Getter
@Setter
public class CemeteryResponseDto {

    private Long id;
    private String source;
    private String name;
    private String registrationNumber;
    private String status;
    private String description;
    private String facilityType;
    private CemeteryAddressSimpleResponse locationAddress;
    private String email;
    private String phoneNumber;
    private CemeteryAddressSimpleResponse contactAddress;
    private String openDate;
    private String closeDate;
    private String type;
    private String religion;
    private String otherReligion;
    private String ownerCategory;
    private CemeteryOwnerSimpleResponse owner;
    private CemeteryManagerSimpleResponse manager;

}