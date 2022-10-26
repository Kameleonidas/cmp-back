package pl.gov.cmp.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;

public interface ApplicationCemeteryRepository extends JpaRepository<ApplicationCemeteryEntity, Long> {
}
