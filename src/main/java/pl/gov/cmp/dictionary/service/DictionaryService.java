package pl.gov.cmp.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.dictionary.model.dto.ChurchReligionElementDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryCemeteryOwnerCategoryDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryElementDto;
import pl.gov.cmp.dictionary.model.mapper.DictionaryMapper;
import pl.gov.cmp.dictionary.repository.*;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DictionaryService {

    private final CemeteryTypeDictionaryRepository cemeteryTypeRepository;
    private final CemeteryFacilityTypeDictionaryRepository cemeteryFacilityTypeRepository;
    private final ChurchReligionDictionaryRepository churchReligionRepository;
    private final CemeterySourceDictionaryRepository cemeterySourceRepository;
    private final CemeteryOwnerCategoryDictionaryRepository cemeteryOwnerCategoryDictionaryRepository;
    private final UserAccountStatusDictionaryRepository userAccountStatusDictionaryRepository;
    private final DictionaryMapper dictionaryMapper;

    public List<DictionaryElementDto> getCemeteryTypes() {
        return this.dictionaryMapper.toCemeteryTypes(this.cemeteryTypeRepository.findAll());
    }

    public List<DictionaryElementDto> getCemeteryFacilityTypes() {
        return this.dictionaryMapper.toCemeteryFacilityTypes(this.cemeteryFacilityTypeRepository.findAll());
    }

    public List<ChurchReligionElementDto> getChurchesReligions() {
        return this.dictionaryMapper.toChurchReligions(this.churchReligionRepository.findAll());
    }

    public List<DictionaryElementDto> getCemeterySources() {
        return this.dictionaryMapper.toCemeterySources(this.cemeterySourceRepository.findAll());
    }

    public List<DictionaryCemeteryOwnerCategoryDto> getCemeteryOwnerCategories() {
        return this.dictionaryMapper.toCemeteryOwnerCategories(this.cemeteryOwnerCategoryDictionaryRepository.findAll());
    }

    public List<DictionaryElementDto> getUserAccountStatuses() {
        return this.dictionaryMapper.toUserAccountStatuses(this.userAccountStatusDictionaryRepository.findAll());
    }
}
