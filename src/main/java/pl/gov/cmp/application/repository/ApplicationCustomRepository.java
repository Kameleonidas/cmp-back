package pl.gov.cmp.application.repository;

import org.springframework.data.domain.Page;
import pl.gov.cmp.application.model.dto.ApplicationCriteriaDto;
import pl.gov.cmp.application.model.entity.ApplicationEntity;

public interface ApplicationCustomRepository {
    Page<ApplicationEntity> findByCriteria(ApplicationCriteriaDto criteria);
}
