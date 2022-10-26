package pl.gov.cmp.administration.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.administration.controller.protocol.InvitationRequest;
import pl.gov.cmp.administration.model.dto.InvitationDto;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface InvitationProtocolMapper {

    @Mapping(target = "requestIdentifier", expression = "java(java.util.UUID.randomUUID().toString())")
    InvitationDto toInvitationDto(InvitationRequest invitationRequest);
}
