package pl.gov.cmp.cemetery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.cemetery.model.entity.CemeteryAddressEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryGeometryEntity;
import pl.gov.cmp.cemetery.model.mapper.AddressDtoMapperImpl;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;
import pl.gov.cmp.gugik.model.entity.GugikAddressEntity;
import pl.gov.cmp.gugik.model.entity.GugikCemeteryGeometryEntity;
import pl.gov.cmp.gugik.model.mapper.GugikAddressDtoMapper;
import pl.gov.cmp.gugik.model.mapper.GugikAddressDtoMapperImpl;
import pl.gov.cmp.gugik.repository.GugikCemeteryGeometryRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CemeteryAddressesCheckerTest {

    @Mock
    private CemeteryRepository cemeteryRepositoryMock;

    @Mock
    private GugikCemeteryGeometryRepository gugikCemeteryGeometryRepositoryMock;

    private final GugikAddressDtoMapper gugikAddressDtoMapper = new GugikAddressDtoMapperImpl();

    private final pl.gov.cmp.cemetery.model.mapper.AddressDtoMapper addressDtoMapper = new AddressDtoMapperImpl();

    private CemeteryAddressesChecker cemeteryAddressesChecker;

    @BeforeEach
    void setUp() {
        cemeteryAddressesChecker = new CemeteryAddressesChecker(
                cemeteryRepositoryMock,
                gugikCemeteryGeometryRepositoryMock,
                gugikAddressDtoMapper,
                addressDtoMapper
        );
    }

    @Test
    void shouldProcessAddressCheckCorrectly() {
        // given
        CemeteryEntity cemeteryEntity = prepareCemeteryEntityList();
        given(cemeteryRepositoryMock.findByLocationAddressIsNull()).willReturn(List.of(cemeteryEntity));
        given(gugikCemeteryGeometryRepositoryMock.findByIdIipStartingWith(anyString()))
                .willReturn(prepareGugikCemeteryGeometry());

        // when
        cemeteryAddressesChecker.checkCemeteries();

        // then
        assertEquals(prepareExpectedCemeteryAddress(), cemeteryEntity.getLocationAddress());
    }

    @Test
    void shouldProcessAddressCheckWhenGugikCemeteryNotFound() {
        // given
        CemeteryEntity cemeteryEntity = prepareCemeteryEntityList();
        given(cemeteryRepositoryMock.findByLocationAddressIsNull()).willReturn(List.of(cemeteryEntity));
        given(gugikCemeteryGeometryRepositoryMock.findByIdIipStartingWith(anyString())).willReturn(Optional.empty());

        // when
        cemeteryAddressesChecker.checkCemeteries();

        // then
        assertNull(cemeteryEntity.getLocationAddress());
    }

    @Test
    void shouldProcessAddressCheckWhenCemeteriesWithoutAddressNotFound() {
        // given
        given(cemeteryRepositoryMock.findByLocationAddressIsNull()).willReturn(List.of());

        // when
        cemeteryAddressesChecker.checkCemeteries();

        // then
        assertTrue(true);
    }

    private CemeteryAddressEntity prepareExpectedCemeteryAddress() {
        return CemeteryAddressEntity.builder()
                .voivodeship("vo")
                .voivodeshipTercCode("vtc")
                .district("dis")
                .districtTercCode("dtc")
                .commune("com")
                .communeTercCode("ctc")
                .place("somewhere in poland")
                .placeSimcCode("psc")
                .build();
    }

    private Optional<GugikCemeteryGeometryEntity> prepareGugikCemeteryGeometry() {
        return Optional.of(GugikCemeteryGeometryEntity.builder()
                .address(GugikAddressEntity.builder()
                        .voivodeship("vo")
                        .voivodeshipTercCode("vtc")
                        .district("dis")
                        .districtTercCode("dtc")
                        .commune("com")
                        .communeTercCode("ctc")
                        .place("somewhere in poland")
                        .placeSimcCode("psc")
                        .build())
                .build());
    }

    private CemeteryEntity prepareCemeteryEntityList() {
        return
            CemeteryEntity.builder()
                .cemeteryGeometry(
                        CemeteryGeometryEntity.builder().idIipIdentifier("test_1234").build()
                ).build();
    }
}