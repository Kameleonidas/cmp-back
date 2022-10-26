package pl.gov.cmp.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Mail {

    private String sender;

    private String receiver;

    @Singular("modelElement")
    private Map<String, Object> model = new HashMap<>();

    private String template;

    @Singular
    private Map<String, String> attachments = new HashMap<>();
}
