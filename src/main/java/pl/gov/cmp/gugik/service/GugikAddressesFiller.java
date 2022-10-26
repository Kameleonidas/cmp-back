package pl.gov.cmp.gugik.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.gov.cmp.cemetery.service.CemeteryAddressFiller;
import pl.gov.cmp.gugik.model.dto.AddressDto;
import pl.gov.cmp.gugik.model.entity.GugikCemeteryGeometryEntity;
import pl.gov.cmp.gugik.model.mapper.GugikAddressDtoMapper;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class GugikAddressesFiller {

    private final TercFetcher tercFetcher;

    private final AddressFetcher addressFetcher;

    private final CemeteryAddressFiller cemeteryAddressFiller;

    private final GugikAddressDtoMapper gugikAddressDtoMapper;

    Optional<AddressDto> processCemeteryGeometry(GugikCemeteryGeometryEntity geometry) {
        if (geometry.getAddressPoint() == null) {
            geometry.setAddressPoint(geometry.getGeometry().getCentroid());
        }
        return tercFetcher.fetch(geometry.getAddressPoint())
                .map(addressDto -> addressFetcher.fillAddressPart(geometry.getAddressPoint(), addressDto));
    }

    void processFetchedAddress(GugikCemeteryGeometryEntity geometry, AddressDto addressDto) {
        fillAddressInGugikCemetery(geometry, addressDto);
        cemeteryAddressFiller.fill(geometry.getIdIipIdentifier(), addressDto);
        log.info("Address processed.");
    }

    private void fillAddressInGugikCemetery(GugikCemeteryGeometryEntity cemeteryGeometry, AddressDto addressDto) {
        var mappedAddress = gugikAddressDtoMapper.toGugikAddressEntity(addressDto);
        cemeteryGeometry.setAddress(mappedAddress);
        log.debug("Filled gugikCemeteryAddress [gugikCemeteryGeometry={}, addressDto={}, mappedAddress={}]",
                cemeteryGeometry, addressDto, mappedAddress);
    }
}
