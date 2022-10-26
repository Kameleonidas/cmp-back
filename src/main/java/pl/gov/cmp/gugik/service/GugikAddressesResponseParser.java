package pl.gov.cmp.gugik.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import pl.gov.cmp.gugik.model.dto.AddressDto;

@Component
class GugikAddressesResponseParser {

    Optional<AddressDto> parse(String result) {

        if (result == null) {
            return Optional.empty();
        }

        var serviceResults = prepareDataToParse(result);
        var newGugikAddressEntity = Optional.<AddressDto>empty();
        if (serviceResults.length >= 4) {
            var addressDtoBuilder = AddressDto.builder()
                    .voivodeship(serviceResults[1])
                    .district(serviceResults[2])
                    .commune(serviceResults[3]);

            newGugikAddressEntity = Optional.of(parseTerytParameter(addressDtoBuilder, serviceResults[0]).build());
        }
        return newGugikAddressEntity;
    }

    private String[] prepareDataToParse(String result) {
        // 0 - service result, [1..n] - addresses
        var resultLines = result.split("\n");
        var correctResultStr = resultLines.length > 1 ? resultLines[1] : resultLines[0];
        return correctResultStr.split("\\|");
    }

    private AddressDto.AddressDtoBuilder parseTerytParameter(
            AddressDto.AddressDtoBuilder gugikAddressEntityBuilder, String terytParam) {

        if (terytParam != null) {
            var terytParts = terytParam.split("\\.");
            if (terytParts[0] != null && terytParts[0].length() == 8) {
                var terc = terytParts[0].replace("_", "");
                gugikAddressEntityBuilder
                        .voivodeshipTercCode(terc.substring(0, 2))
                        .districtTercCode(terc.substring(0, 4))
                        .communeTercCode(terc);
            }
        }
        return gugikAddressEntityBuilder;
    }

}
