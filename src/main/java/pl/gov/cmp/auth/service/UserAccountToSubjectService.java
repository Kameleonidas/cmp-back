package pl.gov.cmp.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.cmp.auth.exception.UserAccountToSubjectNotFoundException;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.repository.UserAccountToSubjectRepository;

@Service
@RequiredArgsConstructor
public class UserAccountToSubjectService {

    private final UserAccountToSubjectRepository repository;

    public UserAccountToSubjectEntity getByIdAndType(long id, ObjectCategoryEnum institutionType) {
        return repository.findByIdAndCategory(id, institutionType)
                .orElseThrow(() -> new UserAccountToSubjectNotFoundException(id, institutionType));
    }
}
