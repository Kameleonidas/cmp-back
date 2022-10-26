package pl.gov.cmp.cemetery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.cemetery.model.entity.CemeteryAddressEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryGeometryEntity;
import pl.gov.cmp.cemetery.model.mapper.AddressDtoMapper;
import pl.gov.cmp.cemetery.model.mapper.AddressDtoMapperImpl;
import pl.gov.cmp.cemetery.repository.CemeteryGeometryRepository;
import pl.gov.cmp.gugik.model.dto.AddressDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CemeteryAddressFillerTest {

    @Mock
    private CemeteryGeometryRepository cemeteryGeometryRepositoryMock;

    private final AddressDtoMapper addressDtoMapper = new AddressDtoMapperImpl();

    private CemeteryAddressFiller cemeteryAddressFiller;

    @BeforeEach
    void setUp() {
        cemeteryAddressFiller = new CemeteryAddressFiller(cemeteryGeometryRepositoryMock, addressDtoMapper);
    }

    @Test
    void shouldFillCemeteryAddress() {
        // given
        CemeteryGeometryEntity cemeteryGeometryEntity = prepareCemeteryEntity();
        given(cemeteryGeometryRepositoryMock.findByIdIipIdentifier(anyString())).willReturn(Optional.of(cemeteryGeometryEntity));

        // when
        cemeteryAddressFiller.fill("test_1123", prepareCemeteryAddress());

        // then
        assertEquals(prepareMappedAddress(), cemeteryGeometryEntity.getCemetery().getLocationAddress());
    }

    @Test
    void shouldNotFillCemeteryAddress() {
        // given
        given(cemeteryGeometryRepositoryMock.findByIdIipIdentifier(anyString())).willReturn(Optional.empty());

        // when
        cemeteryAddressFiller.fill("test_1123", prepareCemeteryAddress());

        // then
        assertTrue(true);
    }

    private AddressDto prepareCemeteryAddress() {
        return AddressDto.builder()
                .voivodeship("vo")
                .voivodeshipTercCode("votc")
                .district("dis")
                .districtTercCode("distc")
                .commune("com")
                .communeTercCode("comtc")
                .place("area")
                .build();
    }

    private CemeteryAddressEntity prepareMappedAddress() {
        return CemeteryAddressEntity.builder()
                .voivodeship("vo")
                .voivodeshipTercCode("votc")
                .district("dis")
                .districtTercCode("distc")
                .commune("com")
                .communeTercCode("comtc")
                .place("area")
                .build();
    }

    private CemeteryGeometryEntity prepareCemeteryEntity() {
        CemeteryGeometryEntity cemeteryGeometryEntity = new CemeteryGeometryEntity();
        cemeteryGeometryEntity.setCemetery(new CemeteryEntity());
        return cemeteryGeometryEntity;
    }
}