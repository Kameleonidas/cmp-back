package pl.gov.cmp.dictionary.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gov.cmp.dictionary.controller.protocol.mapper.DictionaryProtocolMapper;
import pl.gov.cmp.dictionary.controller.protocol.response.CemeteryOwnerCategoryResponse;
import pl.gov.cmp.dictionary.controller.protocol.response.ChurchReligionResponse;
import pl.gov.cmp.dictionary.controller.protocol.response.DictionaryResponse;
import pl.gov.cmp.dictionary.model.dto.ChurchReligionElementDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryCemeteryOwnerCategoryDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryElementDto;
import pl.gov.cmp.dictionary.service.DictionaryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dictionaries")
public class DictionaryController {

    private final DictionaryService dictionaryService;
    private final DictionaryProtocolMapper dictionaryProtocolMapper;

    @Operation(summary = "Get cemetery types", tags = "dictionary")
    @GetMapping("/cemetery-types")
    public ResponseEntity<DictionaryResponse> getCemeteryTypes() {
        List<DictionaryElementDto> cemeteryTypes = this.dictionaryService.getCemeteryTypes();
        DictionaryResponse response = this.dictionaryProtocolMapper.toDictionaryResponse(cemeteryTypes);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get cemetery facility types", tags = "dictionary")
    @GetMapping("/cemetery-facility-types")
    public ResponseEntity<DictionaryResponse> getCemeteryFacilityTypes() {
        List<DictionaryElementDto> cemeteryFacilityTypes = this.dictionaryService.getCemeteryFacilityTypes();
        DictionaryResponse response = this.dictionaryProtocolMapper.toDictionaryResponse(cemeteryFacilityTypes);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get churches and religious associations", tags = "dictionary")
    @GetMapping("/churches-religions")
    public ResponseEntity<ChurchReligionResponse> getCemeteryReligions() {
        List<ChurchReligionElementDto> churchesReligions = this.dictionaryService.getChurchesReligions();
        ChurchReligionResponse response = this.dictionaryProtocolMapper.toChurchesReligionResponse(churchesReligions);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get cemetery sources", tags = "dictionary")
    @GetMapping("/cemetery-sources")
    public ResponseEntity<DictionaryResponse> getCemeterySources() {
        List<DictionaryElementDto> cemeterySources = this.dictionaryService.getCemeterySources();
        DictionaryResponse response = this.dictionaryProtocolMapper.toDictionaryResponse(cemeterySources);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get cemetery owner categories", tags = "dictionary")
    @GetMapping("/cemetery-owner-categories")
    public ResponseEntity<List<CemeteryOwnerCategoryResponse>> getCemeteryOwnerCategories() {
        List<DictionaryCemeteryOwnerCategoryDto> cemeteryOwnerCategories = this.dictionaryService.getCemeteryOwnerCategories();
        var response = this.dictionaryProtocolMapper.toCemeteryOwnerCategoryResponse(cemeteryOwnerCategories);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user account statuses", tags = "dictionary")
    @GetMapping("/user-account-statuses")
    public ResponseEntity<DictionaryResponse> getUserAccountStatuses() {
        List<DictionaryElementDto> cemeterySources = this.dictionaryService.getUserAccountStatuses();
        DictionaryResponse response = this.dictionaryProtocolMapper.toDictionaryResponse(cemeterySources);
        return ResponseEntity.ok(response);
    }
}
