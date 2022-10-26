package pl.gov.cmp.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.application.controller.protocol.mapper.ApplicationCemeteryProtocolMapper;
import pl.gov.cmp.application.controller.protocol.mapper.ApplicationDraftMapper;
import pl.gov.cmp.application.controller.protocol.mapper.ApplicationMapper;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryApplicantDetailsRequest;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryDraftPageRequest;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryDraftRequest;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryRequest;
import pl.gov.cmp.application.controller.protocol.request.ApplicationPageRequest;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryApplicantResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationCemeteryResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationDraftPageResponse;
import pl.gov.cmp.application.controller.protocol.response.ApplicationPageResponse;
import pl.gov.cmp.application.controller.protocol.response.ReasonsRejectApplicationResponse;
import pl.gov.cmp.application.model.dto.ApplicationDraftDto;
import pl.gov.cmp.application.model.dto.ApplicationDto;
import pl.gov.cmp.application.model.dto.ApplicationToBeCompletedDto;
import pl.gov.cmp.application.model.mapper.ApplicationCemeteryApplicantMapper;
import pl.gov.cmp.application.service.ApplicationDraftService;
import pl.gov.cmp.application.service.ApplicationService;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.security.configuration.CurrentUser;
import pl.gov.cmp.cemetery.controller.protocol.model.RejectApplicationDto;
import pl.gov.cmp.cemetery.controller.protocol.request.RejectApplication;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/applications/cemeteries")
public class ApplicationCemeteryController {

    private final ApplicationCemeteryApplicantMapper applicantMapper;
    private final ApplicationService applicationService;
    private final ApplicationDraftService applicationDraftService;
    private final ApplicationCemeteryProtocolMapper mapper;
    private final ApplicationMapper applicationMapper;
    private final ApplicationDraftMapper applicationDraftMapper;

    @Operation(summary = "Create cemetery application", tags = "application")
    @PostMapping
    public ApplicationCemeteryResponse createApplication(@Parameter(hidden = true) @CurrentUser UserAccountDto currentUser, @Valid @RequestBody ApplicationCemeteryRequest request) {
        Optional.ofNullable(request.getApplicant())
                .ifPresent(applicant -> {
                    applicant.setFirstName(currentUser.getFirstName());
                    applicant.setLastName(currentUser.getLastName());
                    applicant.setUserId(currentUser.getId());
                });
        var applicationCemeteryDto = mapper.toApplicationCemeteryDto(request);
        applicationService.createApplicationCemetery(applicationCemeteryDto);
        return mapper.toApplicationCemeteryResponse(applicationCemeteryDto);
    }

    @Operation(summary = "Update cemetery application", tags = "application")
    @PutMapping("/update/{applicationId}")
    public ApplicationCemeteryResponse updateApplication(@Parameter(hidden = true) @CurrentUser UserAccountDto currentUser, @RequestBody ApplicationCemeteryRequest request, @PathVariable Long applicationId) {
        var applicationCemeteryDto = mapper.toApplicationCemeteryDto(request);
        applicationService.updateApplicationCemetery(applicationCemeteryDto, applicationId);
        return mapper.toApplicationCemeteryResponse(applicationCemeteryDto);
    }

    @Operation(summary = "Get cemetery application", tags = "application")
    @GetMapping("/{appId}")
    public ResponseEntity<ApplicationCemeteryResponse> getApplicationCemetery(@PathVariable Long appId) {
        var applicationCemeteryDto = applicationService.getApplicationCemetery(appId);
        var response = mapper.toApplicationCemeteryResponse(applicationCemeteryDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get cemetery application draft for user", tags = "application")
    @GetMapping("/drafts")
    public ResponseEntity<ApplicationDraftPageResponse> getApplicationDraft(@Valid ApplicationCemeteryDraftPageRequest request) {
        var criteria = applicationDraftMapper.toApplicationDraftCriteriaDto(request);
        Page<ApplicationDraftDto> page = applicationDraftService.findDraftByCriteria(criteria);
        var response = applicationDraftMapper.toApplicationDraftResponse(page, criteria);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Save cemetery application draft for user", tags = "application")
    @PostMapping("/drafts/{userId}")
    public void createApplicationDraft(@RequestBody ApplicationCemeteryDraftRequest request, @PathVariable Long userId) {
        applicationDraftService.saveApplicationCemeteryDraft(request, userId);
    }

    @Operation(summary = "Get application by criteria", tags = "application")
    @GetMapping
    public ResponseEntity<ApplicationPageResponse> getApplication(@Valid ApplicationPageRequest request) {
        var criteria = applicationMapper.toApplicationCriteriaDto(request);
        Page<ApplicationDto> page = applicationService.findByCriteria(criteria);
        var response = applicationMapper.toApplicationResponse(page, criteria);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get application by criteria for user", tags = "application")
    @GetMapping("/user")
    public ResponseEntity<ApplicationPageResponse> getApplicationForUser(@Valid ApplicationPageRequest request,
                                                                         @Parameter(hidden = true) @CurrentUser UserAccountDto currentUser) {
        Long userAccountId = currentUser.getId();
        var criteria = applicationMapper.toUserApplicationCriteriaDto(request, userAccountId);
        Page<ApplicationDto> page = applicationService.findByCriteria(criteria);
        var response = applicationMapper.toApplicationResponse(page, criteria);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Reject cemetery application", tags = "application")
    @PostMapping("/reject")
    public ResponseEntity<ReasonsRejectApplicationResponse> rejectApplication(@RequestBody RejectApplication rejectApplication) {
        var response = applicationService.rejectApplication(RejectApplicationDto
                .builder()
                .applicationId(rejectApplication.getApplicationId())
                .reasonRejectionApplication(rejectApplication.getReasonsRejectionApplication())
                .description(rejectApplication.getDescription())
                .build());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Return application to be completed", tags = "application")
    @PostMapping("/to-complete/{applicationId}")
    public void completeApplication (@RequestBody String fieldsToBeCompleted, @PathVariable Long applicationId) {
        applicationService.applicationToBeCompleted(ApplicationToBeCompletedDto
                .builder()
                .applicationId(applicationId)
                .fieldsToBeCompleted(fieldsToBeCompleted)
                .build());
    }

    @Operation(summary = "Return applicants", tags = "application")
    @GetMapping("/applicants")
    public List<ApplicationCemeteryApplicantResponse> getApplicants(@Valid ApplicationCemeteryApplicantDetailsRequest request) {
        final var applicants = applicationService.getApplicants(request.getFirstName(), request.getLastName());
        return applicantMapper.toApplicantResponseList(applicants);
    }
}
