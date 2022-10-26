package pl.gov.cmp.application.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.gov.cmp.application.model.dto.ApplicationCemeteryDraftCriteriaDto;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryDraftEntity;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryDraftEntity_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationCemeteryDraftRepositoryImpl implements ApplicationCustomDraftRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ApplicationCemeteryDraftEntity> findByCriteria(ApplicationCemeteryDraftCriteriaDto criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicationCemeteryDraftEntity> criteriaQuery = cb.createQuery(ApplicationCemeteryDraftEntity.class);
        Root<ApplicationCemeteryDraftEntity> root = criteriaQuery.from(ApplicationCemeteryDraftEntity.class);

        List<Predicate> predicates = initPredicates(criteria, cb, root);
        criteriaQuery.select(root).distinct(true);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ApplicationCemeteryDraftEntity> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(criteria.getPageSize());
        query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
        List<ApplicationCemeteryDraftEntity> applicationEntityList = query.getResultList();
        Pageable pageable = PageRequest.of(criteria.getPageIndex(), criteria.getPageSize());
        Long totalElements = countByCriteria(criteria);

        return new PageImpl<>(applicationEntityList, pageable, totalElements);
    }

    private Long countByCriteria(ApplicationCemeteryDraftCriteriaDto criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ApplicationCemeteryDraftEntity> root = criteriaQuery.from(ApplicationCemeteryDraftEntity.class);
        List<Predicate> predicates = initPredicates(criteria, criteriaBuilder, root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.select(criteriaBuilder.countDistinct(root));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private List<Predicate> initPredicates(ApplicationCemeteryDraftCriteriaDto criteria, CriteriaBuilder criteriaBuilder, Root<ApplicationCemeteryDraftEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get(ApplicationCemeteryDraftEntity_.userAccountId), criteria.getUserId()));
        predicates.add(criteriaBuilder.greaterThan(root.get(ApplicationCemeteryDraftEntity_.UPDATE_DATE), LocalDate.now().minusDays(30).atStartOfDay()));
        return predicates;
    }

}
