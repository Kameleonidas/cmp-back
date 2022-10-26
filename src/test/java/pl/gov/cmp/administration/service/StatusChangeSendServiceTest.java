package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import pl.gov.cmp.administration.configuration.StatusChangeConfiguration;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryApplicantEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatusChangeSendServiceTest {
    private StatusChangeSendService statusChangeSendService;

    @Mock
    private StatusChangeConfiguration statusChangeConfiguration;

    @Mock
    private MessageSource messageSource;

    @Mock
    private MessageSendService messageSendService;

    @Captor
    private ArgumentCaptor<MessageDto> messageDtoCaptor;

    @BeforeEach
    void setUp() {
        statusChangeSendService = new StatusChangeSendService(statusChangeConfiguration, messageSource, messageSendService);
    }

    @Test
    void shouldSendStatusChangeToRejected() {
        //given
        ApplicationEntity applicationEntity = prepareApplicationEntity(ApplicationStatus.REJECTED);
        given(messageSource.getMessage(eq("enum.ApplicationStatus.REJECTED"), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn("Odrzucony");
        given(messageSource.getMessage(eq("enum.ApplicationType.CEMETERY_REGISTRATION"), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn("Wniosek o rejestrację cmentarza");
        given(statusChangeConfiguration.getSender()).willReturn("no-replay@gov.pl");
        given(statusChangeConfiguration.getTemplate()).willReturn("test-template-name");
        given(statusChangeConfiguration.getApplicationUrl()).willReturn("https://test-domain/test-uri/%s");
        //when
        statusChangeSendService.sendStatusChange(applicationEntity);
        //then
        verify(messageSendService).sendMessage(messageDtoCaptor.capture());
        MessageDto captorValue = messageDtoCaptor.getValue();
        assertThat(captorValue.getEmailSender().getEmailFrom()).isEqualTo("no-replay@gov.pl");
        assertThat(captorValue.getEmailSender().getEmailTo()).isEqualTo("test@gov.pl");
        assertThat(captorValue.getTemplateName()).isEqualTo("test-template-name");
        assertThat(captorValue.getParameters())
                .contains(Map.entry("application_status", "Odrzucony"))
                .contains(Map.entry("application_number", "ERC00123"))
                .contains(Map.entry("application_type", "Wniosek o rejestrację cmentarza"))
                .contains(Map.entry("cemetery_name", "Test cemetery"))
                .contains(Map.entry("application_link", "https://test-domain/test-uri/ERC00123"))
                .contains(Map.entry("additional_info", "test-reason"));
    }

    @Test
    void shouldSendStatusChangeToAccepted() {
        //given
        ApplicationEntity applicationEntity = prepareApplicationEntity(ApplicationStatus.ACCEPTED);
        given(messageSource.getMessage(eq("enum.ApplicationStatus.ACCEPTED"), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn("Zaakceptowany");
        given(messageSource.getMessage(eq("enum.ApplicationType.CEMETERY_REGISTRATION"), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn("Wniosek o rejestrację cmentarza");
        given(this.messageSource.getMessage(eq("additionalInfo"), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn("Brak");
        given(statusChangeConfiguration.getSender()).willReturn("no-replay@gov.pl");
        given(statusChangeConfiguration.getTemplate()).willReturn("test-template-name");
        given(statusChangeConfiguration.getApplicationUrl()).willReturn("https://test-domain/test-uri/%s");
        //when
        statusChangeSendService.sendStatusChange(applicationEntity);
        //then
        verify(messageSendService).sendMessage(messageDtoCaptor.capture());
        MessageDto captorValue = messageDtoCaptor.getValue();
        assertThat(captorValue.getParameters())
                .contains(Map.entry("additional_info", "Brak"));

    }
    private ApplicationEntity prepareApplicationEntity(ApplicationStatus status) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        ApplicationCemeteryEntity application = new ApplicationCemeteryEntity();
        ApplicationCemeteryApplicantEntity applicant = new ApplicationCemeteryApplicantEntity();
        applicant.setEmail("test@gov.pl");
        application.setApplicant(applicant);
        application.setObjectName("Test cemetery");
        applicationEntity.setApplication(application);
        applicationEntity.setAppStatus(status);
        applicationEntity.setAppNumber("ERC00123");
        applicationEntity.setAppType(ApplicationType.CEMETERY_REGISTRATION);
        applicationEntity.setRejectionReasonDescription("test-reason");
        return applicationEntity;
    }
}
