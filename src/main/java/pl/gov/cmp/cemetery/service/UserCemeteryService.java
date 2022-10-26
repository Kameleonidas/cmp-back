package pl.gov.cmp.cemetery.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryCriteriaDto;
import pl.gov.cmp.cemetery.model.dto.UserCemeteryElementDto;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.mapper.UserCemeteryMapper;
import pl.gov.cmp.cemetery.repository.CemeteryRepository;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Transactional
@Service
public class UserCemeteryService {

    private final CemeteryRepository cemeteryRepository;
    private final UserCemeteryMapper userCemeteryMapper;

    public Page<UserCemeteryElementDto> findByCriteria(UserCemeteryCriteriaDto criteria, Set<Long> cemeteryIds) {
        PageRequest pageRequest = createPageRequest(criteria);
        if (!cemeteryIds.isEmpty()) {
            Page<CemeteryEntity> cemeteryPage = this.cemeteryRepository.findByIds(cemeteryIds, pageRequest);
            List<UserCemeteryElementDto> elements = this.userCemeteryMapper.toUserCemeteryElementDtoList(cemeteryPage.getContent());
            return new PageImpl<>(elements, cemeteryPage.getPageable(), cemeteryPage.getTotalElements());
        } else {
            return new PageImpl<>(Lists.newArrayList(), pageRequest, 0);
        }
    }

    private PageRequest createPageRequest(UserCemeteryCriteriaDto criteria) {
        if (criteria.getSortColumn() != null) {
            return PageRequest.of(criteria.getPageIndex(), criteria.getPageSize(), this.getSort(criteria));
        }
        return PageRequest.of(criteria.getPageIndex(), criteria.getPageSize());
    }

    private Sort getSort(UserCemeteryCriteriaDto criteria) {
        if (criteria.getSortOrder() == Sort.Direction.ASC) {
            return Sort.by(criteria.getSortColumn()).ascending();
        }
        return Sort.by(criteria.getSortColumn()).descending();
    }
}
