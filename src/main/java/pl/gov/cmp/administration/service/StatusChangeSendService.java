package pl.gov.cmp.administration.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.configuration.StatusChangeConfiguration;
import pl.gov.cmp.administration.model.dto.EmailSenderDto;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class StatusChangeSendService {

    private static final String APPLICATION_STATUS = "application_status";
    private static final String APPLICATION_NUMBER = "application_number";
    private static final String APPLICATION_TYPE = "application_type";
    private static final String CEMETERY_NAME = "cemetery_name";
    private static final String APPLICATION_LINK = "application_link";
    private static final String ADDITIONAL_INFO = "additional_info";
    private static final String ADDITIONAL_INF0_MESSAGE = "additionalInfo";

    private final StatusChangeConfiguration statusChangeConfiguration;
    private final MessageSource messageSource;
    private final MessageSendService messageSendService;

    public boolean sendStatusChange(ApplicationEntity applicationEntity) {
        ApplicationStatus appStatus = applicationEntity.getAppStatus();
        String statusCode = String.join(".", "enum", appStatus.getClass().getSimpleName(), appStatus.name());
        String status = this.messageSource.getMessage(statusCode, Lists.newArrayList().toArray(String[]::new), LocaleContextHolder.getLocale());
        ApplicationType appType = applicationEntity.getAppType();
        String appTypeCode = String.join(".", "enum", appType.getClass().getSimpleName(), appType.name());
        String applicationType = this.messageSource.getMessage(appTypeCode, Lists.newArrayList().toArray(String[]::new), LocaleContextHolder.getLocale());
        MessageDto messageDto = MessageDto.builder()
                .emailSender(EmailSenderDto.builder()
                        .emailTo(applicationEntity.getApplication().getApplicant().getEmail())
                        .emailFrom(statusChangeConfiguration.getSender())
                        .build())
                .templateName(statusChangeConfiguration.getTemplate())
                .parameters(ImmutableMap.<String, String>builder()
                        .put(APPLICATION_STATUS, status)
                        .put(APPLICATION_NUMBER, applicationEntity.getAppNumber())
                        .put(APPLICATION_TYPE, applicationType)
                        .put(CEMETERY_NAME, applicationEntity.getApplication().getObjectName())
                        .put(APPLICATION_LINK, String.format(statusChangeConfiguration.getApplicationUrl(), applicationEntity.getAppNumber()))
                        .put(ADDITIONAL_INFO, getAdditionalInfo(applicationEntity))
                        .build())
                .build();
        return messageSendService.sendMessage(messageDto);
    }

    private String getAdditionalInfo(ApplicationEntity applicationEntity) {
        if (applicationEntity.getAppStatus() == ApplicationStatus.REJECTED) {
            return applicationEntity.getRejectionReasonDescription();
        }
        return this.messageSource.getMessage(ADDITIONAL_INF0_MESSAGE, Collections.emptyList().toArray(String[]::new), LocaleContextHolder.getLocale());
    }
}
