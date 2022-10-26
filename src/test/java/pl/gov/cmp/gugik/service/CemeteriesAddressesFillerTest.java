package pl.gov.cmp.gugik.service;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import pl.gov.cmp.cemetery.service.CemeteryAddressesChecker;
import pl.gov.cmp.gugik.model.dto.AddressDto;
import pl.gov.cmp.gugik.model.entity.GugikCemeteryGeometryEntity;
import pl.gov.cmp.gugik.repository.GugikCemeteryGeometryRepository;
import pl.gov.cmp.process.model.enums.ProcessCode;
import pl.gov.cmp.process.service.ProcessService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static pl.gov.cmp.gugik.model.enums.CemeteryItemType.CEMETERY_SURFACE;
import static pl.gov.cmp.process.model.enums.ProcessCode.CEMETERIES_ADDRESSES_FILLER;

@ExtendWith(MockitoExtension.class)
class CemeteriesAddressesFillerTest {

    @Mock
    private GugikCemeteryGeometryRepository gugikCemeteryGeometryRepositoryMock;

    @Mock
    private GugikAddressesFiller gugikAddressesFillerMock;

    @Mock
    private ProcessService processServiceMock;

    @Mock
    private CemeteryAddressesChecker cemeteryAddressesCheckerMock;

    private CemeteriesAddressesFiller cemeteriesAddressesFiller;

    @BeforeEach
    void setUp() {
        cemeteriesAddressesFiller = new CemeteriesAddressesFiller(
                gugikCemeteryGeometryRepositoryMock,
                gugikAddressesFillerMock,
                processServiceMock,
                cemeteryAddressesCheckerMock);
    }

    @Test
    void shouldNotProcessAddressFillWhenCantChangeStateToIsRunning() {
        // given
        given(processServiceMock.changeProcessToIsRunningState(any(ProcessCode.class))).willReturn(false);

        // when
        cemeteriesAddressesFiller.process();

        // then
        then(gugikCemeteryGeometryRepositoryMock).should().countCemeteriesWithoutAddresses();
        then(gugikCemeteryGeometryRepositoryMock).shouldHaveNoMoreInteractions();
        then(gugikAddressesFillerMock).shouldHaveNoInteractions();
    }

    @Test
    void shouldNotProcessAddressFillWhenErrorDuringChangeStateToIsRunning() {
        // given
        given(processServiceMock.changeProcessToIsRunningState(any(ProcessCode.class)))
                .willThrow(new NonUniqueObjectException(1L, ProcessCode.class.getName()));

        // when
        cemeteriesAddressesFiller.process();

        // then
        then(gugikCemeteryGeometryRepositoryMock).should().countCemeteriesWithoutAddresses();
        then(gugikCemeteryGeometryRepositoryMock).shouldHaveNoMoreInteractions();
        then(gugikAddressesFillerMock).shouldHaveNoInteractions();
    }

    @Test
    void shouldProcessAddressFillWhenCanChangeStateToIsRunning() throws ParseException {
        // given
        List<GugikCemeteryGeometryEntity> geometriesToProcess = prepareGugikCemeteryGeometries();
        given(processServiceMock.changeProcessToIsRunningState(any(ProcessCode.class))).willReturn(true);
        given(gugikCemeteryGeometryRepositoryMock.findCemeteriesWithoutAddresses())
                .willReturn(geometriesToProcess);
        given(gugikAddressesFillerMock.processCemeteryGeometry(any(GugikCemeteryGeometryEntity.class)))
                .willAnswer(prepareAddressDtoAnswerWithIdFromAddressPoint());

        // when
        cemeteriesAddressesFiller.process();

        // then
        then(gugikAddressesFillerMock).should(times(4)).processCemeteryGeometry(any(GugikCemeteryGeometryEntity.class));
        then(gugikAddressesFillerMock).should(times(4)).processFetchedAddress(any(GugikCemeteryGeometryEntity.class),
                any(AddressDto.class));
        then(cemeteryAddressesCheckerMock).should().checkCemeteries();
        then(processServiceMock).should().markProcessAsWasRun(CEMETERIES_ADDRESSES_FILLER);
    }

    @Test
    void shouldSetWasNotRunStateWhenErrorDuringProcess() {
        // given
        given(processServiceMock.changeProcessToIsRunningState(any(ProcessCode.class)))
                .willReturn(true);
        given(gugikCemeteryGeometryRepositoryMock.findCemeteriesWithoutAddresses())
                .willThrow(new SQLGrammarException("Test Exception", new SQLException()));

        // when
        cemeteriesAddressesFiller.process();

        // then
        then(gugikAddressesFillerMock).shouldHaveNoInteractions();
        then(processServiceMock).should().markProcessAsWasNotRun(CEMETERIES_ADDRESSES_FILLER);
    }

    private Answer<Optional<AddressDto>> prepareAddressDtoAnswerWithIdFromAddressPoint() {
        return invocationOnMock -> {
            GugikCemeteryGeometryEntity argument = invocationOnMock.getArgument(0);
            var idFromCoordinates = format("%d%d", (int) argument.getAddressPoint().getCoordinate().getX(),
                    (int) argument.getAddressPoint().getCoordinate().getY());
            return Optional.of(AddressDto.builder()
                    .voivodeship("voivodeship" + idFromCoordinates)
                    .voivodeshipTercCode("voivodeshipTercCode" + idFromCoordinates)
                    .district("district" + idFromCoordinates)
                    .districtTercCode("districtTercCode" + idFromCoordinates)
                    .commune("commune" + idFromCoordinates)
                    .communeTercCode("communeTercCode" + idFromCoordinates)
                    .place("place" + idFromCoordinates)
                    .placeSimcCode("placeSimcCode" + idFromCoordinates)
                    .build());
        };
    }

    private List<GugikCemeteryGeometryEntity> prepareGugikCemeteryGeometries() throws ParseException {
        WKTReader wktReader = new WKTReader();
        return asList(GugikCemeteryGeometryEntity
                        .builder()
                        .id(1L)
                        .type(CEMETERY_SURFACE)
                        .addressPoint((Point) wktReader.read("Point (1 1)"))
                        .idIip("first_11_test")
                        .build(),
                GugikCemeteryGeometryEntity
                        .builder()
                        .id(2L)
                        .type(CEMETERY_SURFACE)
                        .addressPoint((Point) wktReader.read("Point (2 1)"))
                        .idIip("second_21_test")
                        .build(),
                GugikCemeteryGeometryEntity
                        .builder()
                        .id(3L)
                        .type(CEMETERY_SURFACE)
                        .addressPoint((Point) wktReader.read("Point (1 2)"))
                        .idIip("third_12_test")
                        .build(),
                GugikCemeteryGeometryEntity
                        .builder()
                        .id(4L)
                        .type(CEMETERY_SURFACE)
                        .addressPoint((Point) wktReader.read("Point (2 2)"))
                        .idIip("fourth_22_test")
                        .build());
    }
}
