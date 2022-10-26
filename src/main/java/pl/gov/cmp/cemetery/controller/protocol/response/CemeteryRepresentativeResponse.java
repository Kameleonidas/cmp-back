package pl.gov.cmp.cemetery.controller.protocol.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.gov.cmp.application.model.enums.RepresentativeType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CemeteryRepresentativeResponse {

    private Long id;
    private RepresentativeType type;
    @JsonIgnore
    private String firstName;
    @JsonIgnore
    private String lastName;
    @JsonIgnore
    private String email;
    private String nameRepresentationPersonObjectManagerLegalPerson;
    private String surnameRepresentationPersonObjectManagerLegalPerson;
    private String emailRepresentationPersonObjectManagerLegalPerson;
}
