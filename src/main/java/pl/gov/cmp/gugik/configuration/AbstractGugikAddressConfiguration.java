package pl.gov.cmp.gugik.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponentsBuilder;

@ToString
@Getter
@Setter
class AbstractGugikAddressConfiguration {
    private String url;
    private String coordinateParamName;

    public String getServiceUrlForCoordinate(String coordinate) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(coordinateParamName, coordinate)
                .build()
                .toUriString();
    }
}
