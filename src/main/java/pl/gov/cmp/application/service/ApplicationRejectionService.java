package pl.gov.cmp.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.service.MessageSendService;
import pl.gov.cmp.administration.service.StatusChangeSendService;
import pl.gov.cmp.application.configuration.ApplicationRejectionConfiguration;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.repository.ApplicationRepository;
import pl.gov.cmp.history.model.dto.HistoryOperationDto;
import pl.gov.cmp.history.model.enums.HistoryOperationType;
import pl.gov.cmp.history.service.HistoryOperationService;
import pl.gov.cmp.utils.DateProvider;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ApplicationRejectionService {

    private static final String TO_BE_COMPLETED_TIME_EXCEEDED = "toBeCompletedTimeExceeded";
    private final ApplicationRepository applicationRepository;
    private final ApplicationRejectionConfiguration applicationRejectionConfiguration;
    private final HistoryOperationService historyOperationService;
    private final MessageSource messageSource;
    private final StatusChangeSendService statusChangeSendService;
    private final DateProvider dateProvider;

    public void rejectOldApplications() {
        var thresholdDate = dateProvider.getCurrentDate().minusDays(applicationRejectionConfiguration.getDaysToAutomaticRejection());
        var applicationEntityList = applicationRepository.findByAppStatusAndUpdateDateLessThan(ApplicationStatus.TO_BE_COMPLETED, thresholdDate);
        applicationEntityList.forEach(this::updateApplication);
    }

    private void updateApplication(ApplicationEntity applicationEntity) {
        applicationEntity.setAppStatus(ApplicationStatus.REJECTED);
        applicationEntity.setRejectionReasonDescription(messageSource.getMessage(TO_BE_COMPLETED_TIME_EXCEEDED, new Object[0], LocaleContextHolder.getLocale()));
        applicationRepository.save(applicationEntity);
        log.info("Application with appNumber=[{}] rejected.", applicationEntity.getAppNumber());
        statusChangeSendService.sendStatusChange(applicationEntity);
        saveLogEvent(applicationEntity);
    }

    private void saveLogEvent(ApplicationEntity applicationEntity) {
        var historyItem = HistoryOperationDto.builder().type(HistoryOperationType.APPLICATION_REJECTED)
                .applicationId(applicationEntity.getId())
                .build();
        log.info("Operation history prepared [{}]", historyItem);
        historyOperationService.saveHistoryOperation(historyItem);
        log.info("Operation history saved", historyItem);
    }
}
