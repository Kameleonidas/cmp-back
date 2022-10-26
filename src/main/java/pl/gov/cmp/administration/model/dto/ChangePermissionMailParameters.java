package pl.gov.cmp.administration.model.dto;


import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class ChangePermissionMailParameters {
    private String email;
    private String institutionName;
    private String  userGrantPermissionFirstName;
    private String  userGrantPermissionLastName;
    private String permissionGroupName;
    private List<String> groupsNames;
}
