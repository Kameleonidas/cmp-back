package pl.gov.cmp.gugik.model.protocol.response;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

    private Map<Integer, AddressResult> results;
}
