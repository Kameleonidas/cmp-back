package pl.gov.cmp.administration.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Getter
@Setter
@ConfigurationProperties(prefix = "cmp.status-change")
public class StatusChangeConfiguration {
    private String sender;
    private String template;
    private String applicationUrl;
}
