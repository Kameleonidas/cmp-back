package pl.gov.cmp.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.service.StatusChangeSendService;
import pl.gov.cmp.application.controller.protocol.response.ReasonsRejectApplicationResponse;
import pl.gov.cmp.application.exception.ApplicationCemeteryNotFoundException;
import pl.gov.cmp.application.exception.ApplicationNotFoundException;
import pl.gov.cmp.application.exception.CemeteryFacilityTypeNotFoundException;
import pl.gov.cmp.application.model.dto.*;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.entity.projection.ApplicationCemeteryApplicantProjection;
import pl.gov.cmp.application.model.enums.ApplicationColumn;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.mapper.ApplicationCemeteryApplicantMapper;
import pl.gov.cmp.application.model.mapper.ApplicationCemeteryMapper;
import pl.gov.cmp.application.model.mapper.ApplicationCemeteryUpdateMapper;
import pl.gov.cmp.application.model.mapper.ApplicationDtoMapper;
import pl.gov.cmp.application.repository.*;
import pl.gov.cmp.cemetery.controller.protocol.model.RejectApplicationDto;
import pl.gov.cmp.cemetery.controller.protocol.request.ReasonsRejectionApplication;
import pl.gov.cmp.dictionary.repository.CemeteryFacilityTypeDictionaryRepository;
import pl.gov.cmp.file.service.FileService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ApplicationService {

    private final ApplicationCemeteryApplicantMapper applicantMapper;
    private final ApplicationCemeteryApplicantRepository applicantRepository;
    private final ApplicationCemeteryRepository applicationCemeteryRepository;
    private final ApplicationCustomRepository applicationCustomRepository;
    private final ApplicationDtoMapper applicationDtoMapper;
    private final ApplicationCemeteryMapper applicationCemeteryMapper;
    private final FileService fileService;
    private final ApplicationRepository applicationRepository;
    private final StatusChangeSendService statusChangeSendService;
    private final ApplicationCemeteryUpdateMapper applicationCemeteryUpdateMapper;
    private final CemeteryFacilityTypeDictionaryRepository facilityTypeDictionaryRepository;

    private static final List<ApplicationStatus> REJECT_AND_TO_COMPLITED_STATUSES = List.of(ApplicationStatus.SENT,ApplicationStatus.COMPLETED);

    private static final String APP_NUMBER = "appNumber";
    private static final String APP_STATUS = "appStatus";
    private static final String APP_TYPE = "appType";
    private static final String CREATE_DATE = "createDate";
    private static final String UPDATE_DATE = "updateDate";
    private static final String OBJECT_NAME = "objectName";
    private static final String ID = "id";


    public void updateApplicationCemetery(ApplicationCemeteryDto applicationCemeteryDto, Long applicationId) {
        var application = applicationCemeteryRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationCemeteryNotFoundException(applicationId.toString()));
        applicationRepository.findById(application.getApplication().getId()).map(a -> {
                    a.setAppStatus(ApplicationStatus.COMPLETED);
                    if(StringUtils.isNoneBlank(applicationCemeteryDto.getObjectName())){
                        a.setObjectName(applicationCemeteryDto.getObjectName());
                    }
                    return applicationRepository.saveAndFlush(a);})
                .orElseThrow(() -> new ApplicationNotFoundException(String.valueOf(applicationId)));
        applicationCemeteryUpdateMapper.updateApplicationCemeteryEntity(application, applicationCemeteryDto);
        if (Objects.nonNull(applicationCemeteryDto.getFacilityType()) || Objects.nonNull(applicationCemeteryDto.getFacilityType().getId())) {
            Long facilityTypeId = applicationCemeteryDto.getFacilityType().getId();
            application.setFacilityType(facilityTypeDictionaryRepository.findById(facilityTypeId)
                    .orElseThrow(() -> new CemeteryFacilityTypeNotFoundException(facilityTypeId.toString())));
        } else {
            application.setFacilityType(null);
        }
        applicationCemeteryRepository.save(application);
    }


    public void createApplicationCemetery(ApplicationCemeteryDto applicationCemeteryDto) {
        ApplicationCemeteryEntity applicationCemetery = applicationCemeteryMapper.toApplicationCemeteryEntity(applicationCemeteryDto);
        if (isNull(applicationCemeteryDto.getId())) {
            ApplicationEntity application = ApplicationEntity.createNewApplication(applicationCemetery.getObjectName(), applicationRepository.getApplicationNumberFromSequence());
            applicationCemetery.setApplication(application);
            application.setApplication(applicationCemetery);
            if (nonNull(applicationCemetery.getApplicant())) {
                applicationCemetery.getApplicant().setApplication(applicationCemetery);
            }
        }
        if (nonNull(applicationCemetery.getPerpetualUser())) {
            applicationCemetery.getPerpetualUser().setApplication(applicationCemetery);
            if (nonNull(applicationCemetery.getPerpetualUser().getRepresentative())) {
                applicationCemetery.getPerpetualUser().getRepresentative().setApplication(applicationCemetery);
            }
        }

        if (nonNull(applicationCemetery.getOwner())) {
            applicationCemetery.getOwner().setApplication(applicationCemetery);
            if (nonNull(applicationCemetery.getOwner().getRepresentative())) {
                applicationCemetery.getOwner().getRepresentative().setApplication(applicationCemetery);
            }
        }
        if (nonNull(applicationCemetery.getManager())) {
            applicationCemetery.getManager().setApplication(applicationCemetery);
            if (nonNull(applicationCemetery.getManager().getRepresentative())) {
                applicationCemetery.getManager().getRepresentative().setApplication(applicationCemetery);
            }
        }
        if (nonNull(applicationCemetery.getUserAdmin())) {
            applicationCemetery.getUserAdmin().setApplication(applicationCemetery);
        }

        applicationCemeteryRepository.save(applicationCemetery);

        if (nonNull(applicationCemeteryDto.getApplicationAttachmentFiles())) {
            fileService.acceptApplicationAttachments(applicationCemetery, applicationCemeteryDto.getApplicationAttachmentFiles());
        }

        if (nonNull(applicationCemeteryDto.getCemeteryAttachmentFiles())) {
            fileService.acceptCemeteryAttachments(applicationCemetery.getId(), applicationCemeteryDto.getCemeteryAttachmentFiles());
        }
    }

    public ApplicationCemeteryDto getApplicationCemetery(Long appCemeteryId) {
        return applicationCemeteryMapper.toApplicationCemeteryDto(getApplicationCemeteryEntity(appCemeteryId));
    }

    private ApplicationCemeteryEntity getApplicationCemeteryEntity(Long appCemeteryId) {
        return applicationCemeteryRepository.findById(appCemeteryId)
                .orElseThrow(() -> new ApplicationCemeteryNotFoundException(appCemeteryId.toString()));
    }

    public Page<ApplicationDto> findByCriteria(ApplicationCriteriaDto criteria) {
        Page<ApplicationEntity> applicationPage = applicationCustomRepository.findByCriteria(criteria);
        List<ApplicationDto> elements = applicationDtoMapper.toApplicationDtoList(applicationPage.getContent());
        sortApplication(elements, criteria.getSortColumn(), criteria.getSortOrder());
        return new PageImpl<>(elements, applicationPage.getPageable(), applicationPage.getTotalElements());
    }

    private void sortApplication(List<ApplicationDto> elements, String sortColumn, Sort.Direction sortOrder) {
        elements.sort(sortOrder.isAscending() ? getComparator(sortColumn) : getComparator(sortColumn).reversed());
    }

    private Comparator<ApplicationDto> getComparator(String sortColumn) {
        Comparator<ApplicationDto> applicationDtoComparator;
        switch (sortColumn) {
            case APP_NUMBER:
                applicationDtoComparator = Comparator.comparing(ApplicationDto::getAppNumber);
                break;
            case APP_STATUS:
                applicationDtoComparator = Comparator.comparing(ApplicationDto::getAppStatus);
                break;
            case APP_TYPE:
                applicationDtoComparator = Comparator.comparing(ApplicationDto::getAppType);
                break;
            case CREATE_DATE:
                applicationDtoComparator = Comparator.comparing(ApplicationDto::getCreateDate);
                break;
            case OBJECT_NAME:
                applicationDtoComparator = Comparator.comparing(ApplicationDto::getObjectName);
                break;
            case UPDATE_DATE:
                applicationDtoComparator = Comparator.comparing(ApplicationDto::getUpdateDate);
                break;
            case ID:
            default:
                applicationDtoComparator = Comparator.comparing(ApplicationDto::getId);
                break;
        }
        return applicationDtoComparator;
    }

    public ReasonsRejectApplicationResponse rejectApplication(RejectApplicationDto rejectApplicationDto) {
        ApplicationEntity application = Optional.ofNullable(applicationRepository.findByIdAndAppStatuses(rejectApplicationDto.getApplicationId(), REJECT_AND_TO_COMPLITED_STATUSES))
                .orElseThrow(() -> new ApplicationCemeteryNotFoundException(rejectApplicationDto.getApplicationId().toString()));
        if(rejectApplicationDto.getReasonRejectionApplication().equals(ReasonsRejectionApplication.OTHER) && rejectApplicationDto.getDescription() == null) {
            throw new IllegalArgumentException("Description is empty");
        }
        application.setAppStatus(ApplicationStatus.REJECTED);
        application.setRejectionReasonDescription(rejectApplicationDto.getDescription());
        applicationRepository.save(application);
        statusChangeSendService.sendStatusChange(application);
        return ReasonsRejectApplicationResponse
                .builder()
                .applicationId(rejectApplicationDto.getApplicationId())
                .description(rejectApplicationDto.getDescription())
                .reasonsRejectionApplication(rejectApplicationDto.getReasonRejectionApplication())
                .build();
    }

    public void applicationToBeCompleted(ApplicationToBeCompletedDto applicationToBeCompletedDto) {
        ApplicationEntity application = Optional.ofNullable(applicationRepository.findByIdAndAppStatuses(applicationToBeCompletedDto.getApplicationId(), REJECT_AND_TO_COMPLITED_STATUSES)).orElseThrow(() -> new ApplicationCemeteryNotFoundException(applicationToBeCompletedDto.getApplicationId().toString()));
        application.setAppStatus(ApplicationStatus.TO_BE_COMPLETED);
        application.getApplication().setFieldsToBeCompleted(applicationToBeCompletedDto.getFieldsToBeCompleted());
        applicationRepository.save(application);
    }


    public List<ApplicationCemeteryApplicantDto> getApplicants(String firstName, String lastName) {
        List<ApplicationCemeteryApplicantProjection> applicants = applicantRepository.getApplicants(firstName, lastName);
        List<ApplicationCemeteryApplicantDto> elements = applicantMapper.toApplicantDtoList(applicants);
        elements.sort(Comparator.comparing(ApplicationCemeteryApplicantDto::getLastName));
        return elements;
    }
}
