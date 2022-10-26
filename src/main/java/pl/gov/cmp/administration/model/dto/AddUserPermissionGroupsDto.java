package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class AddUserPermissionGroupsDto {
    private Long subjectId;
    private Set<Long> permissionGroupIds;
}