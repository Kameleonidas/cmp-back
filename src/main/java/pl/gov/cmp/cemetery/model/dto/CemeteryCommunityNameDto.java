package pl.gov.cmp.cemetery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CemeteryCommunityNameDto {
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
