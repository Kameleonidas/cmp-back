package pl.gov.cmp.cemetery.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gov.cmp.auth.model.dto.UserAccountDto;
import pl.gov.cmp.auth.security.configuration.CurrentUser;
import pl.gov.cmp.cemetery.controller.protocol.mapper.CemeteryProtocolMapper;
import pl.gov.cmp.cemetery.controller.protocol.request.CemeteriesGeometriesRequest;
import pl.gov.cmp.cemetery.controller.protocol.request.CemeteryPageRequest;
import pl.gov.cmp.cemetery.controller.protocol.request.CreateApplicationFromCemeteryRequest;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeterySimpleResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryPageResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.CemeteryResponse;
import pl.gov.cmp.cemetery.controller.protocol.response.SimplifiedCemeteryElementResponse;
import pl.gov.cmp.cemetery.model.dto.*;
import pl.gov.cmp.cemetery.service.CemeteryService;

import javax.validation.Valid;
import java.util.List;

import static pl.gov.cmp.cemetery.controller.protocol.mapper.CreateCemeteryFromApplicationProtocolMapper.toCemeteryResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cemeteries")
public class CemeteryController {

    private final CemeteryService cemeteryService;
    private final CemeteryProtocolMapper cemeteryProtocolMapper;

    @Operation(summary = "Get cemeteries by criteria", tags = "cemetery")
    @GetMapping
    public ResponseEntity<CemeteryPageResponse> getCemeteries(@Valid CemeteryPageRequest request) {
        CemeteryCriteriaDto criteria = cemeteryProtocolMapper.toCemeteryCriteriaDto(request);
        Page<CemeteryElementDto> page = cemeteryService.findByCriteria(criteria);
        CemeteryPageResponse response = cemeteryProtocolMapper.toCemeteryPageResponse(page, criteria);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get simplified cemeteries by criteria", tags = "cemetery")
    @GetMapping("/geometries")
    public List<SimplifiedCemeteryElementResponse> getSimplifiedCemeteries(@Valid CemeteriesGeometriesRequest cemeteriesGeometriesRequest) {
        SimplifiedCemeteryDto criteria = cemeteryProtocolMapper.toSimplifiedCemeteryDto(cemeteriesGeometriesRequest);
        List<SimplifiedCemeteryElementDto> list = cemeteryService.findSimplifiedCemeteriesByCriteria(criteria);
        return this.cemeteryProtocolMapper.toSimplifiedCemeteryResponse(list);
    }

    @Operation(summary = "Get cemetery by id", tags = "cemetery")
    @GetMapping("/{cemeteryId}")
    public ResponseEntity<CemeterySimpleResponse> getCemeterySimpleResponse (@PathVariable Long cemeteryId) {
        CemeteryResponseDto cemeteryDto = cemeteryService.getCemeteryResponse(cemeteryId);
        CemeterySimpleResponse response = cemeteryProtocolMapper.toCemeterySimpleResponse(cemeteryDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create cemetery from application", tags = "cemetery")
    @PostMapping("/create/cemetery")
    public ResponseEntity<CemeteryResponse> createCemeteryFromApplication(@Parameter(hidden = true) @CurrentUser UserAccountDto currentUser, @RequestBody CreateApplicationFromCemeteryRequest createApplicationFromCemeteryRequest) {
        var response = toCemeteryResponse(cemeteryService.registerCemeteryFromApplicationAndSendEmails(createApplicationFromCemeteryRequest.getApplicationId()));
        return ResponseEntity.ok(response);
    }
}
