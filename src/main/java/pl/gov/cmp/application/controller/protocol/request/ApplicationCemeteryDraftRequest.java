package pl.gov.cmp.application.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ApplicationCemeteryDraftRequest {

    @NotBlank
    String request;
    @NotBlank
    String draftName;
}
