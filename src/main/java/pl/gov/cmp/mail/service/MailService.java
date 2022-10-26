package pl.gov.cmp.mail.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.gov.cmp.mail.model.Mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Component
public class MailService {

    private static final String SUBJECT_SUFIX = ".subject.flth";
    private static final String BODY_SUFIX = ".body.flth";
    private final JavaMailSender javaMailSender;
    private final Configuration fmConfiguration;
    private final ResourceUtil resourceUtil;

    public boolean sendEmail(Mail mail) {
        try {
            sendEmailInternal(mail);
        } catch (Exception exception) {
            log.error("Error during email send", exception);
            return false;
        }

        return true;
    }

    void sendEmailInternal(Mail mail) throws MessagingException, TemplateException, IOException {

        var mimeMessage = javaMailSender.createMimeMessage();
        var mimeMessageHelper = prepareMimeMessageHelper(mimeMessage, mail);
        processAttachments(mail, mimeMessageHelper);

        log.info("Sending email message: {}", mimeMessage);
        javaMailSender.send(mimeMessage);
    }

    private MimeMessageHelper prepareMimeMessageHelper(MimeMessage mimeMessage, Mail mail) throws MessagingException,
            TemplateException, IOException {
        var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setSubject(getContent(mail.getModel(), mail.getTemplate() + SUBJECT_SUFIX));
        mimeMessageHelper.setTo(mail.getReceiver());
        mimeMessageHelper.setFrom(mail.getSender());
        mimeMessageHelper.setText(getContent(mail.getModel(), mail.getTemplate() + BODY_SUFIX), true);
        Map<String, ClassPathResource> files = resourceUtil.getResourceFilesMap("/templates" + "/" + mail.getTemplate());
        for (Map.Entry<String, ClassPathResource> stringClassPathResourceEntry : files.entrySet()) {
            mimeMessageHelper.addInline(stringClassPathResourceEntry.getKey(), stringClassPathResourceEntry.getValue());
        }
        return mimeMessageHelper;
    }

    private void processAttachments(Mail mail, MimeMessageHelper mimeMessageHelper) throws MessagingException {
        for (Map.Entry<String, String> attachment : mail.getAttachments().entrySet()) {
            mimeMessageHelper.addAttachment(attachment.getKey(), new FileSystemResource(new File(attachment.getValue())));
        }
    }

    private String getContent(Map<String, Object> emailModel, String template) throws IOException, TemplateException {
        var emailContentWriter = new StringWriter();
        fmConfiguration.getTemplate(template).process(emailModel, emailContentWriter);
        return emailContentWriter.getBuffer().toString();
    }

}
