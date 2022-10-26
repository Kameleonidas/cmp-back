package pl.gov.cmp.auth.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.auth.exception.UserAccountToSubjectNotFoundException;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.repository.UserAccountToSubjectRepository;
import pl.gov.cmp.exception.ErrorCode;

import java.util.Optional;

import static java.lang.String.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static pl.gov.cmp.auth.model.enums.ObjectCategoryEnum.CEMETERY;
import static pl.gov.cmp.exception.ErrorCode.USER_ACCOUNT_TO_SUBJECT_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class UserAccountToSubjectServiceTest {

    @Mock
    private UserAccountToSubjectRepository userAccountToSubjectRepository;

    @InjectMocks
    private UserAccountToSubjectService userAccountToSubjectService;

    @Test
    void shouldGetUserAccountToSubjectByIdAndCategory() {
        //given
        final var id = 365L;
        final var category = CEMETERY;
        final var cemeteryId = 325435L;
        final var subject = UserAccountToSubjectEntity.builder().id(id).category(category).cemeteryId(cemeteryId).build();
        given(userAccountToSubjectRepository.findByIdAndCategory(id, category)).willReturn(Optional.of(subject));

        //when
        final var result = userAccountToSubjectService.getByIdAndType(id, category);

        //then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getCategory()).isEqualTo(category);
        assertThat(result.getCemeteryId()).isEqualTo(cemeteryId);
    }

    @Test
    void shouldThrowExceptionWhenUserAccountToSubjectWasNotFoundByIdAndCategory() {
        //given
        final var id = 365L;
        final var category = CEMETERY;
        given(userAccountToSubjectRepository.findByIdAndCategory(id, category)).willReturn(Optional.empty());

        //when
        final var exception = assertThrows(UserAccountToSubjectNotFoundException.class,
                () -> userAccountToSubjectService.getByIdAndType(id, category));

        //then
        assertThat(exception.getCode()).isEqualTo(USER_ACCOUNT_TO_SUBJECT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(format("User account to subject with id %s and type %s not found", id, category));
    }
}