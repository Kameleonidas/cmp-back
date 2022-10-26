package pl.gov.cmp.auth.controller.protocol;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserTokenRequest {

    @NotNull
    private String authToken;

}
