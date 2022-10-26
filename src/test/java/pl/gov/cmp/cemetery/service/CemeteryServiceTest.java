package pl.gov.cmp.cemetery.service;

import org.assertj.core.util.Lists;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import pl.gov.cmp.administration.model.dto.InvitationDto;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.service.InvitationSendService;
import pl.gov.cmp.administration.service.StatusChangeSendService;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryOwnerEntity;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.repository.ApplicationRepository;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;
import pl.gov.cmp.cemetery.controller.protocol.mapper.CreateCemeteryFromApplicationMapperDto;
import pl.gov.cmp.cemetery.exception.CemeteryNotFoundException;
import pl.gov.cmp.cemetery.model.dto.*;
import pl.gov.cmp.cemetery.model.entity.*;
import pl.gov.cmp.cemetery.model.mapper.CemeteryEntityMapper;
import pl.gov.cmp.cemetery.model.mapper.CemeteryMapper;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;
import pl.gov.cmp.dictionary.model.entity.CemeteryOwnerCategoryDictionaryEntity;
import pl.gov.cmp.dictionary.model.entity.CemeterySourceDictionaryEntity;
import pl.gov.cmp.dictionary.model.entity.ChurchReligionDictionaryEntity;
import pl.gov.cmp.dictionary.repository.CemeteryOwnerCategoryDictionaryRepository;
import pl.gov.cmp.dictionary.repository.CemeterySourceDictionaryRepository;
import pl.gov.cmp.dictionary.repository.ChurchReligionDictionaryRepository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static pl.gov.cmp.cemetery.model.enums.CemeteryStatus.ACTIVE;

@ExtendWith(MockitoExtension.class)
class CemeteryServiceTest {

    private static final long CEMETERY_ID = 11964L;
    private static final long TEST_APPLICATION_ID = 1L;

    @Mock
    private CemeteryRepository cemeteryRepository;

    @Mock
    private CemeteryMapper cemeteryMapper;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private CemeterySourceDictionaryRepository cemeterySourceDictionaryRepository;

    @Mock
    private StatusChangeSendService statusChangeSendService;

    @Mock
    private InvitationSendService invitationSendService;

    @Captor
    private ArgumentCaptor<ApplicationEntity> applicationEntityCaptor;

    @Captor
    private ArgumentCaptor<List<InvitationDto>> invitationDtoListCaptor;

    @Mock
    private ChurchReligionDictionaryRepository churchReligionDictionaryRepository;

    @Mock
    private CemeteryOwnerCategoryDictionaryRepository cemeteryOwnerCategoryDictionaryRepository;

    private CemeteryService cemeteryService;

    @Mock
    private CriteriaBuilderImpl criteriaBuilder;

    @Mock
    private CriteriaQuery<UserAccountEntity> criteriaQuery;

    @Mock
    private Root<CemeteryEntity> root;
    @Mock
    private Join<CemeteryEntity, CemeterySourceDictionaryEntity> source;

    @BeforeEach
    void setUp() {
        cemeteryService = new CemeteryService(cemeteryRepository, cemeteryMapper, applicationRepository, cemeterySourceDictionaryRepository, statusChangeSendService, invitationSendService ,churchReligionDictionaryRepository, cemeteryOwnerCategoryDictionaryRepository);
    }

    @Test
    void shouldFindCemeteries() {
        //given
        final var criteria = buildCriteria();
        final var cemeteryBName = "B cemetery";
        final var cemeteryB = createCemetery(cemeteryBName);
        final var cemeteryAName = "A cemetery";
        final var cemeteryA = createCemetery(cemeteryAName);
        final var totalElements = 2;
        List<CemeteryEntity> cemeteries = List.of(cemeteryB, cemeteryA);
        final var cemeteryPage = new PageImpl<>(cemeteries, PageRequest.of(0, totalElements, Sort.by("name")), totalElements);
        given(cemeteryRepository.findByCriteria(criteria)).willReturn(cemeteryPage);
        CemeteryElementDto cemeteryDTOB = new CemeteryElementDto();
        cemeteryDTOB.setName(cemeteryBName);
        CemeteryElementDto cemeteryDTOA = new CemeteryElementDto();
        cemeteryDTOA.setName(cemeteryAName);
        List<CemeteryElementDto> cemeteryDTOs = Lists.newArrayList(cemeteryDTOB, cemeteryDTOA);
        given(cemeteryMapper.toCemeteryElementDtoList(cemeteries)).willReturn(cemeteryDTOs);
        //when
        final var result = cemeteryService.findByCriteria(criteria);

        //then
        assertThat(result.getTotalElements()).isEqualTo(totalElements);
        final var content = result.getContent();
        assertThat(content.get(0).getName()).isEqualTo(cemeteryAName);
        assertThat(content.get(1).getName()).isEqualTo(cemeteryBName);
    }

    private CemeteryEntity createCemetery(String name) {
        return CemeteryEntity.builder().name(name).build();
    }

    private CemeteryCriteriaDto buildCriteria() {
        final var criteria = new CemeteryCriteriaDto();
        criteria.setSortOrder(ASC);
        return criteria;
    }

    @Test
    void shouldFindCemeteryById() {
        // given
        CemeteryEntity cemeteryEntity = prepareCemeteryEntity();
        when(cemeteryRepository.findById(CEMETERY_ID)).thenReturn(Optional.of(cemeteryEntity));
        when(cemeteryMapper.toCemeteryDto(cemeteryEntity)).thenReturn(prepareCemeteryDTO());
        // when
        CemeteryDto cemeteryDto = cemeteryService.getCemetery(CEMETERY_ID);

        // then
        assertNotNull(cemeteryDto);
        assertEquals(cemeteryEntity.getId(), cemeteryDto.getId());
        assertEquals(cemeteryEntity.getName(), cemeteryDto.getName());
        assertEquals(cemeteryEntity.getStatus().getName(), cemeteryDto.getStatus());
    }

    @Test
    void shouldFindCemeteryResponseById() {
        // given
        CemeteryEntity cemeteryEntity = prepareCemeteryEntity();
        when(cemeteryRepository.findById(CEMETERY_ID)).thenReturn(Optional.of(cemeteryEntity));
        when(cemeteryMapper.toCemeteryResponseDto(cemeteryEntity)).thenReturn(prepareCemeteryResponseDto());
        // when
        var cemeteryDto = cemeteryService.getCemeteryResponse(CEMETERY_ID);

        // then
        assertNotNull(cemeteryDto);
        assertEquals(cemeteryEntity.getId(), cemeteryDto.getId());
        assertEquals(cemeteryEntity.getName(), cemeteryDto.getName());
        assertEquals(cemeteryEntity.getStatus().getName(), cemeteryDto.getStatus());
        assertNull(cemeteryDto.getOwner());
    }

    @Test
    void shouldThrowCemeteryNotFoundException() {
        // given
        when(this.cemeteryRepository.findById(CEMETERY_ID)).thenReturn(Optional.empty());

        // when
        assertThrows(CemeteryNotFoundException.class, () -> cemeteryService.getCemetery(CEMETERY_ID));
    }

    @Test
    void shouldRegisterCemeteryFromApplication() {
        //given
        ApplicationEntity applicationEntity = new ApplicationEntity();
        ApplicationCemeteryEntity applicationCemeteryEntity = new ApplicationCemeteryEntity();
        applicationCemeteryEntity.setReligionId(10L);
        ApplicationCemeteryOwnerEntity owner = new ApplicationCemeteryOwnerEntity();
        owner.setOwnerCategoryId(25L);
        applicationCemeteryEntity.setOwner(owner);
        applicationEntity.setApplication(applicationCemeteryEntity);
        CemeterySourceDictionaryEntity cemeterySourceDictionaryEntity = new CemeterySourceDictionaryEntity();
        ChurchReligionDictionaryEntity churchReligionDictionaryEntity = new ChurchReligionDictionaryEntity();
        CemeteryOwnerCategoryDictionaryEntity cemeteryOwnerCategoryDictionary = new CemeteryOwnerCategoryDictionaryEntity();
        when(applicationRepository.findByIdAndAppStatuses(TEST_APPLICATION_ID, List.of(ApplicationStatus.SENT, ApplicationStatus.COMPLETED)))
                .thenReturn(applicationEntity);
        when(applicationRepository.findByIdAndAppStatus(TEST_APPLICATION_ID, ApplicationStatus.ACCEPTED))
                .thenReturn(applicationEntity);
        when(cemeterySourceDictionaryRepository.findById(1L))
                .thenReturn(Optional.of(cemeterySourceDictionaryEntity));
        when(churchReligionDictionaryRepository.findById(10L))
                .thenReturn(Optional.of(churchReligionDictionaryEntity));
        when(cemeteryOwnerCategoryDictionaryRepository.findById(25L))
                .thenReturn(Optional.of(cemeteryOwnerCategoryDictionary));
        CemeteryEntity cemeteryEntity = new CemeteryEntity();
        cemeteryEntity.setId(CEMETERY_ID);
        cemeteryEntity.setOwner(CemeteryOwnerEntity.builder()
                .email("test@abc.pl")
                .build());
        cemeteryEntity.setUserAdmin(CemeteryUserAdminEntity.builder()
                .email("test2@abc.pl")
                .build());
        when(cemeteryRepository.findById(CEMETERY_ID)).thenReturn(Optional.of(cemeteryEntity));
        CemeteryDto cemeteryDto = CemeteryDto.builder().id(CEMETERY_ID).build();
        try (MockedStatic<CemeteryEntityMapper> utilities = Mockito.mockStatic(CemeteryEntityMapper.class)) {
            utilities.when(()-> CemeteryEntityMapper.toCemetery(applicationCemeteryEntity, cemeterySourceDictionaryEntity, churchReligionDictionaryEntity, cemeteryOwnerCategoryDictionary))
                    .thenReturn(cemeteryEntity);
            try (MockedStatic<CreateCemeteryFromApplicationMapperDto> utilities2 = Mockito.mockStatic(CreateCemeteryFromApplicationMapperDto.class)){
                utilities2.when(()-> CreateCemeteryFromApplicationMapperDto.toCemeteryDto(cemeteryEntity))
                        .thenReturn(cemeteryDto);
                //when
                cemeteryService.registerCemeteryFromApplicationAndSendEmails(TEST_APPLICATION_ID);
            }
        }
        //then
        verify(applicationRepository).save(applicationEntityCaptor.capture());
        verify(statusChangeSendService).sendStatusChange(applicationEntity);
        assertThat(applicationEntityCaptor.getValue().getAppStatus()).isEqualTo(ApplicationStatus.ACCEPTED);
        verify(invitationSendService).sendInvitations(invitationDtoListCaptor.capture());
        assertThat(invitationDtoListCaptor.getValue().get(0).getEmail()).isEqualTo("test@abc.pl");
        InvitationDto invitationDto = invitationDtoListCaptor.getValue().get(1);
        assertThat(invitationDto.getEmail()).isEqualTo("test2@abc.pl");
        assertThat(invitationDto.getInstitutionId()).isEqualTo(CEMETERY_ID);
        assertThat(invitationDto.getInstitutionType()).isEqualTo(InstitutionType.CEMETERY);
        assertThat(invitationDto.getRequestIdentifier()).isNotNull();
    }

    @Test
    void shouldFindSimplifiedCemeteriesByCriteria() {
        //given
        SimplifiedCemeteryDto criteria = new SimplifiedCemeteryDto();
        criteria.setVoivodeshipTercCode("01");
        criteria.setDistrictTercCode("0101");
        criteria.setCommuneTercCode("010101");
        GeometryFactory gf = new GeometryFactory();
        Polygon polygon = gf.createPolygon(new Coordinate[]{new Coordinate(1,0), new Coordinate(0,1), new Coordinate(1,1), new Coordinate(1,0)});
        CemeteryGeometryEntity cemeteryGeometry = CemeteryGeometryEntity.builder().geometry(polygon).build();
        Long cemeteryId = 12L;
        CemeteryEntity cemeteryEntity = CemeteryEntity.builder()
                .id(cemeteryId)
                .name("cemetery name")
                .cemeteryGeometry(cemeteryGeometry)
                .build();
        List<CemeteryEntity> cemeteryEntities = Lists.newArrayList(cemeteryEntity);
        when(root.join(ArgumentMatchers.<SingularAttribute<CemeteryEntity, CemeterySourceDictionaryEntity>>any())).thenReturn(source);
        when(cemeteryRepository.findAll(ArgumentMatchers.<Specification<CemeteryEntity>>any()))
                .thenAnswer(invocationOnMock -> {
                    callToPredicate(invocationOnMock);
                    return cemeteryEntities;
                });
        SimplifiedCemeteryElementDto simplifiedCemeteryElementDto = new SimplifiedCemeteryElementDto();
        simplifiedCemeteryElementDto.setId(cemeteryId);
        simplifiedCemeteryElementDto.setName("cemetery name");
        simplifiedCemeteryElementDto.setCemeteryGeometry(CemeteryGeometryDto.builder().geometry("POLIGON123").build());
        when(cemeteryMapper.toSimplifiedCemeteryElementDtoList(cemeteryEntities)).thenReturn(Lists.newArrayList(simplifiedCemeteryElementDto));
        //when
        List<SimplifiedCemeteryElementDto> result = cemeteryService.findSimplifiedCemeteriesByCriteria(criteria);
        //then
        assertThat(result).isNotEmpty();
        SimplifiedCemeteryElementDto actual = result.get(0);
        assertThat(actual.getId()).isEqualTo(cemeteryId);
        assertThat(actual.getName()).isEqualTo("cemetery name");
        assertThat(actual.getCemeteryGeometry().getGeometry()).isEqualTo("POLIGON123");
    }

    private void callToPredicate(InvocationOnMock invocationOnMock) {
        ((Specification<CemeteryEntity>) invocationOnMock.getArguments()[0]).toPredicate(root, criteriaQuery, criteriaBuilder);
    }

    private CemeteryEntity prepareCemeteryEntity() {
        final CemeteryEntity cemeteryEntity = new CemeteryEntity();
        cemeteryEntity.setId(CEMETERY_ID);
        cemeteryEntity.setName("Katolicki Cmentarz Komunalny");
        cemeteryEntity.setStatus(ACTIVE);
        cemeteryEntity.setCemeteryGeometry(CemeteryGeometryEntity.builder().build());
        cemeteryEntity.setOwnerCategory(CemeteryOwnerCategoryDictionaryEntity.createWithId(5L));
        cemeteryEntity.setOwner(prepareOwnerEntity());
        return cemeteryEntity;
    }

    private CemeteryDto prepareCemeteryDTO() {
        final CemeteryDto cemeteryDto = new CemeteryDto();
        cemeteryDto.setId(CEMETERY_ID);
        cemeteryDto.setName("Katolicki Cmentarz Komunalny");
        cemeteryDto.setStatus(ACTIVE.getName());
        return cemeteryDto;
    }

    private CemeteryOwnerEntity prepareOwnerEntity() {
        return CemeteryOwnerEntity.builder()
                .name("testOwner")
                .nip("niptestowy")
                .regon("regontestowy")
                .representative(new CemeteryRepresentativeEntity())
                .build();
    }

    private CemeteryResponseDto prepareCemeteryResponseDto() {
        return CemeteryResponseDto.builder()
                .id(CEMETERY_ID)
                .source("uproszczone")
                .name("Katolicki Cmentarz Komunalny")
                .registrationNumber("numer rejestracyjny")
                .status(ACTIVE.getName())
                .description("testowy opis")
                .email("test@test.pl")
                .phoneNumber("123456789")
                .ownerCategory("Osoba fizyczna")
                .build();
    }

}
