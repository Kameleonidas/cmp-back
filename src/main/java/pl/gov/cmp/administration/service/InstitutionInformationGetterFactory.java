package pl.gov.cmp.administration.service;

import com.google.common.base.Functions;
import org.springframework.stereotype.Component;
import pl.gov.cmp.administration.model.enums.InstitutionType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class InstitutionInformationGetterFactory {

    private final Map<InstitutionType, InstitutionInformationGetter> informationGetterMap;

    public InstitutionInformationGetterFactory(List<InstitutionInformationGetter> institutionInformationGetters) {
        informationGetterMap = institutionInformationGetters.stream().collect(Collectors.toMap(
                InstitutionInformationGetter::forType,
                Functions.identity()
        ));
    }

    InstitutionInformationGetter findStrategy(InstitutionType institutionType) {
        return Optional.ofNullable(informationGetterMap.get(institutionType))
                .orElseThrow(() -> new IllegalStateException(
                        format("Lack InstitutionInformationGetter implementation for '%s' institutionType.", institutionType)));
    }
}
