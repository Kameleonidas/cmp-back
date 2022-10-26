package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileAttachmentRequest {
    @NotBlank
    @Schema(description="The file name")
    private String fileName;
    @NotBlank
    @Schema(description="The file hash name")
    private String fileHashedName;
}
