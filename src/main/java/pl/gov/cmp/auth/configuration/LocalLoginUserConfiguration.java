package pl.gov.cmp.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "cmp.local-login")
public class LocalLoginUserConfiguration {

    @NotBlank
    private String wkUrl;

    @NotBlank
    private String wkWebUrl;

    @NotBlank
    private String returnBackUrl;

    @NotBlank
    private String returnFrontUrl;

}
