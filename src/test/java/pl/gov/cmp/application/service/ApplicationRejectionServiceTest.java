package pl.gov.cmp.application.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import pl.gov.cmp.administration.service.StatusChangeSendService;
import pl.gov.cmp.application.configuration.ApplicationRejectionConfiguration;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.repository.ApplicationRepository;
import pl.gov.cmp.history.service.HistoryOperationService;
import pl.gov.cmp.utils.DateProvider;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationRejectionServiceTest {
    private static final String PRZEKROCZONY_CZAS_NA_UZUPELNIENIE_WNIOSKU = "Przekroczony czas na uzupełnienie wniosku";
    private ApplicationRejectionService applicationRejectionService;

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ApplicationRejectionConfiguration applicationRejectionConfiguration;
    @Mock
    private HistoryOperationService historyOperationService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private StatusChangeSendService statusChangeSendService;
    @Mock
    private DateProvider dateProvider;

    @BeforeEach
    void setUp() {
        applicationRejectionService = new ApplicationRejectionService(applicationRepository,
                applicationRejectionConfiguration, historyOperationService, messageSource,
                statusChangeSendService, dateProvider);
    }

    @Test
    void shouldRejectOldApplications() {
        //given
        given(dateProvider.getCurrentDate()).willReturn(LocalDate.of(2011,11,15));
        given(applicationRejectionConfiguration.getDaysToAutomaticRejection()).willReturn(14L);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        given(applicationRepository.findByAppStatusAndUpdateDateLessThan(ApplicationStatus.TO_BE_COMPLETED, LocalDate.of(2011,11,1)))
                .willReturn(Lists.newArrayList(applicationEntity));
        given(messageSource.getMessage(eq("toBeCompletedTimeExceeded"), any(), any()))
                .willReturn(PRZEKROCZONY_CZAS_NA_UZUPELNIENIE_WNIOSKU);

        //when
        applicationRejectionService.rejectOldApplications();

        //then
        assertThat(applicationEntity.getAppStatus()).isEqualTo(ApplicationStatus.REJECTED);
        assertThat(applicationEntity.getRejectionReasonDescription()).isEqualTo(PRZEKROCZONY_CZAS_NA_UZUPELNIENIE_WNIOSKU);
        verify(applicationRepository).save(applicationEntity);
        verify(statusChangeSendService).sendStatusChange(applicationEntity);
    }
}
