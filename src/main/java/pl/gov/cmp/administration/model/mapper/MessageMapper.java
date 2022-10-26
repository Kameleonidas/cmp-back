package pl.gov.cmp.administration.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import pl.gov.cmp.administration.controller.protocol.request.MessagePageRequest;
import pl.gov.cmp.administration.controller.protocol.response.MessagePageResponse;
import pl.gov.cmp.administration.controller.protocol.response.MessageResponse;
import pl.gov.cmp.administration.model.dto.CustomMessageDto;
import pl.gov.cmp.administration.model.dto.EmailSenderDto;
import pl.gov.cmp.administration.model.dto.MessageCriteriaDto;
import pl.gov.cmp.administration.model.dto.MessageDto;
import pl.gov.cmp.administration.model.entity.EmailSenderEntity;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.entity.MessageParameterEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MessageMapper {

    default MessageEntity mapToMessageEntity(MessageDto messageDto){
        Collection<MessageParameterEntity> parameters = mapToMessageParameterEntity(messageDto.getParameters());
        MessageEntity messageEntity = MessageEntity.builder()
                .emailSender(toEmailSenderEntity(messageDto.getEmailSender()))
                .templateName(messageDto.getTemplateName())
                .parameters(parameters)
                .build();
        parameters.forEach(messageParameter -> messageParameter.setMessage(messageEntity));
        return messageEntity;
    }

    private EmailSenderEntity toEmailSenderEntity(EmailSenderDto emailSenderDto) {
        return EmailSenderEntity.builder()
                .emailFrom(emailSenderDto.getEmailFrom())
                .emailTo(emailSenderDto.getEmailTo())
                .build();
    }

    private Collection<MessageParameterEntity> mapToMessageParameterEntity(Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(stringStringEntry ->
                        MessageParameterEntity.builder()
                                .key(stringStringEntry.getKey())
                                .value(stringStringEntry.getValue())
                                .build()
                ).collect(Collectors.toSet());
    }

    default MessagePageResponse toMessageResponse(Page<CustomMessageDto> page, MessageCriteriaDto criteria) {
        return MessagePageResponse.builder()
                .elements(toMessageElements(page.getContent()))
                .pageIndex(criteria.getPageIndex())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    default List<MessageResponse> toMessageElements(List<CustomMessageDto> applications) {
        return Optional.of(applications.stream()
                .map(this::messageDtoToMessageResponse)
                .collect(Collectors.toList())).orElse(null);
    }

    MessageResponse messageDtoToMessageResponse(CustomMessageDto applicationDto);

    MessageCriteriaDto toMessageCriteriaDto(MessagePageRequest request);
}
