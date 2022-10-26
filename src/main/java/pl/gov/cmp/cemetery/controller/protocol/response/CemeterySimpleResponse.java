package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CemeterySimpleResponse {

    private Long id;
    private String source;
    private String name;
    private String registrationNumber;
    private String status;
    private String description;
    private String facilityType;
    private String ownerCategory;

    private CemeteryAddressSimpleResponse locationAddress;

    private String email;
    private String phoneNumber;
    private CemeteryAddressSimpleResponse contactAddress;

    private String openDate;
    private String closeDate;
    private String type;
    private String religion;
    private String otherReligion;

    private CemeteryOwnerSimpleResponse owner;
    private CemeteryManagerSimpleResponse manager;

}