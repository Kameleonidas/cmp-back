package pl.gov.cmp.cemetery.controller.protocol.response;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityNameResponse {
        private Long id;
        private String name;
        private String source;
        private String codeSimc;
        private String levelName;
}
