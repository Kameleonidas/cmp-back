package pl.gov.cmp.gugik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.gov.cmp.gugik.configuration.GugikTercServiceConfiguration;
import pl.gov.cmp.gugik.model.dto.AddressDto;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
@Component
class TercFetcher {

    private final RestTemplate restTemplate;

    private final GugikTercServiceConfiguration gugikTercServiceConfiguration;

    private final GugikAddressesResponseParser gugikAddressesResponseParser;

    Optional<AddressDto> fetch(Point addressGeometry) {
        log.debug(
                "Starting fetch address [addressGeometry={}, gugikConfiguration={}]",
                addressGeometry,
                gugikTercServiceConfiguration);

        var addressCoordinates = getCoordinatesFromGeometry(addressGeometry);
        var addressUrl = gugikTercServiceConfiguration.getServiceUrlForCoordinate(addressCoordinates);
        var result = restTemplate.getForObject(addressUrl, String.class);
        log.debug("Address fetched [result={}]", result);
        if (result == null || !result.startsWith("0")) {
            log.warn("Wrong address fetched [coordinate={}, result={}]", addressGeometry, result);
            return Optional.empty();
        }

        var correctAddress = gugikAddressesResponseParser.parse(result);
        log.debug("Address parsed [address={}]", correctAddress);

        return correctAddress;
    }

    private String getCoordinatesFromGeometry(Geometry geometry) {
        var centroidCoordinate = geometry.getCoordinates()[0];
        return format("%s,%s", centroidCoordinate.x, centroidCoordinate.y);
    }
}
