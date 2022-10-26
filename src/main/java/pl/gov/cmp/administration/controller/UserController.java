package pl.gov.cmp.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.cmp.administration.controller.mapper.UserMapper;
import pl.gov.cmp.administration.controller.protocol.request.*;
import pl.gov.cmp.administration.controller.protocol.response.UserPageResponse;
import pl.gov.cmp.administration.controller.protocol.response.UserResponse;
import pl.gov.cmp.administration.model.dto.*;
import pl.gov.cmp.administration.service.UserService;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.security.configuration.CurrentUser;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @Operation(summary = "Get users by criteria", tags = "user")
    @GetMapping
    public ResponseEntity<UserPageResponse> getUsers(@Valid UserPageRequest request) {
        UserCriteriaDto criteria = this.userMapper.toUserCriteriaDto(request);
        Page<UserDto> page = this.userService.findByCriteria(criteria);
        UserPageResponse response = this.userMapper.toUserResponse(page, criteria);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user details", tags = "user")
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailedView> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserDetailedView(id));
    }

    @Operation(summary = "Update user data", tags = "user")
    @PutMapping
    public void updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        var updateUserDto = userMapper.toUpdateUserDto(updateUserRequest);
        userService.updateUser(updateUserDto);
    }

    @Operation(summary = "Add permission groups to the user", tags = "user")
    @PutMapping("/add-permission-groups")
    public void addUserPermissionGroups(@Valid @RequestBody AddUserPermissionGroupsRequest addUserPermissionGroupsRequest) {
        var addUserPermissionGroupsDto = userMapper.toAddUserPermissionGroupsDto(addUserPermissionGroupsRequest);
        userService.addUserPermissionGroups(addUserPermissionGroupsDto);
    }

    @Operation(summary = "Remove permission groups from the user", tags = "user")
    @DeleteMapping("/remove-permission-group/{subjectId}/{permissionGroupId}")
    public void removeUserPermissionGroups(@PathVariable long subjectId, @PathVariable long permissionGroupId) {
        userService.removeUserPermissionGroup(subjectId, permissionGroupId);
    }

    @Operation(summary = "Unblock user", tags = "user")
    @PutMapping("/unblock-user")
    public ResponseEntity<UserResponse> unblockUser(@Valid @RequestBody UnblockUserRequest unblockUserRequest) {
        var response = userService.unblockUser(unblockUserRequest.getUserId());
        return ResponseEntity.ok(userMapper.mapToUserResponse(response));
    }

    @Operation(summary = "Block user", tags = "user")
    @PutMapping("/block-user")
    public ResponseEntity<UserResponse> blockUser(@Valid @RequestBody BlockUserRequest blockUserRequest) {
        var response = userService.blockUser(blockUserRequest.getUserId());
        return ResponseEntity.ok(userMapper.mapToUserResponse(response));
    }

    @Operation(summary = "Giving the user identity in the context of the local application", tags = "user")
    @PutMapping("/set-password-id-for-local-user")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody AddLocalUserRequest addLocalUserRequest, @Parameter(hidden = true) @CurrentUser UserAccountDto currentUser) {
        var response = userService.setPasswordAndLocalId(SetPasswordForUserDto.builder()
                .password(addLocalUserRequest.getPassword())
                .wkId(currentUser.getWkId())
                .build());
        return ResponseEntity.ok(userMapper.mapToUserResponse(response));
    }


}
