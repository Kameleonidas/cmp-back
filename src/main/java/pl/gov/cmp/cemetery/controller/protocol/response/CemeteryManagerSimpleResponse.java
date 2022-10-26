package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.*;
import pl.gov.cmp.application.model.enums.LegalForm;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryManagerSimpleResponse {

    private LegalForm type;
    private String name;
    private String nip;
    private String regon;
    private String representative;
}