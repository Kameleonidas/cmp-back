package pl.gov.cmp.administration.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pl.gov.cmp.administration.model.enums.InstitutionType;

import java.util.Map;

@ToString
@Getter
@Setter
@ConfigurationProperties(prefix = "cmp.invitation")
public class InvitationConfiguration {

    private String sender;
    private String template;
    private Integer ttl;
    private Map<InstitutionType, String> confirmationUrls;
}
