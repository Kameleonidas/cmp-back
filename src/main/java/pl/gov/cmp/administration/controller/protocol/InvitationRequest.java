package pl.gov.cmp.administration.controller.protocol;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.administration.model.enums.InstitutionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class InvitationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotNull
    private InstitutionType institutionType;

    @NotNull
    private Long institutionId;
}
