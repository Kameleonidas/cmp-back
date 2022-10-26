package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import pl.gov.cmp.administration.exception.InvitationNotFoundException;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.repository.InvitationRepository;

import java.util.Optional;

import static java.lang.String.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

class InvitationQueryServiceTest {

    private final InvitationRepository invitationRepository = mock(InvitationRepository.class);

    private final InvitationQueryService invitationQueryService = new InvitationQueryService(invitationRepository);

    @AfterEach
    void cleanup() {
        reset(invitationRepository);
    }

    @Test
    void shouldGetInvitationByRequestIdentifier() {
        //given
        final var invitationRequestIdentifier = "RqId234";
        final var invitation = createInvitation(invitationRequestIdentifier);
        given(invitationRepository.findByRequestIdentifier(invitationRequestIdentifier)).willReturn(Optional.of(invitation));

        //when
        final var result = invitationQueryService.getByRequestIdentifier(invitationRequestIdentifier);

        //then
        assertThat(result.getRequestIdentifier()).isEqualTo(invitationRequestIdentifier);
    }

    @Test
    void shouldThrowExceptionWhenInvitationWasNotFoundByRequestIdentifier() {
        //given
        final var invitationRequestIdentifier = "RqId234";
        given(invitationRepository.findByRequestIdentifier(invitationRequestIdentifier)).willReturn(Optional.empty());

        //when
        final var exception = assertThrows(InvitationNotFoundException.class, () -> invitationQueryService.getByRequestIdentifier(invitationRequestIdentifier));

        //then
        assertThat(exception.getMessage()).isEqualTo(format("Invitation not found [requestIdentifier: %s]", invitationRequestIdentifier));
    }

    private InvitationEntity createInvitation(String invitationRequestIdentifier) {
        return InvitationEntity.builder().requestIdentifier(invitationRequestIdentifier).build();
    }

}