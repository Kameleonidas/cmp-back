package pl.gov.cmp.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.dictionary.model.entity.CemeteryFacilityTypeDictionaryEntity;

import java.util.Optional;

public interface CemeteryFacilityTypeDictionaryRepository extends JpaRepository<CemeteryFacilityTypeDictionaryEntity, Long> {

    Optional<CemeteryFacilityTypeDictionaryEntity> findByCode(String code);
}
