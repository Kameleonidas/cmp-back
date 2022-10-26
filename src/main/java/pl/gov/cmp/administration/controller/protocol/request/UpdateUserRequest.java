package pl.gov.cmp.administration.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateUserRequest {
    @Schema(description="The user account id in institution")
    @NotNull
    private Long subjectId;
    @Schema(description="The institution type")
    @NotNull
    private ObjectCategoryEnum institutionType;
    @Schema(description="The email in institution")
    @NotBlank
    @Email
    private String email;
    @Schema(description="The phone number in institution")
    @NotBlank
    private String phoneNumber;
}