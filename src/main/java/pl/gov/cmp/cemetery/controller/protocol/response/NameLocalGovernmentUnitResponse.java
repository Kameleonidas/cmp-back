package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NameLocalGovernmentUnitResponse {
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
