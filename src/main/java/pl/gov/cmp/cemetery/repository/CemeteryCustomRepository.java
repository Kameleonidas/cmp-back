package pl.gov.cmp.cemetery.repository;

import org.springframework.data.domain.Page;
import pl.gov.cmp.cemetery.model.dto.CemeteryCriteriaDto;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;

public interface CemeteryCustomRepository {

    Page<CemeteryEntity> findByCriteria(CemeteryCriteriaDto criteria);

}
