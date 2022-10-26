package pl.gov.cmp.cemetery.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.dto.CemeteryCriteriaDto;
import pl.gov.cmp.cemetery.model.entity.CemeteryAddressEntity_;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity;
import pl.gov.cmp.cemetery.model.entity.CemeteryEntity_;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;
import pl.gov.cmp.dictionary.model.entity.CemeteryFacilityTypeDictionaryEntity_;
import pl.gov.cmp.dictionary.model.entity.CemeteryOwnerCategoryDictionaryEntity_;
import pl.gov.cmp.dictionary.model.entity.CemeterySourceDictionaryEntity_;
import pl.gov.cmp.dictionary.model.entity.CemeteryTypeDictionaryEntity_;
import pl.gov.cmp.dictionary.model.entity.ChurchReligionDictionaryEntity_;

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
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class CemeteryCustomRepositoryImpl implements CemeteryCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CemeteryEntity> findByCriteria(CemeteryCriteriaDto criteria) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<CemeteryEntity> criteriaQuery = criteriaBuilder.createQuery(CemeteryEntity.class);

        Root<CemeteryEntity> root = criteriaQuery.from(CemeteryEntity.class);
        root.fetch(CemeteryEntity_.source, JoinType.LEFT);
        root.fetch(CemeteryEntity_.facilityType, JoinType.LEFT);
        root.fetch(CemeteryEntity_.type, JoinType.LEFT);
        root.fetch(CemeteryEntity_.religion, JoinType.LEFT);
        root.fetch(CemeteryEntity_.ownerCategory, JoinType.LEFT);
        root.fetch(CemeteryEntity_.locationAddress, JoinType.LEFT);

        List<Long> cemeteryIds = findIdsByCriteria(criteria);
        In<Long> inPredicate = criteriaBuilder.in(root.get(CemeteryEntity_.id));
        cemeteryIds.forEach(inPredicate::value);

        criteriaQuery.select(root).distinct(true);
        criteriaQuery.where(inPredicate);

        TypedQuery<CemeteryEntity> query = this.entityManager.createQuery(criteriaQuery);
        List<CemeteryEntity> cemeteries = query.getResultList();

        Pageable pageable = PageRequest.of(criteria.getPageIndex(), criteria.getPageSize());
        Long totalElements = countByCriteria(criteria);
        return new PageImpl<>(cemeteries, pageable, totalElements);
    }

    private List<Long> findIdsByCriteria(CemeteryCriteriaDto criteria) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<CemeteryRow> criteriaQuery = criteriaBuilder.createQuery(CemeteryRow.class);

        Root<CemeteryEntity> root = criteriaQuery.from(CemeteryEntity.class);
        root.join(CemeteryEntity_.source, JoinType.LEFT);
        root.join(CemeteryEntity_.facilityType, JoinType.LEFT);
        root.join(CemeteryEntity_.type, JoinType.LEFT);
        root.join(CemeteryEntity_.religion, JoinType.LEFT);
        root.join(CemeteryEntity_.ownerCategory, JoinType.LEFT);
        root.join(CemeteryEntity_.locationAddress, JoinType.LEFT);

        List<Predicate> predicates = initPredicates(criteria, criteriaBuilder, root);
        Order order = initOrder(criteria, criteriaBuilder, root);

        criteriaQuery.select(criteriaBuilder.construct(CemeteryRow.class, root.get(CemeteryEntity_.id), root.get(CemeteryEntity_.name))).distinct(true);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(order);

        TypedQuery<CemeteryRow> query = this.entityManager.createQuery(criteriaQuery);
        query.setFirstResult(criteria.getPageIndex() * criteria.getPageSize());
        query.setMaxResults(criteria.getPageSize());

        return query.getResultList().stream().map(CemeteryRow::getId).collect(Collectors.toList());
    }

    private Long countByCriteria(CemeteryCriteriaDto criteria) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<CemeteryEntity> root = criteriaQuery.from(CemeteryEntity.class);
        root.join(CemeteryEntity_.source, JoinType.LEFT);
        root.join(CemeteryEntity_.facilityType, JoinType.LEFT);
        root.join(CemeteryEntity_.type, JoinType.LEFT);
        root.join(CemeteryEntity_.religion, JoinType.LEFT);
        root.join(CemeteryEntity_.ownerCategory, JoinType.LEFT);
        root.join(CemeteryEntity_.locationAddress, JoinType.LEFT);
        root.join(CemeteryEntity_.contactAddress, JoinType.LEFT);
        root.join(CemeteryEntity_.cemeteryPerpetualUser, JoinType.LEFT);
        root.join(CemeteryEntity_.owner, JoinType.LEFT);
        root.join(CemeteryEntity_.manager, JoinType.LEFT);
        root.join(CemeteryEntity_.userAdmin, JoinType.LEFT);
        root.join(CemeteryEntity_.attachmentFiles, JoinType.LEFT);

        List<Predicate> predicates = initPredicates(criteria, criteriaBuilder, root);

        criteriaQuery.select(criteriaBuilder.countDistinct(root));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return this.entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private List<Predicate> initPredicates(CemeteryCriteriaDto criteria, CriteriaBuilder criteriaBuilder, Root<CemeteryEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (isNotBlank(criteria.getPhrase())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.name);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(fieldPath), "%" + criteria.getPhrase().toLowerCase() + "%"));
        }
        Path<CemeteryStatus> statusPath = root.get(CemeteryEntity_.status);
        if (criteria.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(statusPath, criteria.getStatus()));
        }
        predicates.add(criteriaBuilder.notEqual(statusPath, CemeteryStatus.LIQUIDATED));
        if (isNotBlank(criteria.getFacilityTypeCode())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.facilityType).get(CemeteryFacilityTypeDictionaryEntity_.code);
            predicates.add(criteriaBuilder.equal(fieldPath, criteria.getFacilityTypeCode()));
        }
        if (isNotBlank(criteria.getSourceCode())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.source).get(CemeterySourceDictionaryEntity_.code);
            predicates.add(criteriaBuilder.equal(fieldPath, criteria.getSourceCode()));
        }
        if (isNotBlank(criteria.getTypeCode())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.type).get(CemeteryTypeDictionaryEntity_.code);
            predicates.add(criteriaBuilder.equal(fieldPath, criteria.getTypeCode()));
        }
        if (isNotBlank(criteria.getReligionCode())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.religion).get(ChurchReligionDictionaryEntity_.code);
            predicates.add(criteriaBuilder.equal(fieldPath, criteria.getReligionCode()));
        }
        if (isNotBlank(criteria.getOwnerCategoryCode())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.ownerCategory).get(CemeteryOwnerCategoryDictionaryEntity_.code);
            predicates.add(criteriaBuilder.equal(fieldPath, criteria.getOwnerCategoryCode()));
        }
        if (criteria.getOpenTermType() != null) {
            Path<TermType> openTermType = root.get(CemeteryEntity_.openTermType);
            predicates.add(criteriaBuilder.equal(openTermType, criteria.getOpenTermType()));
        }
        if (criteria.getOpenTermType() != null) {
            Path<TermType> closeTermType = root.get(CemeteryEntity_.closeTermType);
            predicates.add(criteriaBuilder.equal(closeTermType, criteria.getCloseTermType()));
        }
        if (criteria.getSubstitutePerformance() != null) {
            Path<Boolean> closeTermType = root.get(CemeteryEntity_.substitutePerformance);
            predicates.add(criteriaBuilder.equal(closeTermType, criteria.getSubstitutePerformance()));
        }
        if (criteria.getChurchPerpetualUser() != null) {
            Path<Boolean> perpetualUser = root.get(CemeteryEntity_.churchPerpetualUser);
            predicates.add(criteriaBuilder.equal(perpetualUser, criteria.getChurchPerpetualUser()));
        }
        if (criteria.getChurchOwner() != null) {
            Path<Boolean> churchOwner = root.get(CemeteryEntity_.churchOwner);
            predicates.add(criteriaBuilder.equal(churchOwner, criteria.getChurchOwner()));
        }
        if (criteria.getChurchRegulatedByLaw() != null) {
            Path<Boolean> churchRegulatedByLaw = root.get(CemeteryEntity_.churchRegulatedByLaw);
            predicates.add(criteriaBuilder.equal(churchRegulatedByLaw, criteria.getChurchRegulatedByLaw()));
        }
        if (criteria.getManagerExists() != null) {
            Path<Boolean> managerExists = root.get(CemeteryEntity_.managerExists);
            predicates.add(criteriaBuilder.equal(managerExists, criteria.getManagerExists()));
        }
        if (criteria.getUserAdminExists() != null) {
            Path<Boolean> userAdminExists = root.get(CemeteryEntity_.userAdminExists);
            predicates.add(criteriaBuilder.equal(userAdminExists, criteria.getUserAdminExists()));
        }
        List<Predicate> addressPredicates = initAddressPredicates(criteria, criteriaBuilder, root);
        if (!CollectionUtils.isEmpty(addressPredicates)) {
            predicates.add(criteriaBuilder.or(addressPredicates.toArray(Predicate[]::new)));
        }
        return predicates;
    }

    private List<Predicate> initAddressPredicates(CemeteryCriteriaDto criteria, CriteriaBuilder criteriaBuilder, Root<CemeteryEntity> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!CollectionUtils.isEmpty(criteria.getVoivodeshipTercCodes())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.locationAddress).get(CemeteryAddressEntity_.voivodeshipTercCode);
            In<String> inPredicate = criteriaBuilder.in(fieldPath);
            criteria.getVoivodeshipTercCodes().forEach(inPredicate::value);
            predicates.add(inPredicate);
        }
        if (!CollectionUtils.isEmpty(criteria.getDistrictTercCodes())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.locationAddress).get(CemeteryAddressEntity_.districtTercCode);
            In<String> inPredicate = criteriaBuilder.in(fieldPath);
            criteria.getDistrictTercCodes().forEach(inPredicate::value);
            predicates.add(inPredicate);
        }
        if (!CollectionUtils.isEmpty(criteria.getCommuneTercCodes())) {
            Path<String> fieldPathCommune = root.get(CemeteryEntity_.locationAddress).get(CemeteryAddressEntity_.communeTercCode);
            In<String> inPredicate = criteriaBuilder.in(fieldPathCommune);
            Path<String> fieldPathDistrict = root.get(CemeteryEntity_.locationAddress).get(CemeteryAddressEntity_.districtTercCode);
            In<String> inPredicateDistrict = criteriaBuilder.in(fieldPathDistrict);
            Set<String> communeTercCodes = new HashSet<>();
            Set<String> communeTercCodesToDistrict = new HashSet<>();
            criteria.getCommuneTercCodes().forEach(x -> {
                if (x.endsWith("011")) {
                    communeTercCodesToDistrict.add(x.substring(0, 4));
                } else {
                    communeTercCodes.add(x);
                }
            });

            communeTercCodes.forEach(inPredicate::value);
            communeTercCodesToDistrict.forEach(inPredicateDistrict::value);
            predicates.add(inPredicate);
            predicates.add(inPredicateDistrict);
        }
        if (!CollectionUtils.isEmpty(criteria.getPlaceSimcCodes())) {
            Path<String> fieldPath = root.get(CemeteryEntity_.locationAddress).get(CemeteryAddressEntity_.placeSimcCode);
            In<String> inPredicate = criteriaBuilder.in(fieldPath);
            criteria.getPlaceSimcCodes().forEach(inPredicate::value);
            predicates.add(inPredicate);
        }
        return predicates;
    }

    private Order initOrder(CemeteryCriteriaDto criteria, CriteriaBuilder criteriaBuilder, Root<CemeteryEntity> root) {
        Path<String> fieldPath = root.get(CemeteryEntity_.name);
        return criteria.getSortOrder().isAscending() ? criteriaBuilder.asc(fieldPath) : criteriaBuilder.desc(fieldPath);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class CemeteryRow {
        private Long id;
        private String name;
    }
}
