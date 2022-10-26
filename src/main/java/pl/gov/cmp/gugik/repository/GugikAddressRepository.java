package pl.gov.cmp.gugik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.gugik.model.entity.GugikAddressEntity;

public interface GugikAddressRepository extends JpaRepository<GugikAddressEntity, Long> {
}
