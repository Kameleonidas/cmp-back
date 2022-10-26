package pl.gov.cmp.permission_group;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.service.UserAccountToSubjectService;

import java.util.List;
import java.util.Set;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;
import static pl.gov.cmp.permission_group.PermissionGroupMapper.*;

@Service
@RequiredArgsConstructor
public class InstitutionPermissionGroupQueryService {

    private final PermissionGroupQueryService permissionGroupService;
    private final UserAccountToSubjectService userAccountToSubjectService;

    public List<PermissionGroupResponse> findMissingPermissionGroupsInInstitution(long subjectId, ObjectCategoryEnum institutionType) {
        final var allPermissionGroupsForInstitutionType = permissionGroupService.findAllByInstitutionType(institutionType);
        final var allPermissionGroupIdsForInstitutionType = getAllPermissionGroupIdsForInstitutionType(allPermissionGroupsForInstitutionType);
        final var subject = userAccountToSubjectService.getByIdAndType(subjectId, institutionType);
        final var permissionGroupIdsForSubject = getPermissionGroupIdsForSubject(subject);
        final var missingPermissionGroupIds = Sets.difference(allPermissionGroupIdsForInstitutionType, permissionGroupIdsForSubject).immutableCopy();
        final var missingPermissionGroups = allPermissionGroupsForInstitutionType.stream().filter(pg -> missingPermissionGroupIds.contains(pg.getId())).collect(toSet());
        final var missingPermissionGroupResponses = PERMISSION_GROUP_MAPPER.toPermissionGroupResponses(missingPermissionGroups);
        return sortById(missingPermissionGroupResponses);
    }

    private List<PermissionGroupResponse> sortById(List<PermissionGroupResponse> missingPermissionGroupResponses) {
        return missingPermissionGroupResponses.stream().sorted(comparing(PermissionGroupResponse::getId)).collect(toList());
    }

    private Set<Long> getAllPermissionGroupIdsForInstitutionType(Set<PermissionGroupEntity> allPermissionGroupsForInstitutionType) {
        return allPermissionGroupsForInstitutionType.stream().map(PermissionGroupEntity::getId).collect(toSet());
    }

    private Set<Long> getPermissionGroupIdsForSubject(UserAccountToSubjectEntity subject) {
        return subject.getPermissionGroups().stream()
                .map(PermissionGroupEntity::getId).collect(toSet());
    }
}
