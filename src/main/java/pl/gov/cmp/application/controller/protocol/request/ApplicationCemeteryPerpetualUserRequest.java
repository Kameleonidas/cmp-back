package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.PerpetualUseType;

@Getter
@Setter
public class ApplicationCemeteryPerpetualUserRequest {

    private PerpetualUseType type;
    @Schema(description="Perpetual user name", defaultValue = "false")
    private String name;
    @Schema(description="Perpetual user first name", defaultValue = "false")
    private String firstName;
    @Schema(description="Perpetual user last name", defaultValue = "false")
    private String lastName;
    @Schema(description="Perpetual user nip", defaultValue = "false")
    private String nip;
    @Schema(description="Perpetual user regon", defaultValue = "false")
    private String regon;
    @Schema(description="Perpetual user email", defaultValue = "false")
    private String email;
    @Schema(description="Perpetual user religion id", defaultValue = "false")
    private Long religionId;
    @Schema(description="Perpetual user religion id", defaultValue = "false")
    private Long perpetualChurchRegulatedByLawId;
    private Long perpetualChurchNotRegulatedByLawId;
    private String perpetualChurchesRegulatedByLawOrNo;
    private String nameOfParishPerpetualUse;
    private ApplicationCemeteryRepresentativeRequest representative;

}
