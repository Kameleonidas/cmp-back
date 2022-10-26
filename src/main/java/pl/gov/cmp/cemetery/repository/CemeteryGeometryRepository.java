package pl.gov.cmp.cemetery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.cemetery.model.entity.CemeteryGeometryEntity;

import java.util.Optional;

public interface CemeteryGeometryRepository extends JpaRepository<CemeteryGeometryEntity, Long> {

    Optional<CemeteryGeometryEntity> findByIdIipIdentifier(String idIipIdentifier);
}
