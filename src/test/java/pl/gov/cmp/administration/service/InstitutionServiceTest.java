package pl.gov.cmp.administration.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.cemetery.service.CemeteryInformationGetter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static pl.gov.cmp.administration.model.enums.InstitutionType.CEMETERY;

class InstitutionServiceTest {

    private final InvitationQueryService invitationQueryService = mock(InvitationQueryService.class);
    private final InstitutionInformationGetterFactory institutionInformationGetterFactory = mock(InstitutionInformationGetterFactory.class);

    private final InstitutionService institutionService = new InstitutionService(invitationQueryService, institutionInformationGetterFactory);

    @AfterEach
    void cleanup() {
        reset(invitationQueryService, institutionInformationGetterFactory);
    }

    @Test
    void shouldGetInstitutionName() {
        //given
        final var requestIdentifier = "asgsdfgf";
        final var institutionType = CEMETERY;
        final var institutionId = 2345L;
        final var invitation = createInvitation(requestIdentifier, institutionType, institutionId);
        final var institutionName = "Medieval cemetery";
        given(invitationQueryService.getByRequestIdentifier(requestIdentifier)).willReturn(invitation);
        final var institutionInformationGetter = mock(CemeteryInformationGetter.class);
        given(institutionInformationGetterFactory.findStrategy(institutionType)).willReturn(institutionInformationGetter);
        given(institutionInformationGetter.getInformation(institutionId)).willReturn(
                InstitutionInformationDto.builder().name(institutionName).build());

        //when
        final var result = institutionService.getNameByInvitationIdentifier(requestIdentifier);

        //then
        assertThat(result.getName()).isEqualTo(institutionName);
    }

    private InvitationEntity createInvitation(String requestIdentifier, InstitutionType institutionType, long institutionId) {
        final var invitation = new InvitationEntity();
        invitation.setRequestIdentifier(requestIdentifier);
        invitation.setInstitutionType(institutionType);
        invitation.setInstitutionId(institutionId);
        return invitation;
    }
}