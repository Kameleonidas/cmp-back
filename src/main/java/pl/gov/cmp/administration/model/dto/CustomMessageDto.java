package pl.gov.cmp.administration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class CustomMessageDto {
    private String subject;
    private String body;
    private LocalDateTime sendDate;
}
