package pl.gov.cmp.cemetery.controller.protocol.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.gov.cmp.application.model.enums.PerpetualUseType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryPerpetualUserResponse {

    private Long id;
    private PerpetualUseType type;
    private String name;
    @JsonIgnore
    private String firstName;
    @JsonIgnore
    private String lastName;
    private String nip;
    private String regon;
    @JsonIgnore
    private String email;
    private Long religionId;
    private Long perpetualChurchRegulatedByLawId;
    private Long perpetualChurchNotRegulatedByLawId;
    private String perpetualChurchesRegulatedByLawOrNo;
    private String nameOfParishPerpetualUse;
    @JsonIgnore
    private CemeteryRepresentativeResponse representative;

}
