package pl.gov.cmp.auth.model.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
public class AuthenticationCommand {

    @NotNull
    @Pattern(regexp = "^[0-9]{8}$")
    String userLocalId;

    @NotNull
    @Size(min = 8, max = 100)
    String password;
}
