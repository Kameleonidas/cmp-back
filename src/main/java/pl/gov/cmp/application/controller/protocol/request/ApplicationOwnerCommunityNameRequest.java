package pl.gov.cmp.application.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationOwnerCommunityNameRequest {
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
