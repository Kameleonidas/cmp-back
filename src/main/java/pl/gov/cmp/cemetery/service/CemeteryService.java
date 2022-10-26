package pl.gov.cmp.cemetery.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.model.dto.InvitationDto;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.service.InvitationSendService;
import pl.gov.cmp.administration.service.StatusChangeSendService;
import pl.gov.cmp.application.exception.ApplicationCemeteryNotFoundException;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.repository.ApplicationRepository;
import pl.gov.cmp.cemetery.controller.protocol.mapper.CreateCemeteryFromApplicationMapperDto;
import pl.gov.cmp.cemetery.exception.CemeteryNotFoundException;
import pl.gov.cmp.cemetery.exception.MinimumCriteriaNotProvidedException;
import pl.gov.cmp.cemetery.model.dto.*;
import pl.gov.cmp.cemetery.model.entity.*;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;
import pl.gov.cmp.cemetery.model.mapper.CemeteryEntityMapper;
import pl.gov.cmp.cemetery.model.mapper.CemeteryMapper;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;
import pl.gov.cmp.dictionary.model.entity.CemeteryOwnerCategoryDictionaryEntity;
import pl.gov.cmp.dictionary.model.entity.CemeterySourceDictionaryEntity;
import pl.gov.cmp.dictionary.model.entity.CemeterySourceDictionaryEntity_;
import pl.gov.cmp.dictionary.model.entity.ChurchReligionDictionaryEntity;
import pl.gov.cmp.dictionary.repository.CemeteryOwnerCategoryDictionaryRepository;
import pl.gov.cmp.dictionary.repository.CemeterySourceDictionaryRepository;
import pl.gov.cmp.dictionary.repository.ChurchReligionDictionaryRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DecimalFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static pl.gov.cmp.application.model.enums.LegalForm.NATURAL_PERSON;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class CemeteryService {

    private final CemeteryRepository cemeteryRepository;
    private final CemeteryMapper cemeteryMapper;
    private final ApplicationRepository applicationRepository;
    private final CemeterySourceDictionaryRepository cemeterySourceDictionaryRepository;
    private final StatusChangeSendService statusChangeSendService;
    private final InvitationSendService invitationSendService;
    private final ChurchReligionDictionaryRepository churchReligionDictionaryRepository;
    private final CemeteryOwnerCategoryDictionaryRepository cemeteryOwnerCategoryDictionaryRepository;
    private static final Long COMPLETE_DATA = 1L;
    private static final Long PARTIAL_DATA = 2L;
    private static final String CEMETERY_REGISTRATION_NUMBER_PREFIX = "ERC";
    private static final String NUMBER_FORMAT = "000000";

    public Page<CemeteryElementDto> findByCriteria(CemeteryCriteriaDto criteria) {
        Page<CemeteryEntity> cemeteryPage = cemeteryRepository.findByCriteria(criteria);
        List<CemeteryElementDto> elements = cemeteryMapper.toCemeteryElementDtoList(cemeteryPage.getContent());
        sortCemeteries(elements, criteria.getSortOrder());
        return new PageImpl<>(elements, cemeteryPage.getPageable(), cemeteryPage.getTotalElements());
    }

    @Transactional
    public CemeteryDto registerCemeteryFromApplication(Long applicationId) {
        ApplicationEntity application = Optional.ofNullable(applicationRepository
                .findByIdAndAppStatuses(applicationId, Arrays.asList(ApplicationStatus.SENT, ApplicationStatus.COMPLETED)))
                .orElseThrow(() -> new ApplicationCemeteryNotFoundException(applicationId.toString()));

        var dictionary = cemeterySourceDictionaryRepository.findById(COMPLETE_DATA)
                .orElseThrow(() -> new IllegalStateException("Dictionary not found"));

        var religion = getChurchReligionDictionaryEntity(application);

        var ownerCategory = getCemeteryOwnerCategoryDictionary(application.getApplication().getOwner().getOwnerCategoryId());

        var cemetery = CemeteryEntityMapper.toCemetery(application.getApplication(), dictionary, religion, ownerCategory);
        if (application.getApplication().getBoundCemeteryId() != null) {
            cemeteryRepository.updateStatus(application.getApplication().getBoundCemeteryId(), CemeteryStatus.BOUND);
            var boundCemetery = cemeteryRepository.findById(application.getApplication().getBoundCemeteryId())
                    .orElseThrow(() -> new IllegalStateException("Bound cemetery not found"));
            CemeteryGeometryEntity cemeteryGeometry = cemeteryMapper.toCemeteryGeometry(boundCemetery.getCemeteryGeometry());
            cemeteryGeometry.setCemetery(cemetery);
            cemetery.setCemeteryGeometry(cemeteryGeometry);
        }
        cemetery.setRegistrationNumber(CEMETERY_REGISTRATION_NUMBER_PREFIX + new DecimalFormat(NUMBER_FORMAT)
                .format(cemeteryRepository.getCemeteryRegistrationNumberFromSequence()));
        cemeteryRepository.save(cemetery);
        application.setAppStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.save(application);
        return CreateCemeteryFromApplicationMapperDto.toCemeteryDto(cemetery);
    }

    private CemeteryOwnerCategoryDictionaryEntity getCemeteryOwnerCategoryDictionary(Long ownerCategoryId) {
        if (ownerCategoryId == null) {
            return null;
        }
        return cemeteryOwnerCategoryDictionaryRepository.findById(ownerCategoryId)
                .orElseThrow(() -> new IllegalStateException("Dictionary religion not found"));
    }

    @Transactional
    public CemeteryDto registerCemeteryFromApplicationAndSendEmails(Long applicationId) {
        CemeteryDto cemeteryDto = registerCemeteryFromApplication(applicationId);
        ApplicationEntity application = Optional.ofNullable(applicationRepository.findByIdAndAppStatus(applicationId, ApplicationStatus.ACCEPTED))
                .orElseThrow(() -> new ApplicationCemeteryNotFoundException(applicationId.toString()));
        var cemetery = cemeteryRepository.findById(cemeteryDto.getId())
                .orElseThrow(() -> new IllegalStateException("Cemetery not found"));
        sendEmails(application, cemetery);
        return cemeteryDto;
    }

    private ChurchReligionDictionaryEntity getChurchReligionDictionaryEntity(ApplicationEntity application) {
        if (application.getApplication().getReligionId() == null) {
            return null;
        }
        return churchReligionDictionaryRepository.findById(application.getApplication().getReligionId())
                .orElseThrow(() -> new IllegalStateException("Dictionary religion not found"));
    }

    private void sendEmails(ApplicationEntity application, CemeteryEntity cemetery) {
        try {
            statusChangeSendService.sendStatusChange(application);
            List<InvitationDto> invitations = prepareInvitations(cemetery);
            invitationSendService.sendInvitations(invitations);
        } catch (Exception exception) {
            log.error("Error while send invocation");
        }
    }

    private List<InvitationDto> prepareInvitations(CemeteryEntity cemetery) {
        List<String> emails = Lists.newArrayList();
        if (Objects.equals(cemetery.getOwner().getEmail().toLowerCase(), cemetery.getUserAdmin().getEmail().toLowerCase())) {
            emails.add(cemetery.getOwner().getEmail());
        } else {
            emails.add(cemetery.getOwner().getEmail());
            emails.add(cemetery.getUserAdmin().getEmail());
        }
        return emails.stream().map(emailArg -> InvitationDto.builder()
                        .email(emailArg)
                        .requestIdentifier(UUID.randomUUID().toString())
                        .institutionType(InstitutionType.CEMETERY)
                        .institutionId(cemetery.getId())
                        .build())
                .collect(toList());
    }

    public CemeteryDto getCemetery(Long cemeteryId) {
        CemeteryEntity cemeteryEntity = getCemeteryEntity(cemeteryId);
        return cemeteryMapper.toCemeteryDto(cemeteryEntity);
    }

    public CemeteryResponseDto getCemeteryResponse(Long cemeteryId) {
        CemeteryEntity cemeteryEntity = getCemeteryEntity(cemeteryId);

        CemeteryResponseDto cemeteryResponse = cemeteryMapper.toCemeteryResponseDto(cemeteryEntity);

        if (NATURAL_PERSON.name().equals(cemeteryEntity.getOwnerCategory().getCode())) {
            cemeteryResponse.setOwner(null);
        }

        return cemeteryResponse;
    }

    private CemeteryEntity getCemeteryEntity(Long cemeteryId) {
        return cemeteryRepository.findById(cemeteryId)
                .orElseThrow(() -> new CemeteryNotFoundException(cemeteryId));
    }

    private void sortCemeteries(List<CemeteryElementDto> cemeteries, Sort.Direction sortOrder) {
        cemeteries.sort(sortOrder.isAscending() ? getComparator() : getComparator().reversed());
    }

    private Comparator<CemeteryElementDto> getComparator() {
        return Comparator.comparing(CemeteryElementDto::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
    }

    public List<SimplifiedCemeteryElementDto> findSimplifiedCemeteriesByCriteria(SimplifiedCemeteryDto criteria) {
        if (criteria.getVoivodeshipTercCode() == null && criteria.getDistrictTercCode() == null && criteria.getCommuneTercCode() == null) {
            throw new MinimumCriteriaNotProvidedException();
        }
        List<CemeteryEntity> cemeteryEntityList = cemeteryRepository.findAll((root, query, criteriaBuilder) -> getPredicate(criteria, root, criteriaBuilder));
        return cemeteryMapper.toSimplifiedCemeteryElementDtoList(cemeteryEntityList);
    }

    private Predicate getPredicate(SimplifiedCemeteryDto criteria, Root<CemeteryEntity> root, CriteriaBuilder criteriaBuilder) {
        var predicates = new ArrayList<>();
        Join<CemeteryEntity, CemeterySourceDictionaryEntity> source = root.join(CemeteryEntity_.source);
        predicates.add(criteriaBuilder.equal(source.get(CemeterySourceDictionaryEntity_.id), PARTIAL_DATA));
        if (criteria.getVoivodeshipTercCode() != null || criteria.getDistrictTercCode() != null || criteria.getCommuneTercCode() != null) {
            Join<CemeteryEntity, CemeteryAddressEntity> locationAddress = root.join(CemeteryEntity_.locationAddress);
            if (criteria.getVoivodeshipTercCode() != null) {
                predicates.add(criteriaBuilder.equal(locationAddress.get(CemeteryAddressEntity_.voivodeshipTercCode), criteria.getVoivodeshipTercCode()));
            }
            if (criteria.getDistrictTercCode() != null) {
                predicates.add(criteriaBuilder.equal(locationAddress.get(CemeteryAddressEntity_.districtTercCode), criteria.getDistrictTercCode()));
            }
            if (criteria.getCommuneTercCode() != null) {
                predicates.add(criteriaBuilder.equal(locationAddress.get(CemeteryAddressEntity_.communeTercCode), criteria.getCommuneTercCode()));
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
