package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedCemeteryElementResponse {

    private Long id;
    private String name;
    private CemeteryGeometryResponse cemeteryGeometry;
}
