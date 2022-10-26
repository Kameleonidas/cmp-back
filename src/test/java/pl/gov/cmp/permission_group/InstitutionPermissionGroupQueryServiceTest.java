package pl.gov.cmp.permission_group;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.service.UserAccountToSubjectService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static pl.gov.cmp.auth.model.enums.ObjectCategoryEnum.CEMETERY;

class InstitutionPermissionGroupQueryServiceTest {

    private final PermissionGroupQueryService permissionGroupService = mock(PermissionGroupQueryService.class);
    private final UserAccountToSubjectService userAccountToSubjectService = mock(UserAccountToSubjectService.class);

    private final InstitutionPermissionGroupQueryService institutionPermissionGroupQueryService = new InstitutionPermissionGroupQueryService(permissionGroupService, userAccountToSubjectService);

    @AfterEach
    void cleanup() {
        reset(permissionGroupService, userAccountToSubjectService);
    }

    @Test
    void shouldReturnMissingPermissionGroupsForInstitution() {
        //given
        final var subjectId = 4467;
        final var commonPermissionGroupId = 35654L;
        final var notCommonPermissionGroupId = 546L;
        final var institutionType = CEMETERY;
        final var firstPermissionGroup = createPermissionGroup(commonPermissionGroupId, institutionType);
        final var secondPermissionGroup = createPermissionGroup(notCommonPermissionGroupId, institutionType);
        given(permissionGroupService.findAllByInstitutionType(institutionType)).willReturn(Set.of(firstPermissionGroup, secondPermissionGroup));
        final var userAccountToSubject = createUserAccountToSubjectEntity(commonPermissionGroupId, subjectId, institutionType);
        given(userAccountToSubjectService.getByIdAndType(subjectId, institutionType)).willReturn(userAccountToSubject);

        //when
        final var result = institutionPermissionGroupQueryService.findMissingPermissionGroupsInInstitution(subjectId, institutionType);

        //then
        assertThat(result.size()).isOne();
        assertThat(result.get(0).getId()).isEqualTo(notCommonPermissionGroupId);

    }

    private PermissionGroupEntity createPermissionGroup(long id, ObjectCategoryEnum institutionType) {
        final var permissionGroup = mock(PermissionGroupEntity.class);
        given(permissionGroup.getId()).willReturn(id);
        given(permissionGroup.getInstitutionTypes()).willReturn(Set.of(institutionType));
        return permissionGroup;
    }

    private UserAccountToSubjectEntity createUserAccountToSubjectEntity(long permissionGroupId, long subjectId, ObjectCategoryEnum institutionType) {
        final var permissionGroup = createPermissionGroup(permissionGroupId, institutionType);
        return UserAccountToSubjectEntity.builder()
                .id(subjectId)
                .permissionGroups(Set.of(permissionGroup))
                .category(institutionType).build();    }
}