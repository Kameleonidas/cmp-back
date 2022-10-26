package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationFileAttachmentResponse {
    private String fileName;
    private String fileHashedName;
}
