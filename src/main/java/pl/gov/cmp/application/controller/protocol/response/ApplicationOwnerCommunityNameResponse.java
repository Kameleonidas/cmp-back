package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationOwnerCommunityNameResponse {
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
