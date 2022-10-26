package pl.gov.cmp.gugik.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gugik.address-service-conf")
public class GugikAddressServiceConfiguration extends AbstractGugikAddressConfiguration {
}
