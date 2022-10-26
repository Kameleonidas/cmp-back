package pl.gov.cmp.scheduler.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "cmp")
public class JobConfiguration {
    private Map<JobEnum, JobItemConfiguration> jobs;
}
