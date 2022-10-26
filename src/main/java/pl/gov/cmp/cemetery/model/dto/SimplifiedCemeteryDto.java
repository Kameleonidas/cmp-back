package pl.gov.cmp.cemetery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedCemeteryDto {
    private String voivodeshipTercCode;
    private String districtTercCode;
    private String communeTercCode;
}
