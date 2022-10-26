package pl.gov.cmp.cemetery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryGeometryEntity;
import pl.gov.cmp.cemetery.model.mapper.AddressDtoMapper;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;
import pl.gov.cmp.gugik.model.dto.AddressDto;
import pl.gov.cmp.gugik.model.entity.GugikCemeteryGeometryEntity;
import pl.gov.cmp.gugik.model.mapper.GugikAddressDtoMapper;
import pl.gov.cmp.gugik.repository.GugikCemeteryGeometryRepository;

import java.util.concurrent.atomic.AtomicLong;

import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class CemeteryAddressesChecker {

    private final CemeteryRepository cemeteryRepository;
    private final GugikCemeteryGeometryRepository gugikCemeteryGeometryRepository;
    private final GugikAddressDtoMapper gugikAddressDtoMapper;
    private final AddressDtoMapper addressDtoMapper;

    public void checkCemeteries() {
        log.info("Checking cemeteries without addresses ...");
        var cemeteriesWithoutAddresses = cemeteryRepository.findByLocationAddressIsNull();
        log.info("Got cemeteries without addresses [count={}]", cemeteriesWithoutAddresses.size());
        AtomicLong count = new AtomicLong();
        cemeteriesWithoutAddresses
                .forEach(cemetery -> processCemetery(count, cemetery));
        log.info("Cemeteries checked.");
    }

    private void processCemetery(AtomicLong count, CemeteryEntity cemetery) {
        of(cemetery.getCemeteryGeometry())
                .map(CemeteryGeometryEntity::getIdIipIdentifier)
                .ifPresent(surfaceIdentifier -> this.processSurfaceIdentifier(count, surfaceIdentifier, cemetery));
    }

    private void processSurfaceIdentifier(AtomicLong count, String surfaceIdentifier, CemeteryEntity cemetery) {
        log.info("Processing cemetery [count={}, idIipIdentifier={}]", count.incrementAndGet(), surfaceIdentifier);

        gugikCemeteryGeometryRepository.findByIdIipStartingWith(surfaceIdentifier)
                .map(this::processGugikGeometry)
                .map(addressDtoMapper::toCemeteryAddressEntity)
                .ifPresent(cemetery::setLocationAddress);

        log.debug("Processed cementery address [address={}]", cemetery.getLocationAddress());
    }

    private AddressDto processGugikGeometry(GugikCemeteryGeometryEntity gugikCemeteryGeometryEntity) {
        var gugikAddress = gugikCemeteryGeometryEntity.getAddress();
        log.debug("Processing gugikAddress [address={}]", gugikAddress);
        return gugikAddressDtoMapper.toAddressDto(gugikAddress);
    }

}
