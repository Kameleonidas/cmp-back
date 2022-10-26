package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import pl.gov.cmp.auth.model.dto.UserAccountToSubjectDto;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.cemetery.service.CemeteryService;
import pl.gov.cmp.exception.InconsistentDataException;

import java.util.Optional;

import static java.lang.String.format;
import static pl.gov.cmp.auth.model.enums.ObjectCategoryEnum.*;

@Service
@RequiredArgsConstructor
public class InstitutionResolver {

    private final CemeteryService cemeteryService;

    public Pair<Long, String> getInstitutionData(UserAccountToSubjectDto dto) {
        final var institutionType = dto.getCategory();
        if (CEMETERY == institutionType) {
            return getCemeteryName(dto.getCemeteryId(), institutionType);
        }
        return null;
    }

    private Pair<Long, String> getCemeteryName(Long cemeteryId, ObjectCategoryEnum institutionType) {
        return Optional.ofNullable(cemeteryId).map(cemeteryService::getCemetery).map(dto -> Pair.of(dto.getId(), dto.getName()))
                .orElseThrow(() -> new InconsistentDataException(format("cemeteryId is missing for record with institution type: %s", institutionType)));
    }


}
