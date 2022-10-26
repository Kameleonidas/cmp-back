package pl.gov.cmp.administration.service;

import com.google.common.base.Functions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.configuration.InvitationConfiguration;
import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.administration.model.dto.InvitationDto;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.entity.MessageParameterEntity;
import pl.gov.cmp.administration.model.mapper.InvitationMapper;
import pl.gov.cmp.administration.model.mapper.MailMapper;
import pl.gov.cmp.administration.model.mapper.MapperConst;
import pl.gov.cmp.history.model.dto.HistoryOperationDto;
import pl.gov.cmp.history.model.enums.HistoryOperationType;
import pl.gov.cmp.history.service.HistoryOperationService;
import pl.gov.cmp.mail.service.MailService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationSendService {

    private final InvitationMapper invitationMapper;
    private final MailMapper mailMapper;
    private final InvitationSaver invitationSaver;
    private final HistoryOperationService historyOperationService;
    private final MailService mailService;
    private final InvitationConfiguration invitationConfiguration;
    private final InstitutionInformationGetterFactory institutionInformationGetterFactory;

    public Map<InvitationDto, Boolean> sendInvitations(List<InvitationDto> invitationsDto) {
        log.info("Sending invitations [to={}]", invitationsDto);
        return invitationsDto.stream().collect(Collectors.toMap(Functions.identity(), this::sendInvitation));
    }

    private boolean sendInvitation(InvitationDto invitationDto) {

        log.info("Sending invitation [to={}]", invitationDto);

        var information = getInstitutionInformation(invitationDto);
        log.info("Got entity information [{}]", information);

        var invitation = invitationMapper.mapToInvitationEntity(invitationDto, information, invitationConfiguration);

        var savedInvitation = invitationSaver.initInvitationOnDB(invitation);

        saveLogEvent(savedInvitation, information);

        log.debug("Saved invitation [{}]", savedInvitation);
        boolean sendStatus = sendMessage(savedInvitation.getMessage());
        if (!sendStatus) {
            log.info("Invitation process end, without email send.");
            return false;
        }

        var finalInvitation = invitationSaver.sendInvitationOnDB(savedInvitation);
        log.info("Final invitation [{}]", finalInvitation);
        return true;
    }

    private InstitutionInformationDto getInstitutionInformation(InvitationDto invitationDto) {
        return institutionInformationGetterFactory
                .findStrategy(invitationDto.getInstitutionType())
                .getInformation(invitationDto.getInstitutionId());
    }

    private boolean sendMessage(MessageEntity messageEntity) {
        log.debug("Prepare mail model [message={}]", messageEntity);

        var mail = mailMapper.toMail(messageEntity);

        log.info("Sending email [{}]", mail);
        return mailService.sendEmail(mail);
    }

    private void saveLogEvent(InvitationEntity invitation, InstitutionInformationDto information) {
        var message = invitation.getMessage();
        var historyItem = HistoryOperationDto.builder().type(HistoryOperationType.INVITATION_SEND)
                .param(information.getName())
                .param(message.getEmailSender().getEmailTo())
                .build();
        log.debug("Saving logEvent [{}]", historyItem);
        historyOperationService.saveHistoryOperation(historyItem);
    }
}
