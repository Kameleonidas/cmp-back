package pl.gov.cmp.administration.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.administration.model.dto.UserDto;
import pl.gov.cmp.auth.model.dto.UserAccountToSubjectDto;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.model.enums.UserAccountStatusEnum;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserDtoMapper {

    List<UserDto> toUserDtoList(List<UserAccountEntity> content);

    @Mapping(source = "subjects", target = "categories", qualifiedByName = "subjectsToCategories")
    @Mapping(source = "status", target = "status", qualifiedByName = "enumToString")
    UserDto toUserDto(UserAccountEntity value);

    UserAccountToSubjectDto from(UserAccountToSubjectEntity entity);

    @Named("subjectsToCategories")
    default Set<String> subjectsToCategories(Collection<UserAccountToSubjectEntity> subjects) {
        return subjects.stream()
                .map(userAccountToSubjectEntity -> userAccountToSubjectEntity.getCategory().getName())
                .collect(Collectors.toSet());
    }

    @Named("enumToString")
    default String getStatusName(UserAccountStatusEnum status) {
        return status != null ? status.getName() : null;
    }

}
