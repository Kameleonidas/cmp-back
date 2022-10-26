package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gov.cmp.administration.exception.InvitationNotFoundException;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.repository.InvitationRepository;

@Service
@RequiredArgsConstructor
public class InvitationQueryService {

    private final InvitationRepository invitationRepository;

    public InvitationEntity getByRequestIdentifier(String invitationRequestIdentifier) {
        return invitationRepository.findByRequestIdentifier(invitationRequestIdentifier)
                .orElseThrow(() -> new InvitationNotFoundException(invitationRequestIdentifier));
    }
}
