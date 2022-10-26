package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationAddressRequest {

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
