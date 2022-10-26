package pl.gov.cmp.administration.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.cmp.administration.controller.mapper.InvitationProtocolMapper;
import pl.gov.cmp.administration.controller.protocol.InvitationRequest;
import pl.gov.cmp.administration.model.dto.InstitutionDataCommand;
import pl.gov.cmp.administration.service.InvitationConfirmService;
import pl.gov.cmp.administration.service.InvitationSendService;

import javax.validation.Valid;

import static java.util.Collections.singletonList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationProtocolMapper invitationProtocolMapper;
    private final InvitationSendService invitationSendService;
    private final InvitationConfirmService invitationConfirmService;

    @Operation(summary = "Send invitation email", tags = "invitation")
    @PostMapping
    public ResponseEntity<Boolean> sendInvitation(@Valid @RequestBody InvitationRequest invitationRequest) {
        var invitationDto = invitationProtocolMapper.toInvitationDto(invitationRequest);
        var result = invitationSendService.sendInvitations(singletonList(invitationDto));
        return ResponseEntity.ok(result.get(invitationDto));
    }

    @Operation(summary = "Confirm invitation", tags = "invitation")
    @PutMapping("/{invitationRequestIdentifier}")
    public void confirmInvitation(@PathVariable String invitationRequestIdentifier, @Valid @RequestBody InstitutionDataCommand institutionDataCommand) {
        // Porocess ackeptacji zaproszenia, musi być wywołany po zalogowaniu użytkownika
        invitationConfirmService.confirmInvitation(invitationRequestIdentifier, institutionDataCommand);
    }
}
