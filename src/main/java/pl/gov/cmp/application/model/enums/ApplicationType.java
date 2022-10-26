package pl.gov.cmp.application.model.enums;

import lombok.Getter;

@Getter
public enum ApplicationType {

    CEMETERY_REGISTRATION("Wniosek o rejestrację cmentarza"),
    VETERAN_REGISTRATION("Wniosek o rejestrację weterana");

    private String name;

    ApplicationType(String name) {
        this.name = name;
    }

}
