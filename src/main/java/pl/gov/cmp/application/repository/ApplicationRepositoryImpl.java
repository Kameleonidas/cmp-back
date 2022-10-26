package pl.gov.cmp.application.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.gov.cmp.application.model.dto.ApplicationCriteriaDto;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryApplicantEntity_;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity_;
import pl.gov.cmp.application.model.entity.ApplicationEntity;
import pl.gov.cmp.application.model.entity.ApplicationEntity_;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;
import pl.gov.cmp.auth.model.entity.UserAccountEntity_;
import pl.gov.cmp.dictionary.model.entity.CemeteryFacilityTypeDictionaryEntity_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class ApplicationRepositoryImpl implements ApplicationCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ApplicationEntity> findByCriteria(ApplicationCriteriaDto criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ApplicationEntity> query = createQuery(criteria, cb);

        TypedQuery<ApplicationEntity> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
        typedQuery.setMaxResults(criteria.getPageSize());

        List<ApplicationEntity> applicationEntityList = typedQuery.getResultList();
        Pageable pageable = PageRequest.of(criteria.getPageIndex(), criteria.getPageSize());
        Long totalElements = countByCriteria(criteria);

        return new PageImpl<>(applicationEntityList, pageable, totalElements);
    }


    private CriteriaQuery<ApplicationEntity> createQuery(ApplicationCriteriaDto criteria, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<ApplicationEntity> query = criteriaBuilder.createQuery(ApplicationEntity.class);

        Root<ApplicationEntity> root = query.from(ApplicationEntity.class);
        root.join(ApplicationEntity_.application, JoinType.LEFT);
        List<Order> listaSortowania = new ArrayList<>();

        List<Predicate> predicates = initPredicates(criteria, criteriaBuilder, root);
        query = query.where(predicates.toArray(new Predicate[0]));
        uzupelnijParametrySortowania(criteriaBuilder, root, listaSortowania, criteria);
        return query;
    }

    private List<Predicate> initPredicates(ApplicationCriteriaDto criteria, CriteriaBuilder criteriaBuilder, Root<ApplicationEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getUserId() != null) {
            Path<Long> userId = root.get(ApplicationEntity_.application).get(ApplicationCemeteryEntity_.applicant).get(ApplicationCemeteryApplicantEntity_.userId);
            predicates.add(criteriaBuilder.equal(userId, criteria.getUserId()));
        }
        if (isNotBlank(criteria.getAppNumber())) {
            Path<String> appNumber = root.get(ApplicationEntity_.appNumber);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(appNumber), "%" + criteria.getAppNumber().toLowerCase() + "%"));
        }
        if (criteria.getAppType() != null) {
            Path<ApplicationType> appType = root.get(ApplicationEntity_.appType);
            predicates.add(criteriaBuilder.equal(appType, criteria.getAppType()));
        }
        if (isNotBlank(criteria.getCemeteryFacilityType())) {
            Path<String> facilityType = root.get(ApplicationEntity_.application).get(ApplicationCemeteryEntity_.facilityType).get(CemeteryFacilityTypeDictionaryEntity_.code);
            predicates.add(criteriaBuilder.equal(facilityType, criteria.getCemeteryFacilityType()));
        }
        if (isNotBlank(criteria.getApplicantFirstName())) {
            Path<String> applicantFirstName = root.get(ApplicationEntity_.application).get(ApplicationCemeteryEntity_.applicant).get(ApplicationCemeteryApplicantEntity_.firstName);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(applicantFirstName), "%" + criteria.getApplicantFirstName().toLowerCase() + "%"));
        }
        if (isNotBlank(criteria.getApplicantLastName())) {
            Path<String> applicantLastName = root.get(ApplicationEntity_.application).get(ApplicationCemeteryEntity_.applicant).get(ApplicationCemeteryApplicantEntity_.lastName);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(applicantLastName), "%" + criteria.getApplicantLastName().toLowerCase() + "%"));
        }
        if (isNotBlank(criteria.getObjectName())) {
            Path<String> objectName = root.get(ApplicationEntity_.application).get(ApplicationCemeteryEntity_.objectName);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(objectName), "%" + criteria.getObjectName().toLowerCase() + "%"));
        }
        if (isNotBlank(criteria.getUserFirstName())) {
            Path<String> userFirstName = root.get(ApplicationEntity_.operator).get(UserAccountEntity_.firstName);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userFirstName), "%" + criteria.getApplicantFirstName().toLowerCase() + "%"));
        }
        if (isNotBlank(criteria.getUserLastName())) {
            Path<String> userLastName = root.get(ApplicationEntity_.operator).get(UserAccountEntity_.lastName);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userLastName), "%" + criteria.getApplicantLastName().toLowerCase() + "%"));
        }
        if (isNotBlank(criteria.getCreatedDateFrom()) && isNotBlank(criteria.getCreatedDateTo())) {
            Path<LocalDateTime> createdDate = root.get(ApplicationEntity_.createDate);
            predicates.add(criteriaBuilder.between(createdDate, LocalDateTime.parse(criteria.getCreatedDateFrom()), LocalDateTime.parse(criteria.getCreatedDateTo())));
        }

        if (isNotBlank(criteria.getModificationDateFrom()) && isNotBlank(criteria.getModificationDateTo())) {
            Path<LocalDateTime> updateDate = root.get(ApplicationEntity_.updateDate);
            predicates.add(criteriaBuilder.between(updateDate, LocalDateTime.parse(criteria.getModificationDateFrom()), LocalDateTime.parse(criteria.getModificationDateTo())));
        }
        if (criteria.getAppStatus() != null && !criteria.getAppStatus().isEmpty()) {
            Path<ApplicationStatus> status = root.get(ApplicationEntity_.appStatus);
            In<ApplicationStatus> inPredicateStatus = criteriaBuilder.in(status);
            criteria.getAppStatus().forEach(inPredicateStatus::value);
            predicates.add(inPredicateStatus);
        }

        return predicates;
    }

    private void uzupelnijParametrySortowania(CriteriaBuilder cb, Root<ApplicationEntity> root, List<Order> listaSortowania, ApplicationCriteriaDto criteria) {
        if (criteria.getSortColumn() != null) {
            String poleSortowania = criteria.getSortColumn();
            var kierunek = criteria.getSortOrder();

            Path<ApplicationEntity> sciezkaAtrybutuWniosku = getSciezkaAtrybutu(root, poleSortowania);

            if (kierunek != null) {
                if (kierunek.equals(Sort.Direction.DESC)) {
                    listaSortowania.add(cb.desc(sciezkaAtrybutuWniosku));
                } else if (kierunek.equals(Sort.Direction.ASC)) {
                    listaSortowania.add(cb.asc(sciezkaAtrybutuWniosku));
                }
            }
        }
    }

    private Path<ApplicationEntity> getSciezkaAtrybutu(Root<ApplicationEntity> root, String nazwaAtrybutu) {
        Path<ApplicationEntity> path = root;
        for (String part : nazwaAtrybutu.split("\\.")) {
            path = path.get(part);
        }
        return path;
    }

    private Long countByCriteria(ApplicationCriteriaDto criteria) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<ApplicationEntity> root = criteriaQuery.from(ApplicationEntity.class);
        root.join(ApplicationEntity_.application, JoinType.LEFT);

        List<Predicate> predicates = initPredicates(criteria, criteriaBuilder, root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.select(criteriaBuilder.countDistinct(root));

        return this.entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
