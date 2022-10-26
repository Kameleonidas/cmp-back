package pl.gov.cmp.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.dictionary.model.entity.ChurchReligionDictionaryEntity;

public interface ChurchReligionDictionaryRepository extends JpaRepository<ChurchReligionDictionaryEntity, Long> {
}
