package pl.gov.cmp.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pl.gov.cmp.process.model.entity.ProcessEntity;
import pl.gov.cmp.process.model.enums.ProcessCode;

import javax.persistence.LockModeType;

public interface ProcessRepository extends JpaRepository<ProcessEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ProcessEntity findByCode(ProcessCode code);
}
