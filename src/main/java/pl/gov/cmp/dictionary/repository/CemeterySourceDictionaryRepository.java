package pl.gov.cmp.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.dictionary.model.entity.CemeterySourceDictionaryEntity;

public interface CemeterySourceDictionaryRepository extends JpaRepository<CemeterySourceDictionaryEntity, Long> {
}
