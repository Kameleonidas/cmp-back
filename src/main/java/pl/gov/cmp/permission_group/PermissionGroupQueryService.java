package pl.gov.cmp.permission_group;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
class PermissionGroupQueryService {

    private final PermissionGroupRepository permissionGroupRepository;

    Set<PermissionGroupEntity> findAllByInstitutionType(ObjectCategoryEnum institutionType) {
        return permissionGroupRepository.findAllWithInstitutionTypes().stream()
                .filter(permissionGroupEntity -> permissionGroupEntity.getInstitutionTypes().contains(institutionType))
                .collect(toSet());
    }
}
