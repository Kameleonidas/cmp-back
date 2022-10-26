package pl.gov.cmp.application.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.LegalForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ApplicationCemeteryManagerRequest {

    @NotNull
    private LegalForm type;
    @NotBlank
    private String name;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String nip;
    @NotBlank
    private String regon;
    @NotBlank
    private String email;
    private Boolean managerDataTheSameAsPerpetualUserOrObjectOwner;
    private ApplicationCemeteryRepresentativeRequest representative;

}
