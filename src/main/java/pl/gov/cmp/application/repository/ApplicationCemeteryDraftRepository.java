package pl.gov.cmp.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryDraftEntity;

import java.util.List;

public interface ApplicationCemeteryDraftRepository extends JpaRepository<ApplicationCemeteryDraftEntity, Long> {

    List<ApplicationCemeteryDraftEntity> findByUserAccountId(Long userAccountId);

}
