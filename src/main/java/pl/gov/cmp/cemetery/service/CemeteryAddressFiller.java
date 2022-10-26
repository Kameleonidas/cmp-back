package pl.gov.cmp.cemetery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.cemetery.model.entity.CemeteryGeometryEntity;
import pl.gov.cmp.cemetery.model.mapper.AddressDtoMapper;
import pl.gov.cmp.cemetery.repository.CemeteryGeometryRepository;
import pl.gov.cmp.gugik.model.dto.AddressDto;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class CemeteryAddressFiller {

    private final CemeteryGeometryRepository cemeteryGeometryRepository;
    private final AddressDtoMapper addressDtoMapper;

    public void fill(String idIipIdentifier, AddressDto addressDto) {
        cemeteryGeometryRepository.findByIdIipIdentifier(idIipIdentifier).ifPresent(cemeteryGeometry ->
                mapAddressAndConnectWithCemetery(addressDto, cemeteryGeometry));
    }

    private void mapAddressAndConnectWithCemetery(AddressDto addressDto, CemeteryGeometryEntity cemeteryGeometry) {
        var cemetery = cemeteryGeometry.getCemetery();
        var mappedAddress = addressDtoMapper.toCemeteryAddressEntity(addressDto);
        cemetery.setLocationAddress(mappedAddress);
        log.debug("Filled cemeteryAddress [cemetery={}, addressDto={}, mappedAddress={}]",
                cemeteryGeometry, addressDto, mappedAddress);
    }
}
