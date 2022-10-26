package pl.gov.cmp.administration.model.dto;

import lombok.Value;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Value
public class InstitutionDataCommand {

    @NotNull
    @Email
    String institutionEmail;
    String institutionPhoneNumber;
}
