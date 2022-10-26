package pl.gov.cmp.cemetery.model.dto;

import lombok.*;
import pl.gov.cmp.application.model.enums.PerpetualUseType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryPerpetualUserDto {
    private Long id;
    private PerpetualUseType type;
    private String name;
    private String firstName;
    private String lastName;
    private String nip;
    private String regon;
    private String email;
    private Long religionId;
    private Long perpetualChurchRegulatedByLawId;
    private Long perpetualChurchNotRegulatedByLawId;
    private String perpetualChurchesRegulatedByLawOrNo;
    private String nameOfParishPerpetualUse;
    private CemeteryRepresentativeDto representative;
}
