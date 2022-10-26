package pl.gov.cmp.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.history.model.entity.HistoryOperationEntity;

public interface HistoryOperationRepository extends JpaRepository<HistoryOperationEntity, Long> {
}
