package pl.gov.cmp.cemetery.model.dto;

import lombok.*;
import pl.gov.cmp.application.model.enums.RepresentativeType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CemeteryRepresentativeDto {
    private Long id;
    private RepresentativeType type;
    private String firstName;
    private String lastName;
    private String email;
    private String nameRepresentationPersonObjectManagerLegalPerson;
    private String surnameRepresentationPersonObjectManagerLegalPerson;
    private String emailRepresentationPersonObjectManagerLegalPerson;
}
