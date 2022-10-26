package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.administration.exception.PersonalIdentifierGenerationException;
import pl.gov.cmp.auth.repository.UserAccountRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static pl.gov.cmp.exception.ErrorCode.*;

@ExtendWith(MockitoExtension.class)
class PersonalIdentifierGeneratorTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private PersonalIdentifierGenerator personalIdentifierGenerator;

    @Test
    void shouldGeneratePersonalIdentifier() {
        //given
        given(userAccountRepository.existsByLocalId(any())).willReturn(false);

        //when
        final var result = personalIdentifierGenerator.generate();

        //then
        assertThat(result)
                .hasSize(8)
                .matches("^[0-9]*$");
    }

    @Test
    void shouldThrowPersonalIdentifierGenerationExceptionWhenGeneratingPersonalIdFailed() {
        //given
        given(userAccountRepository.existsByLocalId(any())).willReturn(true);

        //when
        final var exception = assertThrows(PersonalIdentifierGenerationException.class, () -> personalIdentifierGenerator.generate());

        //then
        assertThat(exception.getCode()).isEqualTo(INTERNAL_ERROR);
        assertThat(exception.getMessage()).isEqualTo("Generating unique personal identifier failed");
        verify(userAccountRepository, times(1000)).existsByLocalId(any());
    }
}