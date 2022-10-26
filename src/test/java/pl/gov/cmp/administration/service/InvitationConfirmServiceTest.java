package pl.gov.cmp.administration.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.administration.model.dto.InstitutionDataCommand;
import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.administration.model.entity.EmailSenderEntity;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.entity.MessageParameterEntity;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.model.mapper.MapperConst;
import pl.gov.cmp.auth.security.facade.AuthenticationFacade;
import pl.gov.cmp.history.model.enums.HistoryOperationType;
import pl.gov.cmp.history.service.HistoryOperationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class InvitationConfirmServiceTest {

    @Mock
    private InvitationSaver invitationSaverMock;

    @Mock
    private HistoryOperationService historyOperationServiceMock;

    @Mock
    private InstitutionInformationGetterFactory institutionInformationGetterFactoryMock;

    @Mock
    private InstitutionInformationGetter informationGetterMock;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    private InvitationConfirmService invitationConfirmService;

    @BeforeEach
    void setUp() {
        given(institutionInformationGetterFactoryMock.findStrategy(any(InstitutionType.class)))
                .willReturn(informationGetterMock);
        invitationConfirmService = new InvitationConfirmService(
                invitationSaverMock,
                historyOperationServiceMock,
                institutionInformationGetterFactoryMock, userService, authenticationFacade);
    }

    @Test
    void shouldProcessConfirmCorrectly() {
        // given
        given(invitationSaverMock.confirmInvitationOnDB(anyString())).willReturn(
                InvitationEntity
                        .builder()
                        .message(MessageEntity.builder()
                                .emailSender(EmailSenderEntity.builder()
                                        .emailTo("tm@mc.gov.pl")
                                        .build())
                                .build())
                        .institutionType(InstitutionType.CEMETERY)
                        .institutionId(22345L)
                        .build());
        given(informationGetterMock.getInformation(anyLong())).willReturn(
                InstitutionInformationDto
                        .builder()
                        .name("test institution name")
                        .build()
        );

        // when
        invitationConfirmService.confirmInvitation("test_identifier", new InstitutionDataCommand("email@com.pl", "545656789"));

        // then

        then(invitationSaverMock).should().confirmInvitationOnDB("test_identifier");
        then(institutionInformationGetterFactoryMock).should().findStrategy(InstitutionType.CEMETERY);
        then(historyOperationServiceMock).should().saveHistoryOperation(argThat(historyOperation -> {
            assertThat(historyOperation.getType()).isEqualTo(HistoryOperationType.INVITATION_CONFIRMED);
            return true;
        }));
    }
}