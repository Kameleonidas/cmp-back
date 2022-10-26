package pl.gov.cmp.application.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.RepresentativeType;

@Builder
@Getter
@Setter
public class ApplicationCemeteryRepresentativeDto {

    private Long id;
    private RepresentativeType type;
    private String firstName;
    private String lastName;
    private String email;
    private String nameRepresentationPersonObjectManagerLegalPerson;
    private String surnameRepresentationPersonObjectManagerLegalPerson;
    private String emailRepresentationPersonObjectManagerLegalPerson;
}
