package pl.gov.cmp.application.repository;

import org.springframework.data.domain.Page;
import pl.gov.cmp.application.model.dto.ApplicationCemeteryDraftCriteriaDto;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryDraftEntity;

public interface ApplicationCustomDraftRepository {

    Page<ApplicationCemeteryDraftEntity> findByCriteria(ApplicationCemeteryDraftCriteriaDto criteria);

}
