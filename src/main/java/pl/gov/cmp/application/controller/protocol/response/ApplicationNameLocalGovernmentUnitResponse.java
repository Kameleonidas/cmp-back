package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationNameLocalGovernmentUnitResponse {
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
