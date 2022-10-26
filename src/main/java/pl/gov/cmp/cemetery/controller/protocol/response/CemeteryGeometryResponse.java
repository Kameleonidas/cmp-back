package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.*;
import pl.gov.cmp.gugik.model.enums.GeometryType;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CemeteryGeometryResponse {

    private Long id;
    private String name;
    private String code;
    private LocalDate createDate;
    private LocalDate updateDate;
    private String description;
    private GeometryType geometryType;
    private String geometry;
    private String idIipIdentifier;
    private String idIipVersion;
}
