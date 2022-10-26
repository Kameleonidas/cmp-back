package pl.gov.cmp.cemetery.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CemeteryAddressDto {

    private String communeTercCode;
    private String districtTercCode;
    private String voivodeshipTercCode;
    private String placeSimcCode;
    private String voivodeship;
    private String district;
    private String commune;
    private String place;
    private String street;
    private String number;
    private String zipCode;
    private String postName;

}
