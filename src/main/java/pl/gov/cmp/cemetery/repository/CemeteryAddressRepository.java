package pl.gov.cmp.cemetery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.cemetery.model.entity.CemeteryAddressEntity;

public interface CemeteryAddressRepository extends JpaRepository<CemeteryAddressEntity, Long> {
}
