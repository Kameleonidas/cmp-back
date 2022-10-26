package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.administration.configuration.PermissionChangeConfiguration;
import pl.gov.cmp.administration.model.dto.ChangePermissionMailParameters;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PermissionChangeNotifierTest {

    private static final String EMAIL = "test@mail.test";
    private static final String PERMISSION_GROUP_NAME = "permissionGroupName";
    private static final String USER_GRANT_PERMISSION_FIRST_NAME = "Jan";
    private static final String USER_GRANT_PERMISSION_LAST_NAME = "Kowalski";
    private static final String TEMPLATE_NAME = "permissionChange.template";
    private static final String INSTITUTION_NAME = "Cmentarz niezarejestrowany";
    private static final List<String> GROUPS_NAME = List.of("GROUP_01", "GROUP_2");

    private  PermissionChangeNotifier permissionChangeNotifier;

    @Mock
    private PermissionChangeConfiguration permissionChangeConfiguration;

    @Mock
    private MessageSendService messageSendService;

    @Mock
    private CemeteryRepository cemeteryRepository;

    @Mock
    InstitutionResolver institutionResolver;

    @Captor
    private ArgumentCaptor<MessageDto> messageDtoCaptor;

    @BeforeEach()
    void setUp() {
        permissionChangeNotifier = new PermissionChangeNotifier(permissionChangeConfiguration, messageSendService, cemeteryRepository);
    }

    @Test
    void shouldSendEmailPermissionChange() {
        //given
        given(permissionChangeConfiguration.getSender()).willReturn(EMAIL);
        given(permissionChangeConfiguration.getTemplate()).willReturn(TEMPLATE_NAME);

        final var mailParameters = ChangePermissionMailParameters
                .builder()
                .email(EMAIL)
                .permissionGroupName(PERMISSION_GROUP_NAME)
                .userGrantPermissionFirstName(USER_GRANT_PERMISSION_FIRST_NAME)
                .userGrantPermissionLastName(USER_GRANT_PERMISSION_LAST_NAME)
                .institutionName(INSTITUTION_NAME)
                .groupsNames(GROUPS_NAME)
                .build();

        //when
        permissionChangeNotifier.sendEmail(mailParameters);

        //then
        verify(messageSendService).sendMessage(messageDtoCaptor.capture());
        MessageDto capturedMessageDto = messageDtoCaptor.getValue();
        assertThat(capturedMessageDto.getEmailSender().getEmailTo()).isEqualTo(EMAIL);
        assertThat(capturedMessageDto.getTemplateName()).isEqualTo(TEMPLATE_NAME);
        assertThat(capturedMessageDto.getParameters())
                .contains(Map.entry("institution_name", INSTITUTION_NAME))
                .contains(Map.entry("user_grant_permission_name", USER_GRANT_PERMISSION_FIRST_NAME + ' ' + USER_GRANT_PERMISSION_LAST_NAME))
                .contains(Map.entry("roles_name", "GROUP_01" + ", " + "GROUP_2"));
    }

}