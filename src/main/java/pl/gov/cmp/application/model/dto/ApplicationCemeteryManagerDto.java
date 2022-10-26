package pl.gov.cmp.application.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.LegalForm;

@Builder
@Getter
@Setter
public class ApplicationCemeteryManagerDto {

    private Long id;
    private LegalForm type;
    private String name;
    private String firstName;
    private String lastName;
    private String nip;
    private String regon;
    private String email;
    private Boolean managerDataTheSameAsPerpetualUserOrObjectOwner;
    private ApplicationCemeteryRepresentativeDto representative;

}
