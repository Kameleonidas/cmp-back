package pl.gov.cmp.administration.service;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.configuration.UserIdentifierConfiguration;
import pl.gov.cmp.administration.model.dto.EmailSenderDto;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class UserIdentifierSendService {

    private static final String USER_IDENTIFIER = "user_identifier";
    private static final String LOGIN_LINK = "login_link";

    private final UserIdentifierConfiguration userIdentifierConfiguration;
    private final MessageSendService messageSendService;

    public boolean sendUserIdentifier(UserAccountEntity userAccountEntity) {
        return userAccountEntity.getSubjects().stream().map(userAccountToSubjectEntity -> {
            MessageDto messageDto = MessageDto.builder()
                    .emailSender(EmailSenderDto.builder()
                            .emailTo(userAccountToSubjectEntity.getEmail())
                            .emailFrom(userIdentifierConfiguration.getSender())
                            .build())
                    .templateName(userIdentifierConfiguration.getTemplate())
                    .parameters(ImmutableMap.<String, String>builder()
                            .put(USER_IDENTIFIER, userAccountEntity.getLocalId())
                            .put(LOGIN_LINK, userIdentifierConfiguration.getLoginUrl())
                            .build())
                    .build();
            return messageSendService.sendMessage(messageDto);
        }).reduce((current, next) -> current && next).get();
    }
}
