package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.PerpetualUseType;

@Getter
@Setter
public class ApplicationCemeteryPerpetualUserResponse {

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
    private ApplicationCemeteryRepresentativeResponse representative;

}
