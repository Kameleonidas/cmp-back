package pl.gov.cmp.application.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LegalForm {

    LEGAL_PERSON("Osoba prawna"),
    NATURAL_PERSON("Osoba fizyczna"),
    OWNER_UNKNOWN("Właściciel nieustalony"),
    UNIT_WITHOUT_LEGAL_PERSONALITY("Jednostka organizacyjna nieposiadająca osobowości prawnej"),
    NATIONAL_FORESTS("Lasy Państwowe"),
    NATIONAL_AGRICULTURAL_SUPPORT_CENTER("Krajowy Ośrodek Wsparcia Rolnictwa"),
    POLISH_WATER("Wody Polskie"),
    LOCAL_GOVERNMENT_UNIT("Jednostka samorządu terytorialnego"),
    UNION_OF_LOCAL_GOVERNMENT_UNIT("Związek jednostek samorządu terytorialnego"),
    STATE_TREASURY("Skarb Państwa"),
    INSTITUTION("Instytucja"),
    UNKNOWN("Nieznany"),
    PRIVATE_PERSON("Osoba prywatna");

    private final String name;
}