package pl.gov.cmp.cemetery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SimplifiedCemeteryElementDto {

    private Long id;
    private String name;
    private CemeteryGeometryDto cemeteryGeometry;
}
