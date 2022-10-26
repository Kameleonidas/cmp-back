package pl.gov.cmp.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;

import java.time.LocalDate;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    ApplicationEntity findByIdAndAppStatus(Long applicationId, ApplicationStatus appStatus);

    @Query("SELECT application FROM ApplicationEntity application WHERE application.id = :id AND application.appStatus IN :statuses")
    ApplicationEntity findByIdAndAppStatuses(Long id, List<ApplicationStatus> statuses);
    List<ApplicationEntity> findByAppStatusAndUpdateDateLessThan(ApplicationStatus appStatus, LocalDate updateDate);

    @Query(value = "SELECT nextval('application_number');", nativeQuery = true)
    Long getApplicationNumberFromSequence();

}
