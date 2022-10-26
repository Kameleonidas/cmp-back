package pl.gov.cmp.cemetery.model.enums;

import lombok.Getter;

@Getter
public enum CemeteryStatus {
    ACTIVE("czynny"),
    CLOSED("zamknięty"),
    FOR_LIQUIDATION("do likwidacji"),
    LIQUIDATED("zlikwidowany"),
    BOUND("powiązany");

    private String name;

    CemeteryStatus(String name) {
        this.name = name;
    }

}
