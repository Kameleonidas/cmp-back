package pl.gov.cmp.cemetery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.model.dto.UserAccountToSubjectDto;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.security.configuration.CurrentUser;
import pl.gov.cmp.cemetery.controller.protocol.mapper.UserCemeteryProtocolMapper;
import pl.gov.cmp.cemetery.controller.protocol.request.UserCemeteryPageRequest;
import pl.gov.cmp.cemetery.controller.protocol.response.UserCemeteryPageResponse;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryCriteriaDto;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryElementDto;
import pl.gov.cmp.cemetery.service.UserCemeteryService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/cemeteries")
public class UserCemeteryController {

    private final UserCemeteryService userCemeteryService;
    private final UserCemeteryProtocolMapper userCemeteryProtocolMapper;

    @Operation(summary = "Get user cemeteries", tags = "cemetery")
    @GetMapping
    public ResponseEntity<UserCemeteryPageResponse> getUserCemeteries(@Valid UserCemeteryPageRequest request,
                                                                  @Parameter(hidden = true) @CurrentUser UserAccountDto currentUser) {
        UserCemeteryCriteriaDto criteria = this.userCemeteryProtocolMapper.toUserCemeteryCriteriaDto(request);
        Set<Long> cemeteryIds = getCemeteryIds(currentUser);
        Page<UserCemeteryElementDto> page = this.userCemeteryService.findByCriteria(criteria, cemeteryIds);
        UserCemeteryPageResponse response = this.userCemeteryProtocolMapper.toUserCemeteryPageResponse(page, criteria);
        return ResponseEntity.ok(response);
    }

    private Set<Long> getCemeteryIds(UserAccountDto currentUser) {
        return currentUser.getSubjects().stream()
                .filter(userAccountToSubjectDto -> userAccountToSubjectDto.isActiveEmployee() && userAccountToSubjectDto.getCategory() == ObjectCategoryEnum.CEMETERY)
                .map(UserAccountToSubjectDto::getCemeteryId)
                .collect(Collectors.toSet());
    }
}
