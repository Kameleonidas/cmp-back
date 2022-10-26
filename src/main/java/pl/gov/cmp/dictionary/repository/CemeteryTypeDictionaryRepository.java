package pl.gov.cmp.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.dictionary.model.entity.CemeteryTypeDictionaryEntity;

public interface CemeteryTypeDictionaryRepository extends JpaRepository<CemeteryTypeDictionaryEntity, Long> {
}
