package pl.gov.cmp.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "cmp.login")
public class LoginUserConfiguration {

    @NotBlank
    private String wkUrl;

    @NotBlank
    private String wkWebUrl;

    @NotBlank
    private String returnBackUrl;

    @NotBlank
    private String returnFrontUrl;

    @NotBlank
    private String jwtSecret;

    @NotNull
    private Integer jwtTokenValidity;

}
