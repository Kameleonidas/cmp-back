package pl.gov.cmp.cemetery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryCriteriaDto;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryElementDto;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.mapper.UserCemeteryMapper;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserCemeteryServiceTest {
    private static final int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 5;
    private static final String CEMETERY_NAME = "name";
    private static final long TOTAL_ELEMENTS = 1L;

    private UserCemeteryService userCemeteryService;

    @Mock
    private CemeteryRepository cemeteryRepository;

    @Mock
    private UserCemeteryMapper userCemeteryMapper;

    @Mock
    private Page<CemeteryEntity> pageCemetery;

    @Mock
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        userCemeteryService = new UserCemeteryService(cemeteryRepository, userCemeteryMapper);
    }

    @Test
    void shouldFindByCriteria() {
        ArgumentCaptor<Pageable> argumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        //given
        Set<Long> cemeteryIds = Set.of(1L, 2L);
        given(cemeteryRepository.findByIds(eq(cemeteryIds), any()))
                .willReturn(pageCemetery);
        CemeteryEntity cemeteryEntity = CemeteryEntity.builder().build();
        List<CemeteryEntity> cemeteryEntityList = List.of(cemeteryEntity);
        given(pageCemetery.getContent())
                .willReturn(cemeteryEntityList);
        given(pageCemetery.getPageable())
                .willReturn(pageable);
        given(pageCemetery.getTotalElements())
                .willReturn(TOTAL_ELEMENTS);
        UserCemeteryElementDto userCemeteryElementDto = new UserCemeteryElementDto();
        given(userCemeteryMapper.toUserCemeteryElementDtoList(cemeteryEntityList))
                .willReturn(List.of(userCemeteryElementDto));
        UserCemeteryCriteriaDto userCemeteryCriteriaDto = UserCemeteryCriteriaDto.builder()
                .sortColumn(CEMETERY_NAME)
                .sortOrder(Sort.Direction.ASC)
                .pageIndex(PAGE_INDEX)
                .pageSize(PAGE_SIZE)
                .build();

        //when
        Page<UserCemeteryElementDto> result = userCemeteryService.findByCriteria(userCemeteryCriteriaDto, cemeteryIds);

        //then
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getPageable()).isEqualTo(pageable);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(userCemeteryElementDto);
        then(cemeteryRepository).should().findByIds(any(), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getPageSize()).isEqualTo(5);
        assertThat(argumentCaptor.getValue().getPageNumber()).isZero();
        assertThat(Objects.requireNonNull(argumentCaptor.getValue().getSort().getOrderFor(CEMETERY_NAME)).getDirection())
                .isEqualTo(Sort.Direction.ASC);
    }
}
