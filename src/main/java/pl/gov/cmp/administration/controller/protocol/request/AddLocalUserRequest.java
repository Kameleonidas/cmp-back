package pl.gov.cmp.administration.controller.protocol.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddLocalUserRequest {

    @NotNull
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[~#?!@$%^&*-]).{8,100}$")
    private String password;
}
