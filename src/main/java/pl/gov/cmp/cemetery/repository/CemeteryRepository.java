package pl.gov.cmp.cemetery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface CemeteryRepository extends JpaRepository<CemeteryEntity, Long>, JpaSpecificationExecutor<CemeteryEntity>, CemeteryCustomRepository {

    List<CemeteryEntity> findByLocationAddressIsNull();

    @Query(value = "select c from CemeteryEntity c where c.id IN (:cemeteryIds)")
    Page<CemeteryEntity> findByIds(@Param("cemeteryIds") Set<Long> cemeteryIds, Pageable pageable);

    @Modifying
    @Query(value = "update CemeteryEntity c set c.status = :status where c.id = :cemeteryId")
    void updateStatus(@Param("cemeteryId") Long cemeteryId, @Param("status") CemeteryStatus status);

    @Query(value = "SELECT nextval('cemetery_registration_number');", nativeQuery = true)
    Long getCemeteryRegistrationNumberFromSequence();

}
