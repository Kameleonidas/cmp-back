package pl.gov.cmp.administration.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import pl.gov.cmp.administration.controller.protocol.request.AddUserPermissionGroupsRequest;
import pl.gov.cmp.administration.controller.protocol.request.UpdateUserRequest;
import pl.gov.cmp.administration.controller.protocol.request.UserPageRequest;
import pl.gov.cmp.administration.controller.protocol.response.UserPageResponse;
import pl.gov.cmp.administration.controller.protocol.response.UserResponse;
import pl.gov.cmp.administration.model.dto.AddUserPermissionGroupsDto;
import pl.gov.cmp.administration.model.dto.UpdateUserDto;
import pl.gov.cmp.administration.model.dto.UserCriteriaDto;
import pl.gov.cmp.administration.model.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    UpdateUserDto toUpdateUserDto(UpdateUserRequest request);

    AddUserPermissionGroupsDto toAddUserPermissionGroupsDto(AddUserPermissionGroupsRequest updateUserRequest);

    UserCriteriaDto toUserCriteriaDto(UserPageRequest request);

    default UserPageResponse toUserResponse(Page<UserDto> page, UserCriteriaDto criteria) {
        UserPageResponse response = new UserPageResponse();
        response.setElements(toApplicationElements(page.getContent()));
        response.setPageIndex(criteria.getPageIndex());
        response.setSortColumn(criteria.getSortColumn());
        response.setSortOrder(criteria.getSortOrder());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }

    List<UserResponse> toApplicationElements(List<UserDto> content);

    UserResponse mapToUserResponse(UserDto value);

}
