package pl.gov.cmp.dictionary.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.dictionary.model.dto.ChurchReligionElementDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryCemeteryOwnerCategoryDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryElementDto;
import pl.gov.cmp.dictionary.model.entity.*;

import java.util.List;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DictionaryMapper {

    List<DictionaryElementDto> toCemeteryTypes(List<CemeteryTypeDictionaryEntity> dictionaryElements);

    List<DictionaryElementDto> toCemeteryFacilityTypes(List<CemeteryFacilityTypeDictionaryEntity> dictionaryElements);

    List<ChurchReligionElementDto> toChurchReligions(List<ChurchReligionDictionaryEntity> dictionaryElements);

    List<DictionaryCemeteryOwnerCategoryDto> toCemeteryOwnerCategories(List<CemeteryOwnerCategoryDictionaryEntity> dictionaryElements);

    List<DictionaryElementDto> toCemeterySources(List<CemeterySourceDictionaryEntity> dictionaryElements);

    List<DictionaryElementDto> toUserAccountStatuses(List<UserAccountStatusDictionaryEntity> dictionaryElements);

    DictionaryElementDto toDictionaryElementDto(CemeteryTypeDictionaryEntity dictionaryElement);

    DictionaryElementDto toDictionaryElementDto(CemeteryFacilityTypeDictionaryEntity dictionaryElement);

    ChurchReligionElementDto toDictionaryElementDto(ChurchReligionDictionaryEntity dictionaryElement);

    DictionaryElementDto toDictionaryElementDto(CemeterySourceDictionaryEntity dictionaryElement);

    DictionaryElementDto toDictionaryElementDto(UserAccountStatusDictionaryEntity dictionaryElement);

    DictionaryElementDto toDictionaryElementDto(CemeteryOwnerCategoryDictionaryEntity dictionaryElement);

}
