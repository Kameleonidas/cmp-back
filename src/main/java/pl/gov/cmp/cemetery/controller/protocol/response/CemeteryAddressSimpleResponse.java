package pl.gov.cmp.cemetery.controller.protocol.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CemeteryAddressSimpleResponse {
    @Schema(description="The cemetery voivodeship")
    private String voivodeship;
    @Schema(description="The cemetery district")
    private String district;
    @Schema(description="The cemetery commune")
    private String commune;
    @Schema(description="The cemetery place")
    private String place;
    @Schema(description="The cemetery street")
    private String street;
    @Schema(description="The cemetery home number")
    private String number;
    @Schema(description="The cemetery zip code")
    private String zipCode;
    @Schema(description="The cemetery post name")
    private String postName;
}