package pl.gov.cmp.process.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import pl.gov.cmp.process.model.entity.ProcessEntity;
import pl.gov.cmp.process.model.enums.ProcessCode;
import pl.gov.cmp.process.model.enums.ProcessState;
import pl.gov.cmp.process.repository.ProcessRepository;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static pl.gov.cmp.process.model.enums.ProcessCode.CEMETERIES_ADDRESSES_FILLER;
import static pl.gov.cmp.process.model.enums.ProcessState.*;

@ExtendWith(MockitoExtension.class)
class ProcessServiceTest {

    private static Stream<Arguments> runnableProcessEntity() {
        return Stream.of(Arguments.of(ProcessEntity.builder().state(ProcessState.WAS_NOT_RUN).build()));
    }

    private static Stream<Arguments> notRunnableProcessEntity() {
        return Stream.of(Arguments.of(ProcessEntity.builder().state(IS_RUNNING).build()), Arguments.of(ProcessEntity.builder().state(WAS_RUN).build()));
    }

    @Mock
    private ProcessRepository processRepositoryMock;

    private ProcessService processService;

    @BeforeEach
    void setUp() {
        processService = new ProcessService(processRepositoryMock);
    }

    @Test
    void shouldMarkProcessAsWasRun() {
        // given
        ProcessEntity processEntity = ProcessEntity.builder().build();
        given(processRepositoryMock.findByCode(any(ProcessCode.class))).willReturn(processEntity);

        // when
        processService.markProcessAsWasRun(CEMETERIES_ADDRESSES_FILLER);

        // then
        assertEquals(WAS_RUN, processEntity.getState());
    }

    @Test
    void shouldMarkProcessAsWasNotRun() {
        // given
        final var processEntity = ProcessEntity.builder().build();
        given(processRepositoryMock.findByCode(any(ProcessCode.class))).willReturn(processEntity);

        // when
        processService.markProcessAsWasNotRun(CEMETERIES_ADDRESSES_FILLER);

        // then
        assertEquals(WAS_NOT_RUN, processEntity.getState());
    }

    @Test
    void shouldMarkProcessAsIsRunning() {
        // given
        ProcessEntity processEntity = ProcessEntity.builder().state(ProcessState.WAS_NOT_RUN).build();
        given(processRepositoryMock.findByCode(any(ProcessCode.class))).willReturn(processEntity);

        // when
        boolean result = processService.changeProcessToIsRunningState(CEMETERIES_ADDRESSES_FILLER);

        // then
        assertTrue(result);
        assertEquals(IS_RUNNING, processEntity.getState());
    }

    @Test
    void shouldCreateProcessAsIsRunning() {
        // given
        given(processRepositoryMock.findByCode(any(ProcessCode.class))).willReturn(null);
        given(processRepositoryMock.save(any(ProcessEntity.class))).willAnswer(prepareAnswerForSave());

        // when
        boolean result = processService.changeProcessToIsRunningState(CEMETERIES_ADDRESSES_FILLER);

        // then
        assertTrue(result);
    }

    private Answer<ProcessEntity> prepareAnswerForSave() {
        return invocation -> {
            ProcessEntity entityToSave = invocation.getArgument(0);
            entityToSave.setId(UUID.randomUUID().getLeastSignificantBits());
            return entityToSave;
        };
    }

    @ParameterizedTest
    @MethodSource("notRunnableProcessEntity")
    void shouldNotMarkProcessAsIsRunning(ProcessEntity processEntity) {
        // given
        given(processRepositoryMock.findByCode(any(ProcessCode.class))).willReturn(processEntity);

        // when
        boolean result = processService.changeProcessToIsRunningState(CEMETERIES_ADDRESSES_FILLER);

        // then
        assertFalse(result);
        then(processRepositoryMock).should().findByCode(CEMETERIES_ADDRESSES_FILLER);
        then(processRepositoryMock).shouldHaveNoMoreInteractions();
    }

}