package pl.gov.cmp.administration.model.mapper;

import com.google.common.collect.ImmutableMap;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import pl.gov.cmp.administration.model.dto.CustomMessageDto;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.exception.AppRollbackException;
import pl.gov.cmp.exception.ErrorCode;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Slf4j
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MessageDtoMapper {

    private static final String SUBJECT_SUFIX = ".subject.flth";
    private static final String BODY_SUFIX = ".pushbody.flth";
    private static final String COULD_NOT_INITIALIZE_TEMPLATE = "Could not initialize template";

    @Autowired
    private Configuration fmConfiguration;

    public abstract List<CustomMessageDto> toMessageDtoList(List<MessageEntity> content);

    public CustomMessageDto toMessageDto(MessageEntity message) {
        Map<String, Object> emailModel = toMap(message);
        try{
            return CustomMessageDto.builder()
                    .body(getContent(emailModel, message.getTemplateName() + BODY_SUFIX))
                    .subject(getContent(emailModel, message.getTemplateName() + SUBJECT_SUFIX))
                    .sendDate(message.getSendDate())
                    .build();
        } catch (Exception e) {
            throw new AppRollbackException(ErrorCode.INTERNAL_ERROR, COULD_NOT_INITIALIZE_TEMPLATE, e);
        }
    }

    private Map<String, Object> toMap(MessageEntity message) {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        message.getParameters().stream()
                .forEach(messageParameter -> builder.put(messageParameter.getKey(), messageParameter.getValue()));
        return builder.build();
    }

    private String getContent(Map<String, Object> emailModel, String template) throws IOException, TemplateException {
        var emailContentWriter = new StringWriter();
        fmConfiguration.getTemplate(template).process(emailModel, emailContentWriter);
        return emailContentWriter.getBuffer().toString();
    }
}
