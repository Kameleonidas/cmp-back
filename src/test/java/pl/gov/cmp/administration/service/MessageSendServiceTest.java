package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import pl.gov.cmp.administration.configuration.StatusChangeConfiguration;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.enums.MessageStatus;
import pl.gov.cmp.administration.model.mapper.MailMapper;
import pl.gov.cmp.administration.model.mapper.MessageMapper;
import pl.gov.cmp.administration.repository.MessageRepository;
import pl.gov.cmp.mail.model.Mail;
import pl.gov.cmp.mail.service.MailService;
import pl.gov.cmp.utils.DateProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageSendServiceTest {
    @Mock
    private MailMapper mailMapper;

    @Mock
    private MailService mailService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private DateProvider dateProvider;

    private MessageSendService messageSendService;

    @BeforeEach
    void setUp() {
        messageSendService = new MessageSendService(mailMapper, mailService, messageRepository, messageMapper, dateProvider);
    }

    @Test
    void shouldSendMessageCorrectly() {
        //given
        MessageDto messageDto = MessageDto.builder()
                .build();
        MessageEntity messageEntity = MessageEntity.builder().build();
        when(messageMapper.mapToMessageEntity(messageDto))
                .thenReturn(messageEntity);
        when(messageRepository.save(messageEntity)).thenReturn(messageEntity);
        Mail mail = Mail.builder().build();
        when(mailMapper.toMail(messageEntity)).thenReturn(mail);

        when(mailService.sendEmail(mail)).thenReturn(true);
        LocalDateTime sentDate = LocalDateTime.of(2011, 11, 11, 11, 11);
        when(dateProvider.getCurrentTimestamp()).thenReturn(sentDate);
        //when
        messageSendService.sendMessage(messageDto);
        //then
        assertThat(messageEntity.getSendDate()).isEqualTo(sentDate);
        assertThat(messageEntity.getStatus()).isEqualTo(MessageStatus.SENT);
    }

    @Test
    void shouldNotSendMessageCorrectly() {
        //given
        MessageDto messageDto = MessageDto.builder()
                .build();
        MessageEntity messageEntity = MessageEntity.builder().build();
        when(messageMapper.mapToMessageEntity(messageDto))
                .thenReturn(messageEntity);
        when(messageRepository.save(messageEntity)).thenReturn(messageEntity);
        Mail mail = Mail.builder().build();
        when(mailMapper.toMail(messageEntity)).thenReturn(mail);

        when(mailService.sendEmail(mail)).thenReturn(false);

        //when
        messageSendService.sendMessage(messageDto);
        //then
        assertThat(messageEntity.getStatus()).isEqualTo(MessageStatus.NEW);
    }
}
