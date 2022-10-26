package pl.gov.cmp.application.controller.protocol.response;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApplicationDraftResponse {
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String draft;
    private String draftName;
    private Long id;
    private Long userAccountId;
}
