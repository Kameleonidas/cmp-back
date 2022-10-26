package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import pl.gov.cmp.administration.configuration.InvitationConfiguration;
import pl.gov.cmp.administration.exception.InvitationExpiredException;
import pl.gov.cmp.administration.exception.InvitationNotFoundException;
import pl.gov.cmp.administration.model.entity.EmailSenderEntity;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.model.enums.MessageStatus;
import pl.gov.cmp.administration.repository.InvitationRepository;
import pl.gov.cmp.utils.DateProvider;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class InvitationSaverTest {

    @Mock
    private InvitationRepository invitationRepositoryMock;

    @Mock
    private DateProvider dateProviderMock;

    @Mock
    private InvitationConfiguration invitationConfigurationMock;

    private InvitationSaver invitationSaver;

    @BeforeEach
    void setUp() {
        invitationSaver = new InvitationSaver(invitationRepositoryMock, dateProviderMock, invitationConfigurationMock);
    }

    @Test
    void shouldChangeInvitationStateToSendAndSaveToDB() {
        // given
        var invitation = InvitationEntity.builder().message(MessageEntity.builder().status(MessageStatus.NEW).build()).build();
        LocalDateTime modificationTimestamp = LocalDateTime.of(2011, 11, 11, 11, 11);
        given(dateProviderMock.getCurrentTimestamp()).willReturn(modificationTimestamp);
        given(invitationRepositoryMock.save(any(InvitationEntity.class))).willAnswer(prepareSaveAnswer());

        // when
        var result = invitationSaver.sendInvitationOnDB(invitation);

        // then
        assertThat(result.getMessage().getStatus()).isEqualTo(MessageStatus.SENT);
        assertThat(result.getMessage().getSendDate()).isEqualTo(modificationTimestamp);
        then(invitationRepositoryMock).should().save(invitation);
    }

    @Test
    void shouldInitInvitationWhenNoExistsOnDB() {
        // given
        var invitation = InvitationEntity.builder()
                .message(MessageEntity.builder()
                        .emailSender(EmailSenderEntity.builder()
                                .build())
                        .build())
                .build();
        given(invitationRepositoryMock.save(any(InvitationEntity.class))).willAnswer(prepareSaveAnswer());

        // when
        var result = invitationSaver.initInvitationOnDB(invitation);

        // then
        assertThat(result.getMessage().getStatus()).isEqualTo(MessageStatus.NEW);
        then(invitationRepositoryMock).should().save(invitation);
    }

    @Test
    void shouldInitInvitationWhenExistsOnDB() {
        // given
        var requestedInvitation = InvitationEntity.builder()
                .message(MessageEntity.builder()
                        .emailSender(EmailSenderEntity.builder()
                                .emailTo("test@mc.gov.pl")
                                .build())
                        .build())
                .institutionType(InstitutionType.CEMETERY)
                .institutionId(123456L)
                .build();
        var savedInvitation = InvitationEntity.builder()
                .message(MessageEntity.builder()
                        .emailSender(EmailSenderEntity.builder()
                                .emailTo("test@mc.gov.pl")
                                .build())
                        .status(MessageStatus.NEW)
                        .build())
                .institutionType(InstitutionType.CEMETERY)
                .institutionId(123456L)
                .id(123L)
                .requestIdentifier(UUID.randomUUID().toString())
                .build();
        given(invitationRepositoryMock.findByEmailAndInstitutionTypeAndInstitutionId(
                anyString(), any(InstitutionType.class), anyLong())).willReturn(Optional.of(savedInvitation));
        given(invitationRepositoryMock.save(any(InvitationEntity.class))).willAnswer(prepareSaveAnswer());

        // when
        var result = invitationSaver.initInvitationOnDB(requestedInvitation);

        // then
        then(invitationRepositoryMock).should().save(savedInvitation);
        then(invitationRepositoryMock).should()
                .findByEmailAndInstitutionTypeAndInstitutionId("test@mc.gov.pl", InstitutionType.CEMETERY, 123456L);
        assertThat(result.getMessage().getStatus()).isEqualTo(MessageStatus.NEW);
        assertThat(result.getRequestIdentifier()).isEqualTo(savedInvitation.getRequestIdentifier());
    }

    @Test
    void shouldConfirmInvitationCorrectly() {
        // given
        LocalDateTime sendTimestamp = LocalDateTime.now().minus(24, ChronoUnit.HOURS);
        LocalDateTime now = LocalDateTime.now();
        given(dateProviderMock.getCurrentTimestamp()).willReturn(now);
        given(invitationRepositoryMock.findByRequestIdentifierAndStatus(anyString(), any(MessageStatus.class)))
                .willReturn(Optional.of(
                                InvitationEntity
                                        .builder()
                                        .message(MessageEntity.builder()
                                                .sendDate(sendTimestamp)
                                                .build())
                                        .build()
                        )
                );
        given(invitationConfigurationMock.getTtl()).willReturn(25);
        given(invitationRepositoryMock.save(any(InvitationEntity.class))).willAnswer(prepareSaveAnswer());

        // when
        invitationSaver.confirmInvitationOnDB("test-req-id");

        // then
        then(invitationRepositoryMock).should()
                .findByRequestIdentifierAndStatus("test-req-id", MessageStatus.SENT);
        then(invitationRepositoryMock).should().save(argThat(toSave -> {
            assertThat(toSave.getMessage().getStatus()).isEqualTo(MessageStatus.CONFIRMED);
            assertThat(toSave.getMessage().getConfirmDate()).isEqualTo(now);
            return true;
        }));
    }

    @Test
    void shouldThrowExceptionWhenNoInvitationFound() {
        // given
        given(invitationRepositoryMock.findByRequestIdentifierAndStatus(anyString(), any(MessageStatus.class)))
                .willReturn(Optional.empty());

        // when then
        assertThrows(InvitationNotFoundException.class, () ->
                invitationSaver.confirmInvitationOnDB("test-req-id"));
    }

    @Test
    void shouldThrowExceptionWhenInvitationConfirmedTooLate() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sendTimestamp = now.minus(24, ChronoUnit.HOURS);
        given(dateProviderMock.getCurrentTimestamp()).willReturn(now);
        given(invitationRepositoryMock.findByRequestIdentifierAndStatus(anyString(), any(MessageStatus.class)))
                .willReturn(Optional.of(
                                InvitationEntity
                                        .builder()
                                        .message(MessageEntity.builder()
                                                .sendDate(sendTimestamp)
                                                .build())
                                        .build()
                        )
                );
        given(invitationConfigurationMock.getTtl()).willReturn(23);

        // when
        assertThrows(InvitationExpiredException.class, () ->
                invitationSaver.confirmInvitationOnDB("test-req-id"));

        // then
        then(invitationRepositoryMock).should()
                .findByRequestIdentifierAndStatus("test-req-id", MessageStatus.SENT);
    }

    private Answer<Object> prepareSaveAnswer() {
        return invocation -> {
            InvitationEntity invitationToSave = invocation.getArgument(0);
            if (invitationToSave.getId() == null) {
                invitationToSave.setId(new Random().nextLong());
            }
            return invitationToSave;
        };
    }
}