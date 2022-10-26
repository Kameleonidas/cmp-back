package pl.gov.cmp.cemetery.controller.protocol.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReasonsRejectionApplication {

    REGISTERED("Zarejestrowany"),
    OTHER("Inny");

    private final String displayName;
}
