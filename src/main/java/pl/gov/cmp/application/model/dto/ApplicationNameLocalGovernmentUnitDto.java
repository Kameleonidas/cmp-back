package pl.gov.cmp.application.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApplicationNameLocalGovernmentUnitDto {
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
