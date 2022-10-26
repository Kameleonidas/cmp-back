package pl.gov.cmp.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAccountStatusEnum {
    NEW ("Nowy"),
    ACTIVE ("Aktywny"),
    REJECTED ("Odrzucony"),
    LOCKED ("Zablokowany");

    private final String name;
}
