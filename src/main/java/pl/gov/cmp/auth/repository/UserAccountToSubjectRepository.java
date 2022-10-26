package pl.gov.cmp.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;

import java.util.Optional;

public interface UserAccountToSubjectRepository extends JpaRepository<UserAccountToSubjectEntity, Long> {

    Optional<UserAccountToSubjectEntity> findByIdAndCategory(long id, ObjectCategoryEnum category);
}
