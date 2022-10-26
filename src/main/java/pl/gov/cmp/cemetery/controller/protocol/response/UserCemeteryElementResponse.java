package pl.gov.cmp.cemetery.controller.protocol.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCemeteryElementResponse {
    @Schema(description="The cemetery id")
    private Long id;
    @Schema(description="The cemetery name")
    private String name;
    @Schema(description="The cemetery type")
    private String type;
    @Schema(description="The cemetery creation date")
    private LocalDate createDate;
    private CemeteryAddressResponse locationAddress;
}
