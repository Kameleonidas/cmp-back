package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.LegalForm;

@Getter
@Setter
public class ApplicationCemeteryManagerResponse {

    private Long id;
    private LegalForm type;
    private String name;
    private String firstName;
    private String lastName;
    private String nip;
    private String regon;
    private String email;
    private Boolean managerDataTheSameAsPerpetualUserOrObjectOwner;
    private ApplicationCemeteryRepresentativeResponse representative;

}
