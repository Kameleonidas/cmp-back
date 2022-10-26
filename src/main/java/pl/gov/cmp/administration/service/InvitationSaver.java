package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.configuration.InvitationConfiguration;
import pl.gov.cmp.administration.exception.InvitationExpiredException;
import pl.gov.cmp.administration.exception.InvitationNotFoundException;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.enums.MessageStatus;
import pl.gov.cmp.administration.repository.InvitationRepository;
import pl.gov.cmp.utils.DateProvider;

import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
public class InvitationSaver {

    private final InvitationRepository invitationRepository;
    private final DateProvider dateProvider;
    private final InvitationConfiguration invitationConfiguration;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public InvitationEntity initInvitationOnDB(InvitationEntity invitationEntity) {

        var invitationToSave = invitationRepository.findByEmailAndInstitutionTypeAndInstitutionId(
                        invitationEntity.getMessage().getEmailSender().getEmailTo(),
                        invitationEntity.getInstitutionType(),
                        invitationEntity.getInstitutionId())
                .map(this::changeInvitationStatusToNew)
                .orElse(invitationEntity.init());

        return invitationRepository.save(invitationToSave);
    }

    InvitationEntity sendInvitationOnDB(InvitationEntity invitationEntity) {
        var invitationToSave = changeInvitationStatusToSent(invitationEntity);
        return invitationRepository.save(invitationToSave);
    }

    InvitationEntity confirmInvitationOnDB(String requestIdentifier) {
        var invitationEntity =
                invitationRepository.findByRequestIdentifierAndStatus(requestIdentifier, MessageStatus.SENT);

        invitationEntity.filter(this::ifInvitationHasExpired).ifPresent(
                invitation -> {
                    throw new InvitationExpiredException(requestIdentifier);
                }
        );
        var invitationToSave = invitationEntity.map(invitation -> {
                    invitation.getMessage().setStatus(MessageStatus.CONFIRMED);
                    invitation.getMessage().setConfirmDate(dateProvider.getCurrentTimestamp());
                    return invitation;
                })
                .orElseThrow(() -> new InvitationNotFoundException(requestIdentifier));

        return invitationRepository.save(invitationToSave);
    }

    private boolean ifInvitationHasExpired(InvitationEntity invitation) {
        return dateProvider.getCurrentTimestamp()
                .minus(invitationConfiguration.getTtl(), ChronoUnit.HOURS).compareTo(invitation.getMessage().getSendDate()) > 0;
    }

    private InvitationEntity changeInvitationStatusToNew(InvitationEntity invitationEntity) {
        invitationEntity.getMessage().setStatus(MessageStatus.NEW);
        return invitationEntity;
    }

    private InvitationEntity changeInvitationStatusToSent(InvitationEntity invitationEntity) {
        invitationEntity.getMessage().setStatus(MessageStatus.SENT);
        invitationEntity.getMessage().setSendDate(dateProvider.getCurrentTimestamp());
        return invitationEntity;
    }
}
