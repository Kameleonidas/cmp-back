package pl.gov.cmp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import pl.gov.cmp.administration.configuration.InvitationConfiguration;
import pl.gov.cmp.administration.configuration.PermissionChangeConfiguration;
import pl.gov.cmp.administration.configuration.StatusChangeConfiguration;
import pl.gov.cmp.administration.configuration.UserIdentifierConfiguration;
import pl.gov.cmp.application.configuration.ApplicationRejectionConfiguration;
import pl.gov.cmp.auth.configuration.LocalLoginUserConfiguration;
import pl.gov.cmp.auth.configuration.LoginUserConfiguration;
import pl.gov.cmp.file.configuration.FileConfiguration;
import pl.gov.cmp.gugik.configuration.GugikAddressServiceConfiguration;
import pl.gov.cmp.gugik.configuration.GugikTercServiceConfiguration;
import pl.gov.cmp.scheduler.configuration.JobConfiguration;

@SpringBootApplication
@EnableConfigurationProperties({
        GugikTercServiceConfiguration.class,
        GugikAddressServiceConfiguration.class,
        FileConfiguration.class,
        LoginUserConfiguration.class,
        LocalLoginUserConfiguration.class,
        InvitationConfiguration.class,
        StatusChangeConfiguration.class,
        UserIdentifierConfiguration.class,
        JobConfiguration.class,
        PermissionChangeConfiguration.class,
        ApplicationRejectionConfiguration.class
})
@EnableScheduling
public class CmpBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmpBackApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
