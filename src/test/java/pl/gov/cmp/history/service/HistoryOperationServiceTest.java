package pl.gov.cmp.history.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import pl.gov.cmp.history.model.dto.HistoryOperationDto;
import pl.gov.cmp.history.model.enums.HistoryOperationType;
import pl.gov.cmp.history.model.maper.HistoryOperationMapperImpl;
import pl.gov.cmp.history.repository.HistoryOperationRepository;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryOperationServiceTest {

    private static final long CEMETERY_ID = 11L;
    private static final String CEMETERY_REGISTRATION_NUMBER = "CMP001";
    private static final String HISTORY_OPERATION_MESSAGE = "Zarejestrowano cmentarz o numerze ewidencyjnym CMP001";

    @Mock
    private HistoryOperationRepository historyOperationRepository;

    @Mock
    private MessageSource messageSource;

    private HistoryOperationService historyOperationService;

    @BeforeEach
    void setUp() {
        historyOperationService = new HistoryOperationService(historyOperationRepository, new HistoryOperationMapperImpl(), messageSource);
    }

    @Test
    void shouldSaveHistoryOperation() {
        // given
        HistoryOperationDto historyOperation = prepareHistoryOperation();
        when(this.messageSource.getMessage(any(), any(), any())).thenReturn(HISTORY_OPERATION_MESSAGE);

        // when
        this.historyOperationService.saveHistoryOperation(historyOperation);

        // then
        verify(this.historyOperationRepository, times(1)).save(argThat(o -> {
            assertThat(o).isNotNull();
            assertEquals(HistoryOperationType.CEMETERY_REGISTERED, o.getType());
            assertEquals(CEMETERY_ID, o.getCemeteryId());
            assertEquals(HISTORY_OPERATION_MESSAGE, o.getDescription());
            return true;
        }));
        verify(messageSource).getMessage("enum.HistoryOperationType.CEMETERY_REGISTERED",
                new String[]{CEMETERY_REGISTRATION_NUMBER}, Locale.getDefault());
    }

    private HistoryOperationDto prepareHistoryOperation() {
        return HistoryOperationDto.builder()
                .type(HistoryOperationType.CEMETERY_REGISTERED)
                .params(List.of(CEMETERY_REGISTRATION_NUMBER))
                .cemeteryId(CEMETERY_ID)
                .build();
    }
}
