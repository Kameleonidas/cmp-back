package pl.gov.cmp.application.service;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import pl.gov.cmp.administration.service.StatusChangeSendService;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryDraftRequest;
import pl.gov.cmp.application.controller.protocol.response.ReasonsRejectApplicationResponse;
import pl.gov.cmp.application.exception.ApplicationCemeteryNotFoundException;
import pl.gov.cmp.application.model.dto.*;
import pl.gov.cmp.application.model.entity.*;
import pl.gov.cmp.application.model.entity.projection.ApplicationCemeteryApplicantProjection;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;
import pl.gov.cmp.application.model.enums.LegalForm;
import pl.gov.cmp.application.model.mapper.ApplicationCemeteryApplicantMapper;
import pl.gov.cmp.application.model.mapper.ApplicationCemeteryMapper;
import pl.gov.cmp.application.model.mapper.ApplicationCemeteryUpdateMapper;
import pl.gov.cmp.application.model.mapper.ApplicationDtoMapper;
import pl.gov.cmp.application.repository.*;
import pl.gov.cmp.auth.repository.UserAccountRepository;
import pl.gov.cmp.cemetery.controller.protocol.model.RejectApplicationDto;
import pl.gov.cmp.cemetery.controller.protocol.request.ReasonsRejectionApplication;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;
import pl.gov.cmp.dictionary.repository.CemeteryFacilityTypeDictionaryRepository;
import pl.gov.cmp.file.service.FileService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static pl.gov.cmp.application.model.enums.ApplicationStatus.COMPLETED;
import static pl.gov.cmp.application.model.enums.ApplicationStatus.SENT;
import static pl.gov.cmp.application.model.enums.ApplicationStatus.TO_BE_COMPLETED;
import static pl.gov.cmp.application.model.enums.ApplicationType.CEMETERY_REGISTRATION;
import static pl.gov.cmp.application.model.enums.ApplicationType.VETERAN_REGISTRATION;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    private static final long APP_ID = 1L;
    private static final String OBJECT_NAME = "Cmentarz";
    public static final String VOIVODESHIP_TERC = "06";
    public static final String VOIVODESHIP_NAME = "lubelskie";
    public static final String DISTRICT_TERC = "0601";
    public static final String DISTRICT_NAME = "bialski";
    public static final String COMMUNE_TERC = "0601032";
    public static final String COMMUNE_NAME = "Biała Podlaska";
    public static final String INSTITUTION_NAME = "Instytucja";
    public static final String INSTITUTION_NIP = "3814564069";
    public static final String FIRST_NAME = "Jan";
    public static final String LAST_NAME = "Kowalski";
    public static final String EMAIL = "jkowal@test.pl";
    public static final Long TEST_APPLICATION_ID = 1L;
    public static final String TEST_DESCRIPTION = "testDescription";

    @Mock
    private ApplicationCemeteryApplicantRepository applicantRepository;

    @Mock
    private ApplicationCemeteryDraftRepository applicationCemeteryDraftRepository;

    @Mock
    private ApplicationCemeteryRepository applicationCemeteryRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private ApplicationCustomRepository applicationCustomRepository;
    @Mock
    private CemeteryFacilityTypeDictionaryRepository cemeteryFacilityTypeDictionaryRepository;

    @Mock
    FileService fileService;

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    StatusChangeSendService statusChangeSendService;

    @Mock
    ApplicationCemeteryUpdateMapper applicationCemeteryUpdateMapper;

    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        final var applicantMapper = Mappers.getMapper(ApplicationCemeteryApplicantMapper.class);
        final var applicationCemeteryMapper = Mappers.getMapper(ApplicationCemeteryMapper.class);
        final var applicationDtoMapper = Mappers.getMapper(ApplicationDtoMapper.class);
        applicationService = new ApplicationService(applicantMapper,
                applicantRepository,
                applicationCemeteryRepository,
                applicationCustomRepository,
                applicationDtoMapper,
                applicationCemeteryMapper,
                fileService,
                applicationRepository,
                statusChangeSendService,
                applicationCemeteryUpdateMapper,
                cemeteryFacilityTypeDictionaryRepository);
    }

    @Test
    void shouldCreateApplicationCemetery() {
        // given
        ApplicationCemeteryDto applicationCemeteryDto = prepareApplicationCemeteryDto();

        // when
        this.applicationService.createApplicationCemetery(applicationCemeteryDto);

        // then
        verify(this.applicationCemeteryRepository, times(1)).save(argThat(app -> {
            assertThat(app).isNotNull();
            assertEquals(SENT, app.getApplication().getAppStatus());
            assertEquals(CemeteryStatus.ACTIVE, app.getCemeteryStatus());
            assertEquals(OBJECT_NAME, app.getObjectName());
            assertEquals(VOIVODESHIP_TERC, app.getLocationAddress().getVoivodeshipTercCode());
            assertEquals(VOIVODESHIP_NAME, app.getLocationAddress().getVoivodeship());
            assertEquals(DISTRICT_TERC, app.getLocationAddress().getDistrictTercCode());
            assertEquals(DISTRICT_NAME, app.getLocationAddress().getDistrict());
            assertEquals(COMMUNE_TERC, app.getLocationAddress().getCommuneTercCode());
            assertEquals(COMMUNE_NAME, app.getLocationAddress().getCommune());
            assertEquals(FIRST_NAME, app.getApplicant().getFirstName());
            assertEquals(LAST_NAME, app.getApplicant().getLastName());
            assertEquals(FIRST_NAME, app.getOwner().getFirstName());
            assertEquals(LAST_NAME, app.getOwner().getLastName());
            assertEquals(LegalForm.INSTITUTION, app.getManager().getType());
            assertEquals(INSTITUTION_NAME, app.getManager().getName());
            assertEquals(INSTITUTION_NIP, app.getManager().getNip());
            assertEquals(FIRST_NAME, app.getManager().getRepresentative().getFirstName());
            assertEquals(LAST_NAME, app.getManager().getRepresentative().getLastName());
            return true;
        }));
    }

    @Test
    void shouldRejectApplicationCemetery() {
        //given
        RejectApplicationDto rejectApplicationDto = RejectApplicationDto.builder()
                .applicationId(TEST_APPLICATION_ID)
                .reasonRejectionApplication(ReasonsRejectionApplication.OTHER)
                .description(TEST_DESCRIPTION)
                .build();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        when(applicationRepository.findByIdAndAppStatuses(TEST_APPLICATION_ID, List.of(SENT, COMPLETED)))
                .thenReturn(applicationEntity);

        //when
        ReasonsRejectApplicationResponse response = applicationService.rejectApplication(rejectApplicationDto);

        //then
        verify(applicationRepository).save(applicationEntity);
        verify(statusChangeSendService).sendStatusChange(applicationEntity);
        assertThat(response.getApplicationId()).isEqualTo(TEST_APPLICATION_ID);
        assertThat(response.getDescription()).isEqualTo(TEST_DESCRIPTION);
        assertThat(response.getReasonsRejectionApplication()).isEqualTo(ReasonsRejectionApplication.OTHER);
    }

//    @Test
//    void shouldFindApplicationCemetery() {
//        // given
//        ApplicationCemeteryEntity applicationCemeteryEntity = prepareApplicationCemeteryEntity();
//        when(this.applicationCemeteryRepository.findById(anyLong())).thenReturn(Optional.of(applicationCemeteryEntity));
//
//        // when
//        ApplicationCemeteryDto applicationCemeteryDto = this.applicationService.getApplicationCemetery(APP_ID);
//
//        // then
//        assertThat(applicationCemeteryDto).isNotNull();
//        assertEquals(APP_ID, applicationCemeteryDto.getId());
//        assertEquals(CemeteryStatus.ACTIVE, applicationCemeteryDto.getCemeteryStatus());
//        assertEquals(OBJECT_NAME, applicationCemeteryDto.getObjectName());
//        assertEquals(VOIVODESHIP_TERC, applicationCemeteryDto.getLocationAddress().getVoivodeshipTercCode());
//        assertEquals(VOIVODESHIP_NAME, applicationCemeteryDto.getLocationAddress().getVoivodeship());
//        assertEquals(DISTRICT_TERC, applicationCemeteryDto.getLocationAddress().getDistrictTercCode());
//        assertEquals(DISTRICT_NAME, applicationCemeteryDto.getLocationAddress().getDistrict());
//        assertEquals(COMMUNE_TERC, applicationCemeteryDto.getLocationAddress().getCommuneTercCode());
//        assertEquals(COMMUNE_NAME, applicationCemeteryDto.getLocationAddress().getCommune());
//        assertEquals(FIRST_NAME, applicationCemeteryDto.getApplicant().getFirstName());
//        assertEquals(LAST_NAME, applicationCemeteryDto.getApplicant().getLastName());
//        assertEquals(FIRST_NAME, applicationCemeteryDto.getOwner().getFirstName());
//        assertEquals(LAST_NAME, applicationCemeteryDto.getOwner().getLastName());
//        assertEquals(LegalForm.INSTITUTION, applicationCemeteryDto.getManager().getType());
//        assertEquals(INSTITUTION_NAME, applicationCemeteryDto.getManager().getName());
//        assertEquals(INSTITUTION_NIP, applicationCemeteryDto.getManager().getNip());
//        assertEquals(FIRST_NAME, applicationCemeteryDto.getManager().getRepresentative().getFirstName());
//        assertEquals(LAST_NAME, applicationCemeteryDto.getManager().getRepresentative().getLastName());
//    }

    @Test
    void shouldMarkApplicationAsToBeCompleted() {
        //given
        final var applicationCaptor = ArgumentCaptor.forClass(ApplicationEntity.class);
        final var applicationId = 1L;
        final var application = new ApplicationEntity();
        application.setApplication(new ApplicationCemeteryEntity());
        given(applicationRepository.findByIdAndAppStatuses(applicationId, List.of(SENT,COMPLETED))).willReturn(application);
        final var applicationToBeCompleted =
                ApplicationToBeCompletedDto.builder().applicationId(applicationId).fieldsToBeCompleted("json fields").build();

        //when
        assertDoesNotThrow(() -> applicationService.applicationToBeCompleted(applicationToBeCompleted));

        //then
        then(applicationRepository).should().save(applicationCaptor.capture());
        final var savedApplication = applicationCaptor.getValue();
        assertThat(savedApplication.getAppStatus()).isEqualTo(TO_BE_COMPLETED);
        assertThat(savedApplication.getApplication().getFieldsToBeCompleted()).isEqualTo(applicationToBeCompleted.getFieldsToBeCompleted());
    }

    @Test
    void shouldThrowApplicationCemeteryNotFoundExceptionWhenApplicationToMarkAsToBeCompletedWasNotFoundByNumberAndStatus() {
        //given
        final var applicationId = 1L;
        given(applicationRepository.findByIdAndAppStatuses(applicationId, List.of(SENT,COMPLETED))).willReturn(null);
        final var applicationToBeCompleted =
                ApplicationToBeCompletedDto.builder().applicationId(applicationId).fieldsToBeCompleted("json fields").build();

        //when
        final var exception = assertThrows(ApplicationCemeteryNotFoundException.class,
                () -> applicationService.applicationToBeCompleted(applicationToBeCompleted));

        //then
        assertThat(exception.getCode()).isEqualTo(1800);
        assertThat(exception.getMessage()).isEqualTo(format("Application cemetery not found [appCemeteryId: %s]", applicationId));
        verify(applicationRepository, never()).save(any());
    }

    @Test
    void shouldGetApplicants() {
        //given
        final var firstName = "John";
        final var lastName = "Doe";
        final var firstApplicant = createApplicantProjection("Johnathan", "Doel");
        final var secondApplicant = createApplicantProjection("Johnathano", "Doec");
        given(applicantRepository.getApplicants(firstName, lastName)).willReturn(List.of(firstApplicant, secondApplicant));

        //when
        final var result = applicationService.getApplicants(firstName, lastName);

        //then
        final var firstReturnedApplicant = result.get(0);
        assertThat(firstReturnedApplicant.getFirstName()).isEqualTo("Johnathano");
        assertThat(firstReturnedApplicant.getLastName()).isEqualTo("Doec");
        final var secondReturnedApplicant = result.get(1);
        assertThat(secondReturnedApplicant.getFirstName()).isEqualTo("Johnathan");
        assertThat(secondReturnedApplicant.getLastName()).isEqualTo("Doel");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ID", "APP_NUMBER", "APP_TYPE", "APP_STATUS", "OBJECT_NAME", "CREATE_DATE", "UPDATE_DATE"})
    void shouldFindApplicationsByCriteria(String sortColumn) {
        //given
        final var criteria = buildApplicationSearchCriteria(sortColumn);
        final var firstApplication = createApplication(2L, "AppNr2", VETERAN_REGISTRATION, TO_BE_COMPLETED,
                "object 2", LocalDateTime.parse("2022-02-11T10:00:00"), LocalDateTime.parse("2022-03-11T10:00:00"));
        final var secondApplication = createApplication(1L, "AppNr1", CEMETERY_REGISTRATION, SENT,
                "object 1", LocalDateTime.parse("2022-02-10T10:00:00"), LocalDateTime.parse("2022-03-10T10:00:00"));
        final var pageSize = criteria.getPageSize();
        final var page = new PageImpl<>(List.of(firstApplication, secondApplication),
                PageRequest.of(criteria.getPageIndex(), pageSize, Sort.by(sortColumn)), pageSize);
        given(applicationCustomRepository.findByCriteria(criteria))
                .willReturn(page);

        //when
        final var result = applicationService.findByCriteria(criteria);

        //then
        final var pageable = result.getPageable();
        assertThat(pageable.getPageNumber()).isEqualTo(criteria.getPageIndex());
        assertThat(pageable.getPageSize()).isEqualTo(criteria.getPageSize());
        final var content = result.getContent();
        assertThat(content.size()).isEqualTo(2);

        final var firstElement = content.get(0);
        assertThat(firstElement.getId()).isEqualTo(secondApplication.getId());
        assertThat(firstElement.getAppNumber()).isEqualTo(secondApplication.getAppNumber());
        assertThat(firstElement.getAppType()).isEqualTo(secondApplication.getAppType());
        assertThat(firstElement.getAppStatus()).isEqualTo(secondApplication.getAppStatus());
        assertThat(firstElement.getObjectName()).isEqualTo(secondApplication.getObjectName());
        assertThat(firstElement.getCreateDate()).isEqualTo(secondApplication.getCreateDate());
        assertThat(firstElement.getUpdateDate()).isEqualTo(secondApplication.getUpdateDate());

        final var secondElement = content.get(1);
        assertThat(secondElement.getId()).isEqualTo(firstApplication.getId());
        assertThat(secondElement.getAppNumber()).isEqualTo(firstApplication.getAppNumber());
        assertThat(secondElement.getAppType()).isEqualTo(firstApplication.getAppType());
        assertThat(secondElement.getAppStatus()).isEqualTo(firstApplication.getAppStatus());
        assertThat(secondElement.getObjectName()).isEqualTo(firstApplication.getObjectName());
        assertThat(secondElement.getCreateDate()).isEqualTo(firstApplication.getCreateDate());
        assertThat(secondElement.getUpdateDate()).isEqualTo(firstApplication.getUpdateDate());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ID", "APP_NUMBER", "APP_TYPE", "APP_STATUS", "OBJECT_NAME", "CREATE_DATE", "UPDATE_DATE"})
    void shouldFindApplicationsByCriteriaForUser(String sortColumn) {
        //given
        final var criteria = buildApplicationSearchCriteria(sortColumn);
        final var firstApplication = createApplication(2L, "AppNr2", VETERAN_REGISTRATION, TO_BE_COMPLETED,
                "object 2", LocalDateTime.parse("2022-02-11T10:00:00"), LocalDateTime.parse("2022-03-11T10:00:00"));
        final var secondApplication = createApplication(1L, "AppNr1", CEMETERY_REGISTRATION, SENT,
                "object 1", LocalDateTime.parse("2022-02-10T10:00:00"), LocalDateTime.parse("2022-03-10T10:00:00"));
        final var pageSize = criteria.getPageSize();
        final var page = new PageImpl<>(List.of(firstApplication, secondApplication),
                PageRequest.of(criteria.getPageIndex(), pageSize, Sort.by(sortColumn)), pageSize);
        given(applicationCustomRepository.findByCriteria(criteria))
                .willReturn(page);

        //when
        final var result = applicationService.findByCriteria(criteria);

        //then
        final var pageable = result.getPageable();
        assertThat(pageable.getPageNumber()).isEqualTo(criteria.getPageIndex());
        assertThat(pageable.getPageSize()).isEqualTo(criteria.getPageSize());
        final var content = result.getContent();
        assertThat(content.size()).isEqualTo(2);

        final var firstElement = content.get(0);
        assertThat(firstElement.getId()).isEqualTo(secondApplication.getId());
        assertThat(firstElement.getAppNumber()).isEqualTo(secondApplication.getAppNumber());
        assertThat(firstElement.getAppType()).isEqualTo(secondApplication.getAppType());
        assertThat(firstElement.getAppStatus()).isEqualTo(secondApplication.getAppStatus());
        assertThat(firstElement.getObjectName()).isEqualTo(secondApplication.getObjectName());
        assertThat(firstElement.getCreateDate()).isEqualTo(secondApplication.getCreateDate());
        assertThat(firstElement.getUpdateDate()).isEqualTo(secondApplication.getUpdateDate());

        final var secondElement = content.get(1);
        assertThat(secondElement.getId()).isEqualTo(firstApplication.getId());
        assertThat(secondElement.getAppNumber()).isEqualTo(firstApplication.getAppNumber());
        assertThat(secondElement.getAppType()).isEqualTo(firstApplication.getAppType());
        assertThat(secondElement.getAppStatus()).isEqualTo(firstApplication.getAppStatus());
        assertThat(secondElement.getObjectName()).isEqualTo(firstApplication.getObjectName());
        assertThat(secondElement.getCreateDate()).isEqualTo(firstApplication.getCreateDate());
        assertThat(secondElement.getUpdateDate()).isEqualTo(firstApplication.getUpdateDate());
    }

    private ApplicationEntity createApplication(long id, String appNumber, ApplicationType type, ApplicationStatus status,
                                                String objectName, LocalDateTime createDate, LocalDateTime updateDate) {
        final var application = new ApplicationEntity();
        application.setId(id);
        application.setAppNumber(appNumber);
        application.setAppType(type);
        application.setAppStatus(status);
        application.setObjectName(objectName);
        application.setCreateDate(createDate);
        application.setUpdateDate(updateDate);
        return application;
    }

    private ApplicationCriteriaDto buildApplicationSearchCriteria(String sortColumn) {
        return ApplicationCriteriaDto.builder()
                .sortColumn(sortColumn)
                .sortOrder(ASC)
                .pageIndex(0)
                .pageSize(5)
                .build();

    }

    @Test
    void shouldThrowApplicationCemeteryNotFoundException() {
        // given
        when(this.applicationCemeteryRepository.findById(APP_ID)).thenReturn(Optional.empty());

        // when
        assertThrows(ApplicationCemeteryNotFoundException.class, () -> this.applicationService.getApplicationCemetery(APP_ID));
    }

    private ApplicationCemeteryApplicantProjection createApplicantProjection(String firstName, String lastName) {
        return new ApplicationCemeteryApplicantProjection() {
            @Override
            public String getFirstName() {
                return firstName;
            }

            @Override
            public String getLastName() {
                return lastName;
            }
        };
    }

    private ApplicationCemeteryDto prepareApplicationCemeteryDto() {
        return ApplicationCemeteryDto.builder()
                .cemeteryStatus(CemeteryStatus.ACTIVE)
                .objectName(OBJECT_NAME)
                .locationAddress(prepareAddressDto())
                .applicant(prepareApplicantDto())
                .owner(prepareOwnerDto())
                .manager(prepareManagerDto())
                .build();
    }

    private ApplicationAddressDto prepareAddressDto() {
        return ApplicationAddressDto.builder()
                .voivodeshipTercCode(VOIVODESHIP_TERC)
                .voivodeship(VOIVODESHIP_NAME)
                .districtTercCode(DISTRICT_TERC)
                .district(DISTRICT_NAME)
                .communeTercCode(COMMUNE_TERC)
                .commune(COMMUNE_NAME)
                .build();
    }

    private ApplicationCemeteryApplicantDto prepareApplicantDto() {
        return ApplicationCemeteryApplicantDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();
    }

    private ApplicationCemeteryOwnerDto prepareOwnerDto() {
        return ApplicationCemeteryOwnerDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();
    }

    private ApplicationCemeteryManagerDto prepareManagerDto() {
        return ApplicationCemeteryManagerDto.builder()
                .type(LegalForm.INSTITUTION)
                .name(INSTITUTION_NAME)
                .nip(INSTITUTION_NIP)
                .representative(prepareRepresentativeDto())
                .build();
    }

    private ApplicationCemeteryRepresentativeDto prepareRepresentativeDto() {
        return ApplicationCemeteryRepresentativeDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();
    }

    private ApplicationCemeteryRepresentativeEntity prepareRepresentativeEntity() {
        ApplicationCemeteryRepresentativeEntity representative = new ApplicationCemeteryRepresentativeEntity();
        representative.setFirstName(FIRST_NAME);
        representative.setLastName(LAST_NAME);
        representative.setEmail(EMAIL);
        return representative;
    }
}
