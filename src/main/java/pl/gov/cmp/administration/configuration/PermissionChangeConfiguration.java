package pl.gov.cmp.administration.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Getter
@Setter
@ConfigurationProperties(prefix = "cmp.permission-change")
public class PermissionChangeConfiguration {
    private String sender;
    private String template;
}
