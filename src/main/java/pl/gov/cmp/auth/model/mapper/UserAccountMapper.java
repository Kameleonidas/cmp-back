package pl.gov.cmp.auth.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.model.dto.UserWkDto;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAccountMapper {

    UserAccountDto toUserAccountDto(UserAccountEntity userAccount);

    @Mapping(target = "wkId", source = "personIdentifier")
    @Mapping(target = "lastName", source = "familyName")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    UserAccountEntity toUserAccountEntity(UserWkDto user);

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    UserAccountEntity toUserAccountEntity(UserAccountDto userAccount);
}
