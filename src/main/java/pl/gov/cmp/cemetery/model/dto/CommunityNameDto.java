package pl.gov.cmp.cemetery.model.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityNameDto {
        private Long id;
        private String name;
        private String source;
        private String codeSimc;
        private String levelName;
}
