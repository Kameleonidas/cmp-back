package pl.gov.cmp.administration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class EmailSenderDto {
    private String emailFrom;
    private String emailTo;
}
