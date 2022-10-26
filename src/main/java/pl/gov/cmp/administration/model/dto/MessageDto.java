package pl.gov.cmp.administration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class MessageDto {
    private EmailSenderDto emailSender;
    private String templateName;
    private Map<String, String> parameters;
}
