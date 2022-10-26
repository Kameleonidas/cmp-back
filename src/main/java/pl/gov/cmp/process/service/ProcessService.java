package pl.gov.cmp.process.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.process.model.entity.ProcessEntity;
import pl.gov.cmp.process.model.enums.ProcessCode;
import pl.gov.cmp.process.model.enums.ProcessState;
import pl.gov.cmp.process.repository.ProcessRepository;

import java.time.LocalDateTime;

import static pl.gov.cmp.process.model.enums.ProcessState.IS_RUNNING;
import static pl.gov.cmp.process.model.enums.ProcessState.WAS_NOT_RUN;
import static pl.gov.cmp.process.model.enums.ProcessState.WAS_RUN;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class ProcessService {

    private final ProcessRepository processRepository;

    public void markProcessAsWasRun(ProcessCode code) {
        var systemProcess = processRepository.findByCode(code);
        systemProcess.setState(WAS_RUN);
    }

    public void markProcessAsWasNotRun(ProcessCode code) {
        var systemProcess = processRepository.findByCode(code);
        systemProcess.setState(WAS_NOT_RUN);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean changeProcessToIsRunningState(ProcessCode code) {
        var systemProcess = processRepository.findByCode(code);
        log.info("Got systemProcess entity [entity={}]", systemProcess);

        if (systemProcess == null) {
            systemProcess = createNewProcessEntity(code);
        }

        if (ProcessState.WAS_NOT_RUN.equals(systemProcess.getState())) {
            changeToIsRunning(systemProcess);
            return true;
        }
        return false;
    }

    private void changeToIsRunning(ProcessEntity process) {
        process.setRunDate(LocalDateTime.now());
        process.setState(IS_RUNNING);
    }

    private ProcessEntity createNewProcessEntity(ProcessCode code) {
        return processRepository.save(
                ProcessEntity.builder()
                        .code(code)
                        .state(WAS_NOT_RUN)
                        .description(code.getDescription())
                        .runDate(LocalDateTime.now())
                        .build());
    }
}
