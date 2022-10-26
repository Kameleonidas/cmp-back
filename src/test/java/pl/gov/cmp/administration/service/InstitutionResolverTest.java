package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.auth.model.dto.UserAccountToSubjectDto;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.cemetery.model.dto.CemeteryDto;
import pl.gov.cmp.cemetery.service.CemeteryService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionResolverTest {

    private static final long INSTITUTION_ID = 25345L;
    @Mock
    private CemeteryService cemeteryService;

    @InjectMocks
    private InstitutionResolver institutionResolver;

    @ParameterizedTest
    @EnumSource(value = ObjectCategoryEnum.class, mode = INCLUDE, names = {"CEMETERY"})
    void shouldGetInstitutionData(ObjectCategoryEnum institutionType) {
        //given
        final var institutionName = "Lublin cemetery";
        final var userAccountToSubject = UserAccountToSubjectDto.builder()
                .category(institutionType)
                .cemeteryId(INSTITUTION_ID)
                .build();
        final var cemeteryDto = CemeteryDto.builder().id(INSTITUTION_ID).name(institutionName).build();
        given(cemeteryService.getCemetery(INSTITUTION_ID)).willReturn(cemeteryDto);

        //when
        final var result = institutionResolver.getInstitutionData(userAccountToSubject);

        //then
        assertThat(result.getLeft()).isEqualTo(INSTITUTION_ID);
        assertThat(result.getRight()).isEqualTo(institutionName);
    }

    @ParameterizedTest
    @EnumSource(value = ObjectCategoryEnum.class, mode = EXCLUDE, names = {"CEMETERY"})
    void shouldNotReturnInstitutionDataForNotSupportedInstitutionTypes(ObjectCategoryEnum institutionType) {
        //given
        final var userAccountToSubject = UserAccountToSubjectDto.builder()
                .category(institutionType)
                .cemeteryId(243L)
                .crematoriumId(54L)
                .ipnId(563L)
                .voivodshipId(456L)
                .build();

        //when
        final var result = institutionResolver.getInstitutionData(userAccountToSubject);

        //then
        assertThat(result).isNull();
    }
}