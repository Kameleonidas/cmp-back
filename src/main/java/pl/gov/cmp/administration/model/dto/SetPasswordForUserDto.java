package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
public class SetPasswordForUserDto {

    @NotNull
    private String password;

    @NotNull
    private String wkId;

}
