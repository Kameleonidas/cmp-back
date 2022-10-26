package pl.gov.cmp.cemetery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.mapper.CemeteryMapper;
import pl.gov.cmp.cemetery.model.mapper.CemeteryMapperImpl;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CemeteryInformationGetterTest {

    @Mock
    private CemeteryRepository cemeteryRepositoryMock;

    private final CemeteryMapper cemeteryMapper = new CemeteryMapperImpl();

    private CemeteryInformationGetter informationGetter;

    @BeforeEach
    void setUp() {
        informationGetter = new CemeteryInformationGetter(cemeteryRepositoryMock, cemeteryMapper);
    }

    @Test
    void shouldGetCemeteryInformationCorrectly() {
        // given
        given(cemeteryRepositoryMock.getById(ArgumentMatchers.anyLong())).willReturn(
                CemeteryEntity
                        .builder()
                        .name("niezarejestrowany cmentarz")
                        .description("Testowy cmentarz we wsi Testowo")
                        .build()
        );

        // when
        var informationDto = informationGetter.getInformation(1234L);

        // then
        then(cemeteryRepositoryMock).should().getById(1234L);
        assertThat(informationDto.getName()).isEqualTo("niezarejestrowany cmentarz");
    }
}