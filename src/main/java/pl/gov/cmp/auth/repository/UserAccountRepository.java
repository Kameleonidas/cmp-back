package pl.gov.cmp.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;
import pl.gov.cmp.auth.model.enums.UserAccountStatusEnum;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long>, JpaSpecificationExecutor<UserAccountEntity> {

    Optional<UserAccountEntity> findByWkId(String wkId);

    Optional<UserAccountEntity> findByIdAndStatus(Long id, UserAccountStatusEnum status);

    @Query("from UserAccountEntity u left join fetch u.subjects where u.id = :id")
    Optional<UserAccountEntity> findByIdWithSubjects(@Param("id") long id);

    Optional<UserAccountEntity> findByLocalId(String userLocalId);

    boolean existsByLocalId(String personalIdentifier);
}
