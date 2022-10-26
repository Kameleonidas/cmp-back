package pl.gov.cmp.application.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "cmp.application-rejection")
public class ApplicationRejectionConfiguration {
    @NotNull
    private Long daysToAutomaticRejection;
}
