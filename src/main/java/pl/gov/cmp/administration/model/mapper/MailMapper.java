package pl.gov.cmp.administration.model.mapper;

import com.google.common.collect.ImmutableMap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.mail.model.Mail;

import java.util.Map;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MailMapper {

    @Mapping(target = "receiver", source = "message.emailSender.emailTo")
    @Mapping(target = "sender", source = "message.emailSender.emailFrom")
    @Mapping(target = "template", source = "message.templateName")
    @Mapping(target = "model", expression = "java(this.prepareMailModel(message))")
    Mail toMail(MessageEntity message);

    default Map<String, Object> prepareMailModel(MessageEntity message) {
        if (Objects.nonNull(message)) {
            ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
            message.getParameters()
                    .forEach(messageParameter -> builder.put(messageParameter.getKey(), messageParameter.getValue()));
            return builder.build();
        }
        return Map.of();
    }

}