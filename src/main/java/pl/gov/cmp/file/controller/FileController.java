package pl.gov.cmp.file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.gov.cmp.file.controller.mapper.FileProtocolMapper;
import pl.gov.cmp.file.controller.response.FileResourceResponse;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.service.FileService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileService fileService;
    private final FileProtocolMapper fileProtocolMapper;

    @Operation(summary = "Upload file", tags = "file")
    @ApiResponse(content = @Content(schema = @Schema(implementation = FileResourceDto.class)))
    @PostMapping(value="/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResourceResponse> uploadApplicationAttachment(@RequestParam("file") MultipartFile file) {
        var fileResourceResponse = fileProtocolMapper.toFileResourceResponse(fileService.uploadFile(file));
        return ResponseEntity.ok(fileResourceResponse);
    }

    @Operation(summary = "Download application attachment", tags = "file")
    @ApiResponse(responseCode = "200", description = "Download attachment for application", content = @Content(schema = @Schema(implementation = FileResourceResponse.class)))
    @GetMapping("/download-application-attachment")
    public ResponseEntity<FileResourceResponse> downloadApplicationAttachment(Long applicationId, String fileHashedName) {
        var fileResourceResponse = fileProtocolMapper
                .toFileResourceResponse(fileService.downloadApplicationAttachment(applicationId, fileHashedName));
        return ResponseEntity.ok(fileResourceResponse);
    }

    @Operation(summary = "Accept cemetery images", tags = "file")
    @ApiResponse(content = @Content(schema = @Schema(implementation = FileResourceDto.class)))
    @PostMapping(value="/accept-cemetery-images")
    public ResponseEntity<List<FileResourceResponse>> acceptCemeteryImages(Long cemeteryId, @RequestBody List<String> applicationAttachment) {
        var fileResourceResponse = fileProtocolMapper
                .toFileResourceResponse(fileService.acceptCemeteryImages(cemeteryId, applicationAttachment));
        return ResponseEntity.ok(fileResourceResponse);
    }

    @Operation(summary = "Download cemetery image", tags = "file")
    @ApiResponse(responseCode = "200", description = "Download image for cemetery", content = @Content(schema = @Schema(implementation = FileResourceResponse.class)))
    @GetMapping("/download-cemetery-image")
    public ResponseEntity<FileResourceResponse> downloadCemeteryImage(Long cemeteryId, String fileHashedName) {
        var fileResourceResponse = fileProtocolMapper
                .toFileResourceResponse(fileService.downloadCemeteryImage(cemeteryId, fileHashedName));
        return ResponseEntity.ok(fileResourceResponse);
    }

    @Operation(summary = "Download cemetery attachment", tags = "file")
    @ApiResponse(responseCode = "200", description = "Download attachment for cemetery", content = @Content(schema = @Schema(implementation = FileResourceResponse.class)))
    @GetMapping("/download-cemetery-attachment")
    public ResponseEntity<FileResourceResponse> downloadCemeteryAttachment(Long cemeteryId, String fileHashedName) {
        var fileResourceResponse = fileProtocolMapper
                .toFileResourceResponse(fileService.downloadCemeteryAttachment(cemeteryId, fileHashedName));
        return ResponseEntity.ok(fileResourceResponse);
    }
 }
