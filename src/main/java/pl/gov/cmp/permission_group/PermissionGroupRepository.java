package pl.gov.cmp.permission_group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PermissionGroupRepository extends JpaRepository<PermissionGroupEntity, Long> {

    @Query("from PermissionGroupEntity p left join p.institutionTypes")
    Set<PermissionGroupEntity> findAllWithInstitutionTypes();
}
