package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.enums.MessageStatus;
import pl.gov.cmp.administration.model.mapper.MailMapper;
import pl.gov.cmp.administration.model.mapper.MessageMapper;
import pl.gov.cmp.administration.repository.MessageRepository;
import pl.gov.cmp.mail.service.MailService;
import pl.gov.cmp.utils.DateProvider;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MessageSendService {

    private final MailMapper mailMapper;
    private final MailService mailService;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final DateProvider dateProvider;

    public boolean sendMessage(MessageDto messageDto) {
        log.info("Sending message [to={}]", messageDto);

        var messageEntity = messageMapper.mapToMessageEntity(messageDto);

        messageEntity.setStatus(MessageStatus.NEW);
        messageEntity.setCreateDate(dateProvider.getCurrentTimestamp());
        var savedMessage = messageRepository.save(messageEntity);

        log.debug("Saved message [{}]", savedMessage);
        boolean sendStatus = sendMessage(savedMessage);
        if (!sendStatus) {
            log.info("Messaging process end, without email send.");
            return false;
        }

        savedMessage.setStatus(MessageStatus.SENT);
        savedMessage.setSendDate(dateProvider.getCurrentTimestamp());
        var finalMessage = messageRepository.save(savedMessage);
        log.info("Final message [{}]", finalMessage);
        return true;
    }

    private boolean sendMessage(MessageEntity messageEntity) {
        log.debug("Prepare mail model [message={}]", messageEntity);

        var mail = mailMapper.toMail(messageEntity);

        log.info("Sending email [{}]", mail);
        return mailService.sendEmail(mail);
    }
}
