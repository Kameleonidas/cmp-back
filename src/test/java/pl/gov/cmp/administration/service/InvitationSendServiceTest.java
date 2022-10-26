package pl.gov.cmp.administration.service;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.administration.configuration.InvitationConfiguration;
import pl.gov.cmp.administration.model.dto.CemeteryAddressDto;
import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.administration.model.dto.InvitationDto;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.model.enums.MessageStatus;
import pl.gov.cmp.administration.model.mapper.InvitationMapper;
import pl.gov.cmp.administration.model.mapper.MailMapper;
import pl.gov.cmp.administration.model.mapper.MailMapperImpl;
import pl.gov.cmp.administration.model.mapper.MapperConst;
import pl.gov.cmp.history.model.enums.HistoryOperationType;
import pl.gov.cmp.history.service.HistoryOperationService;
import pl.gov.cmp.mail.model.Mail;
import pl.gov.cmp.mail.service.MailService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class InvitationSendServiceTest {

    private final MailMapper mailMapper = new MailMapperImpl();

    @Mock
    private InvitationSaver invitationSaverMock;

    @Mock
    private HistoryOperationService historyOperationServiceMock;

    @Mock
    private MailService mailServiceMock;

    @Mock
    private InvitationConfiguration invitationConfigurationMock;

    @Mock
    private InstitutionInformationGetterFactory institutionInformationGetterFactoryMock;

    @Mock
    private InstitutionInformationGetter institutionInformationGetterMock;

    private InvitationSendService invitationSendService;

    @BeforeEach
    void setUp() {
        invitationSendService = new InvitationSendService(new InvitationMapper(), mailMapper, invitationSaverMock,
                historyOperationServiceMock, mailServiceMock, invitationConfigurationMock,
                institutionInformationGetterFactoryMock);
    }

    @Test
    void shouldProcessInvitationCorrectly() {
        // given
        LocalDateTime createTime = LocalDateTime.of(2011, 10, 10, 10, 10, 10);
        String identifier = UUID.randomUUID().toString();
        var invitationInformation = InvitationDto
                .builder()
                .email("test@mc.gov.pl")
                .institutionType(InstitutionType.CEMETERY)
                .institutionId(334488L)
                .name("Jan Testowy")
                .requestIdentifier(identifier)
                .build();
        given(invitationSaverMock.initInvitationOnDB(any(InvitationEntity.class))).willAnswer(invocation -> {
            InvitationEntity invitationToInit = invocation.getArgument(0);
            invitationToInit.setId(123L);
            invitationToInit.getMessage().setStatus(MessageStatus.NEW);
            invitationToInit.getMessage().setCreateDate(createTime);
            invitationToInit.setRequestIdentifier(identifier);
            return invitationToInit;
        });
        replayInstitutionInformationMockBehaviour();
        replayInvitationConfigurationBehaviour();
        given(mailServiceMock.sendEmail(any(Mail.class))).willReturn(true);


        // when
        var sendInvitation = invitationSendService.sendInvitations(singletonList(invitationInformation));

        // then
        assertThat(sendInvitation.entrySet()).containsOnly(Map.entry(invitationInformation, true));
        then(historyOperationServiceMock).should().saveHistoryOperation(argThat(historyOperation -> {
            assertThat(historyOperation.getType()).isEqualTo(HistoryOperationType.INVITATION_SEND);
            assertThat(historyOperation.getParams()).hasSize(2);
            assertThat(historyOperation.getParams())
                    .containsExactlyInAnyOrder("Cmentarz komunalny w Koninie", "test@mc.gov.pl");
            return true;
        }));
        then(mailServiceMock).should().sendEmail(argThat(mail -> {
            assertThat(mail.getSender()).isEqualTo("sender@mc.gov.pl");
            assertThat(mail.getTemplate()).isEqualTo("test-template");
            assertThat(mail.getAttachments()).isEmpty();
            assertThat(mail.getReceiver()).isEqualTo("test@mc.gov.pl");
            assertThat(mail.getModel()).containsOnly(
                    entry(MapperConst.INSTITUTION_NAME, "Cmentarz komunalny w Koninie"),
                    entry(MapperConst.CONFIRMATION_LINK_PARAMETER, "http://test.link/?id=" + identifier),
                    entry(MapperConst.INSTITUTION_ADDRESS, "12-345 place street 2"),
                    entry(MapperConst.INSTITUTION_CONTACT, "cemetery123@mc.gov.pl, 123-123-345"),
                    entry(MapperConst.LINK_VALIDITY, "0")
            );

            return true;
        }));
        then(invitationSaverMock).should().sendInvitationOnDB(any(InvitationEntity.class));
    }

    @Test
    void shouldReturnFalseWhenErrorDuringEmailSend() {
        // given
        LocalDateTime createTime = LocalDateTime.of(2011, 10, 10, 10, 10, 10);
        LocalDateTime modificationTime = LocalDateTime.of(2011, 11, 11, 11, 11, 11);
        String identifier = UUID.randomUUID().toString();
        var invitationInformation = InvitationDto
                .builder()
                .email("test@mc.gov.pl")
                .institutionType(InstitutionType.CEMETERY)
                .institutionId(334488L)
                .name("Jan Testowy")
                .build();
        given(invitationSaverMock.initInvitationOnDB(any(InvitationEntity.class))).willAnswer(invocation -> {
            InvitationEntity invitationToInit = invocation.getArgument(0);
            invitationToInit.setId(123L);
            invitationToInit.getMessage().setStatus(MessageStatus.NEW);
            invitationToInit.getMessage().setCreateDate(createTime);
            invitationToInit.getMessage().setSendDate(modificationTime);
            invitationToInit.setRequestIdentifier(identifier);
            return invitationToInit;
        });
        replayInstitutionInformationMockBehaviour();
        replayInvitationConfigurationBehaviour();
        given(mailServiceMock.sendEmail(any(Mail.class))).willReturn(false);


        // when
        var sendInvitation = invitationSendService.sendInvitations(singletonList(invitationInformation));

        // then
        assertThat(sendInvitation.entrySet()).containsOnly(Map.entry(invitationInformation, false));
        then(invitationSaverMock).should().initInvitationOnDB(any(InvitationEntity.class));
        then(invitationSaverMock).shouldHaveNoMoreInteractions();
    }

    private void replayInstitutionInformationMockBehaviour() {
        given(institutionInformationGetterFactoryMock.findStrategy(any(InstitutionType.class))).willReturn(institutionInformationGetterMock);
        given(institutionInformationGetterMock.getInformation(anyLong())).willReturn(InstitutionInformationDto
                .builder()
                    .name("Cmentarz komunalny w Koninie")
                    .contactAddress(CemeteryAddressDto.builder()
                            .number("2")
                            .street("street")
                            .zipCode("12-345")
                            .place("place")
                            .build())
                    .email("cemetery123@mc.gov.pl")
                        .phoneNumber("123-123-345")
                .build());
    }

    private void replayInvitationConfigurationBehaviour() {
        given(invitationConfigurationMock.getConfirmationUrls())
                .willReturn(ImmutableMap.of(InstitutionType.CEMETERY, "http://test.link/?id=%s"));
        given(invitationConfigurationMock.getSender()).willReturn("sender@mc.gov.pl");
        given(invitationConfigurationMock.getTemplate()).willReturn("test-template");
    }
}