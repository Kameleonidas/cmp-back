package pl.gov.cmp.gugik.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import pl.gov.cmp.gugik.configuration.GugikTercServiceConfiguration;
import pl.gov.cmp.gugik.model.dto.AddressDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AddressFetcherTest {

    @Mock
    private RestTemplate restTemplateMock;

    @Mock
    private GugikTercServiceConfiguration gugikTercServiceConfigurationMock;

    @Mock
    private GugikAddressesResponseParser gugikAddressesResponseParserMock;

    private TercFetcher tercFetcher;

    @BeforeEach
    void setUp() {
        tercFetcher = new TercFetcher(
                restTemplateMock,
                gugikTercServiceConfigurationMock,
                gugikAddressesResponseParserMock);
    }

    @Test
    void shouldFetchAddressCorrectly() throws ParseException {

        // given
        AddressDto parsedAddress = prepareAddressDto().build();

        given(gugikTercServiceConfigurationMock.getServiceUrlForCoordinate(anyString())).willReturn("http://get.address.pl?xv=123,321");
        given(restTemplateMock.getForObject(anyString(), eq(String.class))).willReturn("0\n123345435_2.324234234.423432324234");
        given(gugikAddressesResponseParserMock.parse(anyString())).willReturn(Optional.of(parsedAddress));

        // when
        var result = tercFetcher.fetch((Point) new WKTReader().read("Point (123 321)"));

        // then
        assertEquals(Optional.of(parsedAddress), result);
        then(gugikTercServiceConfigurationMock).should().getServiceUrlForCoordinate("123.0,321.0");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"84850349750943", "-1\n0\n1212132"})
    void shouldReturnNullWhenWrongResultFromAddressService(String serviceResult) throws ParseException {

        // given
        given(gugikTercServiceConfigurationMock.getServiceUrlForCoordinate(anyString()))
                .willReturn("http://get.address.pl?xv=123,321");
        given(restTemplateMock.getForObject(anyString(), eq(String.class))).willReturn(serviceResult);

        // when
        var result = tercFetcher.fetch((Point) new WKTReader().read("Point (123 321)"));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnNullWhenCannotParseAddressFromService() throws ParseException {

        // given
        given(gugikTercServiceConfigurationMock.getServiceUrlForCoordinate(anyString()))
                .willReturn("http://get.address.pl?xv=123,321");
        given(restTemplateMock.getForObject(anyString(), eq(String.class)))
                .willReturn("0\n123345435_2.324234234.423432324234");
        given(gugikAddressesResponseParserMock.parse(anyString())).willReturn(Optional.empty());

        // when
        var result = tercFetcher.fetch((Point) new WKTReader().read("Point (123 321)"));

        // then
        assertThat(result).isEmpty();
        then(gugikTercServiceConfigurationMock).should().getServiceUrlForCoordinate("123.0,321.0");
    }

    private AddressDto.AddressDtoBuilder prepareAddressDto() {
        return AddressDto.builder().voivodeship("Wlkp").district("Testowy").commune("Testowo");
    }
}
