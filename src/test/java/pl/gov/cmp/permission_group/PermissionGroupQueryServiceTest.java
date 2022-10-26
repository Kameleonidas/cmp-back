package pl.gov.cmp.permission_group;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static pl.gov.cmp.auth.model.enums.ObjectCategoryEnum.*;

@ExtendWith(MockitoExtension.class)
class PermissionGroupQueryServiceTest {

    @Mock
    private PermissionGroupRepository permissionGroupRepository;

    @InjectMocks
    private PermissionGroupQueryService permissionGroupQueryService;

    @Test
    void shouldFindAllPermissionGroupsByInstitutionType() {
        //given
        final var institutionType = CEMETERY;
        final var groupOne = new PermissionGroupEntity(24545L, "group 1", "description 1", Set.of(institutionType, VOIVODSHIP_OFFICE), Set.of());
        final var groupTwo = new PermissionGroupEntity(22345L, "group 2", "description 2", Set.of(CREMATORIUM, institutionType), Set.of());
        final var groupThree = new PermissionGroupEntity(63346L, "group 3", "description 3", Set.of(CREMATORIUM, VOIVODSHIP_OFFICE), Set.of());
        given(permissionGroupRepository.findAllWithInstitutionTypes()).willReturn(Set.of(groupOne, groupTwo, groupThree));

        //when
        final var result = permissionGroupQueryService.findAllByInstitutionType(institutionType);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).containsAll(Set.of(groupOne, groupTwo));
    }
}