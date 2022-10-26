package pl.gov.cmp.gugik.service;

import static java.lang.String.format;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.gov.cmp.gugik.configuration.GugikAddressServiceConfiguration;
import pl.gov.cmp.gugik.model.dto.AddressDto;
import pl.gov.cmp.gugik.model.protocol.response.AddressResponse;

@Slf4j
@RequiredArgsConstructor
@Component
class AddressFetcher {

    private final RestTemplate restTemplate;

    private final GugikAddressServiceConfiguration gugikAddressServiceConfiguration;

    AddressDto fillAddressPart(Point addressGeometry, AddressDto addressDto) {
        log.debug(
                "Starting fetch address [addressGeometry={}, gugikConfiguration={}, addressDto={}]",
                addressGeometry,
                gugikAddressServiceConfiguration,
                addressDto);

        var correctWkt = getCoordinatesFromGeometry(addressGeometry);
        var addressUrl = gugikAddressServiceConfiguration.getServiceUrlForCoordinate(correctWkt);
        var result = restTemplate.getForObject(addressUrl, AddressResponse.class);
        log.debug("Address fetched [result={}]", result);
        if (result == null) {
            log.warn("Wrong address fetched [coordinate={}]", addressGeometry);
            return addressDto;
        }

        var correctAddress = fillSimcFields(addressDto, result);
        log.debug("Address filled [address={}]", correctAddress);

        return correctAddress;
    }

    private AddressDto fillSimcFields(AddressDto addressDto, AddressResponse result) {
        if (result.getResults() != null &&
                result.getResults().size() > 0) {

            var addressResult = result.getResults().get(1);
            addressDto.setPlace(addressResult.getCity());
            addressDto.setPlaceSimcCode(addressResult.getSimc());
        }
        return addressDto;
    }

    private String getCoordinatesFromGeometry(Geometry geometry) {
        var centroidCoordinate = geometry.getCoordinates()[0];
        return format("POINT(%s %s)", centroidCoordinate.x, centroidCoordinate.y);
    }
}
