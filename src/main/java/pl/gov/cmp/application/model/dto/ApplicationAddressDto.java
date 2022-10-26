package pl.gov.cmp.application.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApplicationAddressDto {

    private Long id;
    private String voivodeshipTercCode;
    private String districtTercCode;
    private String communeTercCode;
    private String placeSimcCode;
    private String voivodeship;
    private String district;
    private String commune;
    private String place;
    private String street;
    private String number;
    private String zipCode;
    private String postName;
    private String streetCode;
}
