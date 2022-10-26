package pl.gov.cmp.dictionary.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChurchReligionResponse {

    private List<ChurchReligionElementResponse> elements;

}
