package pl.gov.cmp.cemetery.controller.protocol.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.gov.cmp.application.model.enums.LegalForm;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryManagerResponse {

    private Long id;
    private LegalForm type;
    private String name;
    @JsonIgnore
    private String firstName;
    @JsonIgnore
    private String lastName;
    private String nip;
    private String regon;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private CemeteryRepresentativeResponse representative;

}
