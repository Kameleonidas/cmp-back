package pl.gov.cmp.cemetery.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NameLocalGovernmentUnitDto {
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
