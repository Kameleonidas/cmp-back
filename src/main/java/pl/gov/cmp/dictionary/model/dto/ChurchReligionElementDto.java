package pl.gov.cmp.dictionary.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChurchReligionElementDto {

    private Long id;
    private String code;
    private String name;
    private boolean regulatedByLaw;
}
