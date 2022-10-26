package pl.gov.cmp.mail.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import pl.gov.cmp.mail.model.Mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSenderMock;

    @Mock
    private Configuration fmConfigurationMock;

    @Mock
    private Template fmTemplateMock;
    @Mock
    private ResourceUtil resourceUtil;
    @TempDir
    private File tempDirectory;

    private MailService mailService;

    @BeforeEach
    void setUp() {
        mailService = new MailService(javaMailSenderMock, fmConfigurationMock, resourceUtil);
    }

    @Test
    void shouldSendEmailCorrectly() throws IOException, MessagingException {
        // given
        File testFile = new File(tempDirectory, "testowy.pdf");
        Mail mailToSend = Mail.builder()
                .sender("from@test.pl")
                .receiver("to@test.pl")
                .template("any")
                .attachment("test.pdf", testFile.getAbsolutePath())
                .build();
        MimeMessage preparedMimeMessage = new MimeMessage((Session) null);
        given(javaMailSenderMock.createMimeMessage()).willReturn(preparedMimeMessage);
        given(fmConfigurationMock.getTemplate(anyString())).willReturn(fmTemplateMock);

        // when
        boolean result = mailService.sendEmail(mailToSend);

        // then
        assertThat(result).isTrue();
        then(javaMailSenderMock).should().send(any(MimeMessage.class));
        assertThat(preparedMimeMessage.getFrom()[0].toString()).hasToString("from@test.pl");
        assertThat(preparedMimeMessage.getRecipients(Message.RecipientType.TO)[0].toString()).hasToString("to@test.pl");
    }

    @Test
    void shouldReturnFalseWhenExceptionDuringSending() throws IOException {
        // given
        File testFile = new File(tempDirectory, "testowy.pdf");
        Mail mailToSend = Mail.builder()
                .sender("from@test.pl")
                .receiver("to@test.pl")
                .template("any")
                .attachment("test.pdf", testFile.getAbsolutePath())
                .build();
        MimeMessage preparedMimeMessage = new MimeMessage((Session) null);
        given(javaMailSenderMock.createMimeMessage()).willReturn(preparedMimeMessage);
        given(fmConfigurationMock.getTemplate(anyString())).willThrow(TemplateNotFoundException.class);

        // when
        boolean result = mailService.sendEmail(mailToSend);

        // then
        assertThat(result).isFalse();
        then(javaMailSenderMock).should().createMimeMessage();
        then(javaMailSenderMock).shouldHaveNoMoreInteractions();
    }
}