package pl.gov.cmp.dictionary.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.dictionary.model.dto.DictionaryElementDto;
import pl.gov.cmp.dictionary.model.entity.*;
import pl.gov.cmp.dictionary.model.mapper.DictionaryMapper;
import pl.gov.cmp.dictionary.repository.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DictionaryServiceTest {

    @Mock
    private CemeteryTypeDictionaryRepository cemeteryTypeRepository;
    @Mock
    private CemeteryFacilityTypeDictionaryRepository cemeteryFacilityTypeRepository;
    @Mock
    private ChurchReligionDictionaryRepository churchReligionRepository;
    @Mock
    private CemeterySourceDictionaryRepository cemeterySourceRepository;
    @Mock
    private CemeteryOwnerCategoryDictionaryRepository cemeteryOwnerCategoryDictionaryRepository;
    @Mock
    private UserAccountStatusDictionaryRepository userAccountStatusDictionaryRepository;
    private final DictionaryMapper dictionaryMapper = Mappers.getMapper(DictionaryMapper.class);

    @InjectMocks
    private DictionaryService dictionaryService;

    @BeforeEach
    void setup() {
        dictionaryService = new DictionaryService(
                cemeteryTypeRepository,
                cemeteryFacilityTypeRepository,
                churchReligionRepository,
                cemeterySourceRepository,
                cemeteryOwnerCategoryDictionaryRepository,
                userAccountStatusDictionaryRepository,
                dictionaryMapper);
    }

    @Test
    void shouldGetCemeteryDictionaryTypes() {
        //given
        final var firstCemeteryType = createCemeteryTypeDictionary(23454L, "3FFG0", "cemetery type A");
        final var secondCemeteryType = createCemeteryTypeDictionary(3453L, "GG87K", "cemetery type B");
        given(cemeteryTypeRepository.findAll()).willReturn(List.of(firstCemeteryType, secondCemeteryType));

        //when
        final var result = dictionaryService.getCemeteryTypes();

        //then
        final var firstReturnedCemeteryType = result.get(0);
        assertThat(firstReturnedCemeteryType.getId()).isEqualTo(firstCemeteryType.getId());
        assertThat(firstReturnedCemeteryType.getCode()).isEqualTo(firstCemeteryType.getCode());
        assertThat(firstReturnedCemeteryType.getName()).isEqualTo(firstCemeteryType.getName());
        final var secondReturnedCemeteryType = result.get(1);
        assertThat(secondReturnedCemeteryType.getId()).isEqualTo(secondCemeteryType.getId());
        assertThat(secondReturnedCemeteryType.getCode()).isEqualTo(secondCemeteryType.getCode());
        assertThat(secondReturnedCemeteryType.getName()).isEqualTo(secondCemeteryType.getName());
    }

    private CemeteryTypeDictionaryEntity createCemeteryTypeDictionary(long id, String code, String name) {
        final var cemeteryTypeDictionary = new CemeteryTypeDictionaryEntity();
        cemeteryTypeDictionary.setId(id);
        cemeteryTypeDictionary.setCode(code);
        cemeteryTypeDictionary.setName(name);
        return cemeteryTypeDictionary;
    }

    @Test
    void shouldGetCemeteryFacilityDictionaryTypes() {
        //given
        final var firstCemeteryFacilityType = createCemeteryFacilityTypeDictionary(23454L, "3FFG0", "cemetery facility type A");
        final var secondCemeteryFacilityType = createCemeteryFacilityTypeDictionary(3453L, "GG87K", "cemetery facility type B");
        given(cemeteryFacilityTypeRepository.findAll()).willReturn(List.of(firstCemeteryFacilityType, secondCemeteryFacilityType));

        //when
        final var result = dictionaryService.getCemeteryFacilityTypes();

        //then
        final var firstReturnedCemeteryFacilityType = result.get(0);
        assertThat(firstReturnedCemeteryFacilityType.getId()).isEqualTo(firstCemeteryFacilityType.getId());
        assertThat(firstReturnedCemeteryFacilityType.getCode()).isEqualTo(firstCemeteryFacilityType.getCode());
        assertThat(firstReturnedCemeteryFacilityType.getName()).isEqualTo(firstCemeteryFacilityType.getName());
        final var secondReturnedCemeteryFacilityType = result.get(1);
        assertThat(secondReturnedCemeteryFacilityType.getId()).isEqualTo(secondCemeteryFacilityType.getId());
        assertThat(secondReturnedCemeteryFacilityType.getCode()).isEqualTo(secondCemeteryFacilityType.getCode());
        assertThat(secondReturnedCemeteryFacilityType.getName()).isEqualTo(secondCemeteryFacilityType.getName());
    }

    private CemeteryFacilityTypeDictionaryEntity createCemeteryFacilityTypeDictionary(long id, String code, String name) {
        final var cemeteryFacilityTypeDictionary = new CemeteryFacilityTypeDictionaryEntity();
        cemeteryFacilityTypeDictionary.setId(id);
        cemeteryFacilityTypeDictionary.setCode(code);
        cemeteryFacilityTypeDictionary.setName(name);
        return cemeteryFacilityTypeDictionary;
    }

    @Test
    void shouldGetChurchReligionDictionaries() {
        //given
        final var firstChurchReligionDictionary = createChurchReligionDictionary(23454L, "3FFG0", "cemetery facility type A");
        final var secondChurchReligionDictionary = createChurchReligionDictionary(3453L, "GG87K", "cemetery facility type B");
        given(churchReligionRepository.findAll()).willReturn(List.of(firstChurchReligionDictionary, secondChurchReligionDictionary));

        //when
        final var result = dictionaryService.getChurchesReligions();

        //then
        final var firstReturnedChurchReligionDictionary = result.get(0);
        assertThat(firstReturnedChurchReligionDictionary.getId()).isEqualTo(firstChurchReligionDictionary.getId());
        assertThat(firstReturnedChurchReligionDictionary.getCode()).isEqualTo(firstChurchReligionDictionary.getCode());
        assertThat(firstReturnedChurchReligionDictionary.getName()).isEqualTo(firstChurchReligionDictionary.getName());
        assertThat(firstReturnedChurchReligionDictionary.isRegulatedByLaw()).isEqualTo(firstChurchReligionDictionary.isRegulatedByLaw());

        final var secondReturnedChurchReligionDictionary = result.get(1);
        assertThat(secondReturnedChurchReligionDictionary.getId()).isEqualTo(secondChurchReligionDictionary.getId());
        assertThat(secondReturnedChurchReligionDictionary.getCode()).isEqualTo(secondChurchReligionDictionary.getCode());
        assertThat(secondReturnedChurchReligionDictionary.getName()).isEqualTo(secondChurchReligionDictionary.getName());
        assertThat(secondReturnedChurchReligionDictionary.isRegulatedByLaw()).isEqualTo(secondChurchReligionDictionary.isRegulatedByLaw());
    }

    private ChurchReligionDictionaryEntity createChurchReligionDictionary(long id, String code, String name) {
        final var churchReligionDictionary = new ChurchReligionDictionaryEntity();
        churchReligionDictionary.setId(id);
        churchReligionDictionary.setCode(code);
        churchReligionDictionary.setName(name);
        churchReligionDictionary.setRegulatedByLaw(true);
        return churchReligionDictionary;
    }

    @Test
    void shouldGetCemeterySourcesDictionaries() {
        //given
        final var firstCemeterySourceDictionary = createCemeterySourceDictionary(23454L, "3FFG0", "cemetery facility type A");
        final var secondCemeterySourceDictionary = createCemeterySourceDictionary(3453L, "GG87K", "cemetery facility type B");
        given(cemeterySourceRepository.findAll()).willReturn(List.of(firstCemeterySourceDictionary, secondCemeterySourceDictionary));

        //when
        final var result = dictionaryService.getCemeterySources();

        //then
        final var firstReturnedCemeterySourceDictionary = result.get(0);
        assertThat(firstReturnedCemeterySourceDictionary.getId()).isEqualTo(firstCemeterySourceDictionary.getId());
        assertThat(firstReturnedCemeterySourceDictionary.getCode()).isEqualTo(firstCemeterySourceDictionary.getCode());
        assertThat(firstReturnedCemeterySourceDictionary.getName()).isEqualTo(firstCemeterySourceDictionary.getName());
        final var secondReturnedCemeterySourceDictionary = result.get(1);
        assertThat(secondReturnedCemeterySourceDictionary.getId()).isEqualTo(secondCemeterySourceDictionary.getId());
        assertThat(secondReturnedCemeterySourceDictionary.getCode()).isEqualTo(secondCemeterySourceDictionary.getCode());
        assertThat(secondReturnedCemeterySourceDictionary.getName()).isEqualTo(secondCemeterySourceDictionary.getName());
    }

    private CemeterySourceDictionaryEntity createCemeterySourceDictionary(long id, String code, String name) {
        final var cemeterySourceDictionary = new CemeterySourceDictionaryEntity();
        cemeterySourceDictionary.setId(id);
        cemeterySourceDictionary.setCode(code);
        cemeterySourceDictionary.setName(name);
        return cemeterySourceDictionary;
    }

    @Test
    void shouldGetCemeteryOwnerCategoryDictionaries() {
        //given
        final var firstCemeteryOwnerCategoryDictionary = createCemeteryOwnerCategoryDictionary(23454L, "3FFG0", "cemetery facility type A", 346365L);
        final var secondCemeteryOwnerCategoryDictionary = createCemeteryOwnerCategoryDictionary(3453L, "GG87K", "cemetery facility type B", 36546L);
        given(cemeteryOwnerCategoryDictionaryRepository.findAll()).willReturn(List.of(firstCemeteryOwnerCategoryDictionary, secondCemeteryOwnerCategoryDictionary));

        //when
        final var result = dictionaryService.getCemeteryOwnerCategories();

        //then
        final var firstReturnedCemeteryOwnerCategoryDictionary = result.get(0);
        assertThat(firstReturnedCemeteryOwnerCategoryDictionary.getId()).isEqualTo(firstCemeteryOwnerCategoryDictionary.getId());
        assertThat(firstReturnedCemeteryOwnerCategoryDictionary.getCode()).isEqualTo(firstCemeteryOwnerCategoryDictionary.getCode());
        assertThat(firstReturnedCemeteryOwnerCategoryDictionary.getName()).isEqualTo(firstCemeteryOwnerCategoryDictionary.getName());
        assertThat(firstReturnedCemeteryOwnerCategoryDictionary.isPerpetualUse()).isEqualTo(firstCemeteryOwnerCategoryDictionary.isPerpetualUse());
        assertThat(firstReturnedCemeteryOwnerCategoryDictionary.getParentId()).isEqualTo(firstCemeteryOwnerCategoryDictionary.getParentId());

        final var secondReturnedCemeteryOwnerCategoryDictionary = result.get(1);
        assertThat(secondReturnedCemeteryOwnerCategoryDictionary.getId()).isEqualTo(secondCemeteryOwnerCategoryDictionary.getId());
        assertThat(secondReturnedCemeteryOwnerCategoryDictionary.getCode()).isEqualTo(secondCemeteryOwnerCategoryDictionary.getCode());
        assertThat(secondReturnedCemeteryOwnerCategoryDictionary.getName()).isEqualTo(secondCemeteryOwnerCategoryDictionary.getName());
        assertThat(secondReturnedCemeteryOwnerCategoryDictionary.isPerpetualUse()).isEqualTo(secondCemeteryOwnerCategoryDictionary.isPerpetualUse());
        assertThat(secondReturnedCemeteryOwnerCategoryDictionary.getParentId()).isEqualTo(secondCemeteryOwnerCategoryDictionary.getParentId());
    }

    private CemeteryOwnerCategoryDictionaryEntity createCemeteryOwnerCategoryDictionary(long id, String code, String name, long partnerId) {
        final var cemeteryOwnerCategoryDictionary = new CemeteryOwnerCategoryDictionaryEntity();
        cemeteryOwnerCategoryDictionary.setId(id);
        cemeteryOwnerCategoryDictionary.setCode(code);
        cemeteryOwnerCategoryDictionary.setName(name);
        cemeteryOwnerCategoryDictionary.setPerpetualUse(true);
        cemeteryOwnerCategoryDictionary.setParentId(partnerId);
        return cemeteryOwnerCategoryDictionary;
    }

    @Test
    void shouldGetUserAccountStatusDictionaries() {
        //given
        final var firstUserAccountStatusDictionary = createUserAccountStatusDictionary(23454L, "3FFG0", "cemetery facility type A");
        final var secondUserAccountStatusDictionary = createUserAccountStatusDictionary(3453L, "GG87K", "cemetery facility type B");
        given(userAccountStatusDictionaryRepository.findAll()).willReturn(List.of(firstUserAccountStatusDictionary, secondUserAccountStatusDictionary));

        //when
        final var result = dictionaryService.getUserAccountStatuses();

        //then
        final var firstReturnedUserAccountStatusDictionary = result.get(0);
        assertThat(firstReturnedUserAccountStatusDictionary.getId()).isEqualTo(firstUserAccountStatusDictionary.getId());
        assertThat(firstReturnedUserAccountStatusDictionary.getCode()).isEqualTo(firstUserAccountStatusDictionary.getCode());
        assertThat(firstReturnedUserAccountStatusDictionary.getName()).isEqualTo(firstUserAccountStatusDictionary.getName());
        final var secondReturnedUserAccountStatusDictionary = result.get(1);
        assertThat(secondReturnedUserAccountStatusDictionary.getId()).isEqualTo(secondUserAccountStatusDictionary.getId());
        assertThat(secondReturnedUserAccountStatusDictionary.getCode()).isEqualTo(secondUserAccountStatusDictionary.getCode());
        assertThat(secondReturnedUserAccountStatusDictionary.getName()).isEqualTo(secondUserAccountStatusDictionary.getName());
    }

    private UserAccountStatusDictionaryEntity createUserAccountStatusDictionary(long id, String code, String name) {
        final var cemeterySourceDictionary = new UserAccountStatusDictionaryEntity();
        cemeterySourceDictionary.setId(id);
        cemeterySourceDictionary.setCode(code);
        cemeterySourceDictionary.setName(name);
        return cemeterySourceDictionary;
    }
}