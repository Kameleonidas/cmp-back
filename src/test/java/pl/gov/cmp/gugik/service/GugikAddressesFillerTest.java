package pl.gov.cmp.gugik.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import pl.gov.cmp.cemetery.service.CemeteryAddressFiller;
import pl.gov.cmp.gugik.model.dto.AddressDto;
import pl.gov.cmp.gugik.model.entity.GugikAddressEntity;
import pl.gov.cmp.gugik.model.entity.GugikCemeteryGeometryEntity;
import pl.gov.cmp.gugik.model.mapper.GugikAddressDtoMapper;
import pl.gov.cmp.gugik.model.mapper.GugikAddressDtoMapperImpl;

import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static pl.gov.cmp.gugik.model.enums.CemeteryItemType.CEMETERY_SURFACE;

@ExtendWith(MockitoExtension.class)
class GugikAddressesFillerTest {

    @Mock
    private TercFetcher tercFetcherMock;

    @Mock
    private AddressFetcher addressFetcherMock;

    @Mock
    private CemeteryAddressFiller cemeteryAddressFillerMock;

    private final GugikAddressDtoMapper gugikAddressDtoMapper = new GugikAddressDtoMapperImpl();

    private GugikAddressesFiller gugikAddressesFiller;

    @BeforeEach
    public void setUp() {
        gugikAddressesFiller = new GugikAddressesFiller(tercFetcherMock, addressFetcherMock, cemeteryAddressFillerMock,
                gugikAddressDtoMapper);
    }

    @Test
    void shouldProcessCemeteryGeometryCorrectly() throws ParseException {
        // given
        var geometry = GugikCemeteryGeometryEntity
                .builder()
                .id(1L)
                .type(CEMETERY_SURFACE)
                .addressPoint((Point) new WKTReader().read("Point (1 1)"))
                .idIip("first_11_test")
                .build();
        given(tercFetcherMock.fetch(any(Point.class))).willReturn(Optional.of(AddressDto.builder()
                .voivodeship("voivodeship")
                .voivodeshipTercCode("voivodeshipTercCode")
                .district("district")
                .districtTercCode("districtTercCode")
                .commune("commune")
                .communeTercCode("communeTercCode")
                .build()));
        given(addressFetcherMock.fillAddressPart(any(Point.class), any(AddressDto.class)))
                .willAnswer(prepareSimcPartInAddressEntityWithIdFromAddressPoint());

        // when
        var result = gugikAddressesFiller.processCemeteryGeometry(geometry);

        // then
        final var addressDto = AddressDto.builder()
                .voivodeship("voivodeship")
                .voivodeshipTercCode("voivodeshipTercCode")
                .district("district")
                .districtTercCode("districtTercCode")
                .commune("commune")
                .communeTercCode("communeTercCode")
                .place("place11")
                .placeSimcCode("placeSimcCode11")
                .build();
        assertThat(result).isPresent().contains(addressDto);
    }

    @Test
    void shouldNotProcessProcessAddressWhenNoTercAddress() throws ParseException {
        // given
        var geometry = GugikCemeteryGeometryEntity
                .builder()
                .id(1L)
                .type(CEMETERY_SURFACE)
                .addressPoint((Point) new WKTReader().read("Point (1 1)"))
                .idIip("first_11_test")
                .build();
        given(tercFetcherMock.fetch(any(Point.class))).willReturn(Optional.empty());

        // when
        var result = gugikAddressesFiller.processCemeteryGeometry(geometry);

        // then
        assertThat(result).isEmpty();
        then(addressFetcherMock).shouldHaveNoInteractions();
    }

    @Test
    void shouldFillAddressPointFromGeometryWhenEmpty() throws ParseException {
        // given
        var givenGeometry = new WKTReader().read(
                "MULTIPOLYGON (((476291.77 719713.06, 476327.98 719680.27, 476326.96 719668.49, " +
                        "476318.51 719655.95, 476312.74 719641.77, 476310.41 719625.54, 476314.94 719601.09, " +
                        "476322.06 719560.42, 476306.91 719559.53, 476304.75 719500.56, 476304.63 719496.65, " +
                        "476273.7 719499.43, 476240.18 719504.27, 476238.64 719495.43, 476236.03 719476.19, " +
                        "476224.38 719460.37, 476144.62 719472.02, 476156.11 719517.87, 476165.62 719553.66, " +
                        "476169.19 719588.24, 476215.31 719594.32, 476222.05 719653.33, 476225.59 719684.36, " +
                        "476254.3 719684.77, 476289.52 719685.15, 476291.77 719713.06)))");

        var expectedAddressPoint = (Point) new WKTReader().read("POINT (476244.1087095159 719573.3250216953)");

        var geometryToProcess = GugikCemeteryGeometryEntity.builder()
                .id(12L)
                .type(CEMETERY_SURFACE)
                .geometry(givenGeometry)
                .build();

        given(tercFetcherMock.fetch(any(Point.class))).willReturn(Optional.empty());

        // when
        var result = gugikAddressesFiller.processCemeteryGeometry(geometryToProcess);

        // then
        assertThat(result).isEmpty();
        assertThat(geometryToProcess.getAddressPoint()).isEqualTo(expectedAddressPoint);
    }

    @Test
    void shouldProcessFetchedAddressCorrectly() {
        // given
        var cemeteryGeometry = GugikCemeteryGeometryEntity.builder().idIip("test_identifier").build();
        var addressDto = AddressDto.builder()
                .voivodeship("WIELKOPOLSKIE")
                .voivodeshipTercCode("30")
                .district("średzki")
                .districtTercCode("3025")
                .commune("Środa Wielkopolska")
                .communeTercCode("3025045")
                .place("Nietrzanowo")
                .placeSimcCode("0597009")
                .build();
        assertThat(cemeteryGeometry.getAddress()).isNull();

        // when
        gugikAddressesFiller.processFetchedAddress(cemeteryGeometry, addressDto);

        // then
        assertThat(cemeteryGeometry.getAddress()).isEqualTo(GugikAddressEntity.builder()
                .voivodeship("WIELKOPOLSKIE")
                .voivodeshipTercCode("30")
                .district("średzki")
                .districtTercCode("3025")
                .commune("Środa Wielkopolska")
                .communeTercCode("3025045")
                .place("Nietrzanowo")
                .placeSimcCode("0597009")
                .build());
        then(cemeteryAddressFillerMock).should().fill("test_identifier", addressDto);
    }

    private Answer<AddressDto> prepareSimcPartInAddressEntityWithIdFromAddressPoint() {
        return invocationOnMock -> {
            Point argument = invocationOnMock.getArgument(0);
            AddressDto address = invocationOnMock.getArgument(1);
            var idFromCoordinates = format("%d%d", (int) argument.getCoordinate().getX(),
                    (int) argument.getCoordinate().getY());
            address.setPlace("place" + idFromCoordinates);
            address.setPlaceSimcCode("placeSimcCode" + idFromCoordinates);
            return address;
        };
    }

}
