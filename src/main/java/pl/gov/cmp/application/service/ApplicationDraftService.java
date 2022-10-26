package pl.gov.cmp.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryDraftRequest;
import pl.gov.cmp.application.model.dto.ApplicationCemeteryDraftCriteriaDto;
import pl.gov.cmp.application.model.dto.ApplicationDraftDto;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryDraftEntity;
import pl.gov.cmp.application.model.enums.ApplicationDraftColumn;
import pl.gov.cmp.application.model.mapper.ApplicationDraftDtoMapper;
import pl.gov.cmp.application.repository.ApplicationCemeteryDraftRepository;
import pl.gov.cmp.application.repository.ApplicationCustomDraftRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ApplicationDraftService {
    private final ApplicationDraftDtoMapper applicationDraftDtoMapper;
    private final ApplicationCustomDraftRepository applicationCustomDraftRepository;
    private final ApplicationCemeteryDraftRepository applicationCemeteryDraftRepository;

    public void saveApplicationCemeteryDraft(ApplicationCemeteryDraftRequest request, Long userId) {
        applicationCemeteryDraftRepository.save(ApplicationCemeteryDraftEntity.createWithDraftAndUserId(request, userId));
    }

    public List<ApplicationDraftDto> getApplicationCemeteryDraft(Long userId) {
        List<ApplicationCemeteryDraftEntity> applicationDraft = applicationCemeteryDraftRepository.findByUserAccountId(userId);
        return applicationDraft
                .stream()
                .map(draft -> ApplicationDraftDto.builder()
                        .draft(draft.getDraft())
                        .updateDate(draft.getUpdateDate())
                        .createDate(draft.getCreateDate())
                        .userAccountId(draft.getUserAccountId())
                        .id(draft.getId())
                        .draftName(draft.getDraftName())
                        .build())
                .collect(Collectors.toList());
    }

    public Page<ApplicationDraftDto> findDraftByCriteria(ApplicationCemeteryDraftCriteriaDto criteria) {
        Page<ApplicationCemeteryDraftEntity> applicationDraftPage = applicationCustomDraftRepository.findByCriteria(criteria);
        List<ApplicationDraftDto> elements = applicationDraftDtoMapper.toApplicationDraftDtoList(applicationDraftPage.getContent());
        sortApplicationDraft(elements, criteria.getSortColumn(), criteria.getSortOrder());
        return new PageImpl<>(elements, applicationDraftPage.getPageable(), applicationDraftPage.getTotalElements());
    }

    private void sortApplicationDraft(List<ApplicationDraftDto> elements, String sortColumn, Sort.Direction sortOrder) {
        elements.sort(sortOrder.isAscending() ? getComparator(sortColumn) : getComparator(sortColumn).reversed());
    }

    private Comparator<ApplicationDraftDto> getComparator(String sortColumn) {
        Comparator<ApplicationDraftDto> applicationDtoComparator;
        switch (ApplicationDraftColumn.valueOf(sortColumn)) {
            case DRAFT_NAME:
                applicationDtoComparator = Comparator.comparing(a -> a.getDraftName().toLowerCase());
                break;
            case CREATE_DATE:
                applicationDtoComparator = Comparator.comparing(ApplicationDraftDto::getCreateDate);
                break;
            case UPDATE_DATE:
                applicationDtoComparator = Comparator.comparing(ApplicationDraftDto::getUpdateDate);
                break;
            default:
                applicationDtoComparator = Comparator.comparing(ApplicationDraftDto::getId);
                break;
        }
        return applicationDtoComparator;
    }
}
