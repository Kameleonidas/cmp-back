package pl.gov.cmp.administration.controller.protocol.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class MessageResponse {
    private String subject;
    private String body;
    private LocalDateTime sendDate;
}
