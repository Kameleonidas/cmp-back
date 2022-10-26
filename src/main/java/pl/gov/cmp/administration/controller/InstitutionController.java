package pl.gov.cmp.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.administration.model.dto.InstitutionNameView;
import pl.gov.cmp.administration.service.InstitutionService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    @Operation(summary = "Get institution information", tags = "institution")
    @GetMapping("/name/{invitationRequestIdentifier}")
    public ResponseEntity<InstitutionNameView> getInstitutionName(@PathVariable String invitationRequestIdentifier) {
        return ResponseEntity.ok(institutionService.getNameByInvitationIdentifier(invitationRequestIdentifier));
    }
}