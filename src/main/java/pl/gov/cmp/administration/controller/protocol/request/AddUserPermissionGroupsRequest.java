package pl.gov.cmp.administration.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
public class AddUserPermissionGroupsRequest {
    @Schema(description="The user account id in institution")
    @NotNull
    private Long subjectId;
    @Schema(description="The permission group ids to be added")
    private Set<Long> permissionGroupIds;
}
