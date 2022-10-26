package pl.gov.cmp.administration.service;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.configuration.PermissionChangeConfiguration;
import pl.gov.cmp.administration.model.dto.ChangePermissionMailParameters;
import pl.gov.cmp.administration.model.dto.EmailSenderDto;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PermissionChangeNotifier {

    private final PermissionChangeConfiguration permissionChangeConfiguration;
    private final MessageSendService messageSendService;
    private final CemeteryRepository cemeteryRepository;
    private static final String ADDED_DATE = "added_date";
    private static final String INSTITUTION_NAME = "institution_name";
    private static final String USER_GRANT_PERMISSION_NAME = "user_grant_permission_name";
    private static final String ROLES_NAME = "roles_name";

    public void sendEmail(ChangePermissionMailParameters changePermissionMailParameters) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        MessageDto messageDto = MessageDto.builder()
                .emailSender(EmailSenderDto.builder()
                        .emailTo(changePermissionMailParameters.getEmail())
                        .emailFrom(permissionChangeConfiguration.getSender())
                        .build())
                .templateName(permissionChangeConfiguration.getTemplate())
                .parameters(ImmutableMap.<String, String>builder()
                        .put(ADDED_DATE, formatter.format(LocalDateTime.now()))
                        .put(INSTITUTION_NAME, changePermissionMailParameters.getInstitutionName())
                        .put(USER_GRANT_PERMISSION_NAME, changePermissionMailParameters.getUserGrantPermissionFirstName() + ' ' + changePermissionMailParameters.getUserGrantPermissionLastName())
                        .put(ROLES_NAME, Joiner.on(", ").join(changePermissionMailParameters.getGroupsNames()))
                        .build())
                .build();
        messageSendService.sendMessage(messageDto);
    }
}
