package pl.gov.cmp.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.dictionary.model.entity.UserAccountStatusDictionaryEntity;

public interface UserAccountStatusDictionaryRepository extends JpaRepository<UserAccountStatusDictionaryEntity, Long> {
}
