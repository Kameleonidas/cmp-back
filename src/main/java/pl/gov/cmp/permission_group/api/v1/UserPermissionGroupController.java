package pl.gov.cmp.permission_group.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.permission_group.InstitutionPermissionGroupQueryService;
import pl.gov.cmp.permission_group.PermissionGroupResponse;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subject")
public class UserPermissionGroupController {

    private final InstitutionPermissionGroupQueryService institutionPermissionGroupQueryService;

    @Operation(summary = "Get missing permission groups for subject", tags = "permissionGroup")
    @GetMapping("/{subjectId}/{institutionType}/permission-groups/missing")
    public ResponseEntity<List<PermissionGroupResponse>> findMissingPermissionsInInstitution(@PathVariable long subjectId,
                                                                                             @PathVariable ObjectCategoryEnum institutionType) {
        return ResponseEntity.ok(institutionPermissionGroupQueryService.findMissingPermissionGroupsInInstitution(subjectId, institutionType));
    }
}
