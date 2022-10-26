package pl.gov.cmp.administration.service;

import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.administration.model.enums.InstitutionType;

public interface InstitutionInformationGetter {
    InstitutionInformationDto getInformation(Long institutionId);

    InstitutionType forType();
}
