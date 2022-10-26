package pl.gov.cmp.process.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessCode {
    CEMETERIES_ADDRESSES_FILLER("Process odpowiedzialny za pobranie i ustawienie adresów cmentarzy.");

    private final String description;
}
