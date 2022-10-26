package pl.gov.cmp.gugik.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.gov.cmp.gugik.model.dto.AddressDto;

class GugikAddressesResponseParserTest {

    private final GugikAddressesResponseParser gugikAddressesResponseParser = new GugikAddressesResponseParser();

    private static Stream<Arguments> provideAddressStrings() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of("-1 błąd usługi", null),
                Arguments.of(
                        "0\n160701_5.0009.358|opolskie|nyski|Głuchołazy",
                        AddressDto.builder()
                                .voivodeship("opolskie")
                                .voivodeshipTercCode("16")
                                .district("nyski")
                                .districtTercCode("1607")
                                .commune("Głuchołazy")
                                .communeTercCode("1607015")
                                .build()),
                Arguments.of(
                        "0\n302504_5.0017.97/1|wielkopolskie|średzki|Środa Wielkopolska",
                        AddressDto.builder()
                                .voivodeship("wielkopolskie")
                                .voivodeshipTercCode("30")
                                .district("średzki")
                                .districtTercCode("3025")
                                .commune("Środa Wielkopolska")
                                .communeTercCode("3025045")
                                .build()),
                Arguments.of(
                        "0\n"
                                + "260413_5.0004.241|świętokrzyskie||Nowa Słupia"
                                + "\n"
                                + "260413_2.0004.241|świętokrzyskie|kielecki|Nowa Słupia||",
                        AddressDto.builder()
                                .voivodeship("świętokrzyskie")
                                .voivodeshipTercCode("26")
                                .district("")
                                .districtTercCode("2604")
                                .commune("Nowa Słupia")
                                .communeTercCode("2604135")
                                .build()));
    }

    @ParameterizedTest
    @MethodSource("provideAddressStrings")
    void shouldParseAddressesFromService(String address, AddressDto expectedResult) {

        // when
        var result = gugikAddressesResponseParser.parse(address);

        // then
        assertEquals(Optional.ofNullable(expectedResult), result);
    }
}
