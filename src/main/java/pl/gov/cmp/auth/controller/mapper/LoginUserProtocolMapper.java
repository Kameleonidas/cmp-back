package pl.gov.cmp.auth.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.auth.controller.protocol.UserAccountResponse;
import pl.gov.cmp.auth.model.dto.UserAccountDto;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoginUserProtocolMapper {

    UserAccountResponse toUserAccountResponse(UserAccountDto userAccount);

}
