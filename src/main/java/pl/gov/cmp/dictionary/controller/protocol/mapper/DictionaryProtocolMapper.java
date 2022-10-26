package pl.gov.cmp.dictionary.controller.protocol.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.dictionary.controller.protocol.response.*;
import pl.gov.cmp.dictionary.model.dto.ChurchReligionElementDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryCemeteryOwnerCategoryDto;
import pl.gov.cmp.dictionary.model.dto.DictionaryElementDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DictionaryProtocolMapper {

    default DictionaryResponse toDictionaryResponse(List<DictionaryElementDto> dictionaryElements) {
        List<DictionaryElementResponse> elements = dictionaryElements.stream().map(this::toDictionaryElementResponse).collect(Collectors.toList());
        DictionaryResponse response = new DictionaryResponse();
        response.setElements(elements);
        return response;
    }

    default List<CemeteryOwnerCategoryResponse> toCemeteryOwnerCategoryResponse(List<DictionaryCemeteryOwnerCategoryDto> dictionaryElements) {
        return dictionaryElements.stream().map(this::toChurchReligionElementResponse).collect(Collectors.toList());
    }

    default ChurchReligionResponse toChurchesReligionResponse(List<ChurchReligionElementDto> dictionaryElements) {
        List<ChurchReligionElementResponse> elements = dictionaryElements.stream().map(this::toChurchReligionElementResponse).collect(Collectors.toList());
        ChurchReligionResponse response = new ChurchReligionResponse();
        response.setElements(elements);
        return response;
    }

    DictionaryElementResponse toDictionaryElementResponse(DictionaryElementDto dictionaryElement);

    ChurchReligionElementResponse toChurchReligionElementResponse(ChurchReligionElementDto dictionaryElement);

    CemeteryOwnerCategoryResponse toChurchReligionElementResponse(DictionaryCemeteryOwnerCategoryDto dictionaryCemeteryOwnerCategoryDto);

}
