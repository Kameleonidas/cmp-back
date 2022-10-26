package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserAccountToSubjectDetailedView {

    long subjectId;
    long institutionId;
    String institutionName;
    String institutionType;
    String email;
    String phoneNumber;
    List<String> rolesInInstitution;
    List<PermissionGroupView> permissionGroups;
    boolean activeEmployee;
}
