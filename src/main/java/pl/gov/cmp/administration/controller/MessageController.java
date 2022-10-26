package pl.gov.cmp.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.administration.controller.protocol.request.MessagePageRequest;
import pl.gov.cmp.administration.controller.protocol.response.MessagePageResponse;
import pl.gov.cmp.administration.exception.ForbiddenAccessException;
import pl.gov.cmp.administration.model.dto.CustomMessageDto;
import pl.gov.cmp.administration.model.dto.MessageCriteriaDto;
import pl.gov.cmp.administration.model.mapper.MessageMapper;
import pl.gov.cmp.administration.service.MessageService;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.model.dto.UserAccountToSubjectDto;
import pl.gov.cmp.auth.security.configuration.CurrentUser;

import javax.validation.Valid;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Operation(summary = "Get push messages for subject user account", tags = "message")
    @GetMapping
    public ResponseEntity<MessagePageResponse> getMessages(@Parameter(hidden = true) @CurrentUser UserAccountDto currentUser, @Valid MessagePageRequest request) {
        checkUser(currentUser, request.getUserAccountToSubjectId());
        MessageCriteriaDto criteria = this.messageMapper.toMessageCriteriaDto(request);
        Page<CustomMessageDto> page = this.messageService.findByCriteria(criteria);
        MessagePageResponse response = this.messageMapper.toMessageResponse(page, criteria);
        return ResponseEntity.ok(response);
    }

    private void checkUser(UserAccountDto currentUser, Long userAccountToSubjectId) {
        if(currentUserDoesNotMatchUserAccountToSubjectId(currentUser, userAccountToSubjectId)) {
            throw new ForbiddenAccessException();
        }
    }

    private boolean currentUserDoesNotMatchUserAccountToSubjectId(UserAccountDto currentUser, Long userAccountToSubjectId) {
        return currentUser.getSubjects().stream()
                .filter(userAccountToSubjectDto -> isSubjectUser(userAccountToSubjectId, userAccountToSubjectDto))
                .findFirst().isEmpty();
    }

    private boolean isSubjectUser(Long userAccountToSubjectId, UserAccountToSubjectDto userAccountToSubjectDto) {
        return userAccountToSubjectDto.isActiveEmployee() && Objects.equals(userAccountToSubjectDto.getId(), userAccountToSubjectId);
    }
}