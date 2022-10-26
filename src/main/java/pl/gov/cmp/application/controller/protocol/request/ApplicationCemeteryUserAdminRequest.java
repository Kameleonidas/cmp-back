package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ApplicationCemeteryUserAdminRequest {
    @Schema(description="Administrator first name")
    // @NotBlank
    private String firstName;
    @Schema(description="Administrator last name")
    // @NotBlank
    private String lastName;
    @Schema(description="Administrator email")
    // @NotBlank
    private String email;
    @Schema(description="Indicates whether the admin is the same person as the manager, perpetual user or owner of the object ")
    private String adminDataTheSameAsObjManagerOrPerpUserOrObjOwner;
}
