package pl.gov.cmp.cemetery.model.dto;

import lombok.*;
import pl.gov.cmp.application.model.enums.LegalForm;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryManagerDto {
    private Long id;
    private LegalForm type;
    private String name;
    private String firstName;
    private String lastName;
    private String nip;
    private String regon;
    private String email;
    private CemeteryRepresentativeDto representative;
}
