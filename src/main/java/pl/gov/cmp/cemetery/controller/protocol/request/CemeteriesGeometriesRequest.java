package pl.gov.cmp.cemetery.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CemeteriesGeometriesRequest {
    private String voivodeshipTercCode;
    private String districtTercCode;
    private String communeTercCode;
}
