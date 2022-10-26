package pl.gov.cmp.application.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatus {

    SENT("Wysłany"),
    TO_BE_COMPLETED("Do uzupełnienia"),
    COMPLETED("Uzupełniony"),
    ACCEPTED("Zaakceptowany"),
    REJECTED("Odrzucony");

    private final String name;
}
