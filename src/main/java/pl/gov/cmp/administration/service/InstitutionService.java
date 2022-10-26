package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.cmp.administration.model.dto.InstitutionNameView;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InvitationQueryService invitationQueryService;
    private final InstitutionInformationGetterFactory institutionInformationGetterFactory;

    public InstitutionNameView getNameByInvitationIdentifier(String invitationRequestIdentifier) {
        final var invitation = invitationQueryService.getByRequestIdentifier(invitationRequestIdentifier);
        final var institutionInformation = institutionInformationGetterFactory.findStrategy(invitation.getInstitutionType())
                .getInformation(invitation.getInstitutionId());
        return new InstitutionNameView(institutionInformation.getName());
    }
}
