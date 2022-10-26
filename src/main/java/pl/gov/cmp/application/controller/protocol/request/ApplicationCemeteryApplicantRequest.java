package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ApplicationCemeteryApplicantRequest {

    @Schema(description="The user id")
    private Long userId;
    @Schema(description="The applicant fist name", accessMode = Schema.AccessMode.READ_ONLY)
    private String firstName;
    @Schema(description="The applicant last name", accessMode = Schema.AccessMode.READ_ONLY)
    private String lastName;
    @Schema(description="The applicant email")
    private String email;
    @Schema(description="The applicant phone number")
    private String phoneNumber;

}
