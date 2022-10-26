package pl.gov.cmp.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryApplicantEntity;
import pl.gov.cmp.application.model.entity.projection.ApplicationCemeteryApplicantProjection;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ApplicationCemeteryApplicantRepository extends JpaRepository<ApplicationCemeteryApplicantEntity, Long> {

    @Query(value = "select DISTINCT first_name as firstName, last_name as lastName from application_cemetery_applicants aca where aca.first_name ilike concat(:firstName, '%') and aca.last_name ilike concat(:lastName, '%');", nativeQuery = true)
    List<ApplicationCemeteryApplicantProjection> getApplicants(@NotNull String firstName, @NotNull String lastName);

}