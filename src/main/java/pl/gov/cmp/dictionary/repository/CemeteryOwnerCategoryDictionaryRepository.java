package pl.gov.cmp.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.dictionary.model.entity.CemeteryOwnerCategoryDictionaryEntity;

public interface CemeteryOwnerCategoryDictionaryRepository extends JpaRepository<CemeteryOwnerCategoryDictionaryEntity, Long> {
}
