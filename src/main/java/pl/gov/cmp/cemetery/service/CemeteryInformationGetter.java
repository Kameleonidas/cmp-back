package pl.gov.cmp.cemetery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.gov.cmp.administration.model.dto.InstitutionInformationDto;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.service.InstitutionInformationGetter;
import pl.gov.cmp.cemetery.model.mapper.CemeteryMapper;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;

@RequiredArgsConstructor
@Component
public class CemeteryInformationGetter implements InstitutionInformationGetter {

    private final CemeteryRepository cemeteryRepository;

    private final CemeteryMapper cemeteryMapper;

    @Override
    public InstitutionInformationDto getInformation(Long institutionId) {
        var cemetery = cemeteryRepository.getById(institutionId);
        return cemeteryMapper.toEntityDto(cemetery);
    }

    @Override
    public InstitutionType forType() {
        return InstitutionType.CEMETERY;
    }
}
