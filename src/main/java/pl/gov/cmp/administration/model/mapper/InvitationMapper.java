package pl.gov.cmp.administration.model.mapper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gov.cmp.administration.configuration.InvitationConfiguration;
import pl.gov.cmp.administration.model.dto.CemeteryAddressDto;
import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.administration.model.dto.InvitationDto;
import pl.gov.cmp.administration.model.entity.EmailSenderEntity;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.entity.MessageParameterEntity;

import static java.lang.String.format;

@Component
@AllArgsConstructor
public class InvitationMapper {

    private static final String EMPTY = "";

    public InvitationEntity mapToInvitationEntity(InvitationDto invitationDto, InstitutionInformationDto institutionInformationDto, InvitationConfiguration invitationConfiguration) {
        return InvitationEntity.builder()
                .message(mapToMessageEntity(invitationDto, institutionInformationDto, invitationConfiguration))
                .requestIdentifier(invitationDto.getRequestIdentifier())
                .institutionId(invitationDto.getInstitutionId())
                .institutionType(invitationDto.getInstitutionType())
                .build();
    }

    MessageEntity mapToMessageEntity(InvitationDto invitationDto, InstitutionInformationDto institutionInformationDto, InvitationConfiguration invitationConfiguration) {
        String link = format(invitationConfiguration.getConfirmationUrls().get(invitationDto.getInstitutionType()),
                invitationDto.getRequestIdentifier());
        String institutionName = institutionInformationDto.getName();
        String address = getAddress(institutionInformationDto.getContactAddress());
        String contact = getContact(institutionInformationDto);
        var parameters = Lists.newArrayList(
                MessageParameterEntity.builder().key(MapperConst.CONFIRMATION_LINK_PARAMETER).value(link).build(),
                MessageParameterEntity.builder().key(MapperConst.INSTITUTION_NAME).value(institutionName).build(),
                MessageParameterEntity.builder().key(MapperConst.LINK_VALIDITY).value(invitationConfiguration.getTtl().toString()).build(),
                MessageParameterEntity.builder().key(MapperConst.INSTITUTION_ADDRESS).value(address).build(),
                MessageParameterEntity.builder().key(MapperConst.INSTITUTION_CONTACT).value(contact).build()
        );
        MessageEntity messageEntity = MessageEntity.builder()
                .emailSender(EmailSenderEntity.builder()
                        .emailFrom(invitationConfiguration.getSender())
                        .emailTo(invitationDto.getEmail())
                        .build())
                .templateName(invitationConfiguration.getTemplate())
                .parameters(parameters)
                .build();
        parameters.forEach(messageParameter -> messageParameter.setMessage(messageEntity));
        return messageEntity;
    }

    private String getContact(InstitutionInformationDto institutionInformationDto) {
        String email = institutionInformationDto.getEmail();
        String phoneNumber = institutionInformationDto.getPhoneNumber();
        StringBuilder builder = new StringBuilder();
        if (email != null) {
            builder.append(email);
        }
        if (email != null && phoneNumber != null) {
            builder.append(", ");
        }
        if (phoneNumber != null) {
            builder.append(phoneNumber);
        }
        return builder.toString();
    }

    private String getAddress(CemeteryAddressDto contactAddress) {
        if (contactAddress == null) {
            return EMPTY;
        }
        return Joiner.on(" ").join(contactAddress.getZipCode(),
                contactAddress.getPlace(),
                contactAddress.getStreet(),
                contactAddress.getNumber());
    }
}
