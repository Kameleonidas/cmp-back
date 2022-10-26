package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.RepresentativeType;

@Getter
@Setter
public class ApplicationCemeteryRepresentativeResponse {

    private Long id;
    private RepresentativeType type;
    private String firstName;
    private String lastName;
    private String email;
    private String nameRepresentationPersonObjectManagerLegalPerson;
    private String surnameRepresentationPersonObjectManagerLegalPerson;
    private String emailRepresentationPersonObjectManagerLegalPerson;
}
