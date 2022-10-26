package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.model.dto.InstitutionDataCommand;
import pl.gov.cmp.administration.model.dto.SaveUserAccountToSubjectDto;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.security.facade.AuthenticationFacade;
import pl.gov.cmp.history.model.dto.HistoryOperationDto;
import pl.gov.cmp.history.model.enums.HistoryOperationType;
import pl.gov.cmp.history.service.HistoryOperationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationConfirmService {

    private final InvitationSaver invitationSaver;
    private final HistoryOperationService historyOperationService;
    private final InstitutionInformationGetterFactory institutionInformationGetterFactory;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @Transactional
    public void confirmInvitation(String requestIdentifier, InstitutionDataCommand institutionDataCommand) {
        log.info("Confirming invitation [requestIdentifier={}]", requestIdentifier);
        var confirmedInvitation = invitationSaver.confirmInvitationOnDB(requestIdentifier);
        final var authenticatedUserId = authenticationFacade.getAuthenticatedUserId();
        userService.updateUserWithNewSubject(buildSaveUserAccountToSubjectDto(institutionDataCommand, confirmedInvitation, authenticatedUserId));
        log.info("Invitation saved to database [invitation={}]", confirmedInvitation);
        saveLogEvent(confirmedInvitation);
        log.info("Invitation confirmed.");
    }

    private SaveUserAccountToSubjectDto buildSaveUserAccountToSubjectDto(InstitutionDataCommand institutionDataCommand, InvitationEntity confirmedInvitation, long authenticatedUserId) {
        return SaveUserAccountToSubjectDto.builder()
                .userId(authenticatedUserId).institutionId(confirmedInvitation.getInstitutionId())
                .institutionType(ObjectCategoryEnum.from(confirmedInvitation.getInstitutionType()))
                .email(institutionDataCommand.getInstitutionEmail())
                .phoneNumber(institutionDataCommand.getInstitutionPhoneNumber())
                .build();
    }

    private void saveLogEvent(InvitationEntity confirmedInvitation) {
        var institutionInformation = institutionInformationGetterFactory
                .findStrategy(confirmedInvitation.getInstitutionType())
                .getInformation(confirmedInvitation.getInstitutionId());
        log.info("Got information from institution [data={}]", institutionInformation);
        var message = confirmedInvitation.getMessage();
        var operationHistory = HistoryOperationDto.builder()
                .type(HistoryOperationType.INVITATION_CONFIRMED)
                .param(institutionInformation.getName())
                .param(message.getEmailSender().getEmailTo())
                .build();
        log.info("Operation history prepared [{}]", operationHistory);
        historyOperationService.saveHistoryOperation(operationHistory);
        log.info("Operation history saved.");
    }

}
