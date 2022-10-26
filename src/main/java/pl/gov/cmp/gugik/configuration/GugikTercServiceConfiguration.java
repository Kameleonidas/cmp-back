package pl.gov.cmp.gugik.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gugik.terc-service-conf")
public class GugikTercServiceConfiguration extends AbstractGugikAddressConfiguration {
}
