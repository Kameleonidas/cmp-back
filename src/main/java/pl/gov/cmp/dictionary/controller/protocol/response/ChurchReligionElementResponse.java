package pl.gov.cmp.dictionary.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChurchReligionElementResponse {

    private Long id;
    private String code;
    private String name;
    private boolean regulatedByLaw;

}
