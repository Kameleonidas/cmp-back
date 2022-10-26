package pl.gov.cmp.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.gov.cmp.administration.model.enums.InstitutionType;

@Getter
@AllArgsConstructor
public enum ObjectCategoryEnum {

    CMP("CMP"),
    CEMETERY("Cmentarze"),
    CREMATORIUM("Krematoria"),
    VOIVODSHIP_OFFICE("Urzędy wojewódzkie"),
    IPN("IPN"),
    KPRM("KPRM");

    private final String name;

    public static ObjectCategoryEnum from(InstitutionType institutionType) {
        switch (institutionType) {
            case CEMETERY:
                return CEMETERY;
            case CREMATORIUM:
                return CREMATORIUM;
            case IPN:
                return IPN;
            default:
                return null;
        }
    }
}
