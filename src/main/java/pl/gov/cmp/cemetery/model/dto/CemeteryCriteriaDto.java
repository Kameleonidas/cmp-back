package pl.gov.cmp.cemetery.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
public class CemeteryCriteriaDto {

    private String sortColumn;
    private Sort.Direction sortOrder;
    private int pageIndex;
    private int pageSize;

    private String phrase;
    private CemeteryStatus status;
    private String facilityTypeCode;
    private String sourceCode;
    private String typeCode;
    private String religionCode;
    private String ownerCategoryCode;
    private TermType openTermType;
    private TermType closeTermType;
    private LocalDate createDate;
    private LocalDate openDate;
    private LocalDate closeDate;
    private Boolean substitutePerformance;
    private Boolean perpetualUse;
    private Boolean churchPerpetualUser;
    private Boolean churchOwner;
    private Boolean churchRegulatedByLaw;
    private Boolean managerExists;
    private Boolean userAdminExists;
    private Set<String> voivodeshipTercCodes = Collections.emptySet();
    private Set<String> districtTercCodes = Collections.emptySet();
    private Set<String> communeTercCodes = Collections.emptySet();
    private Set<String> placeSimcCodes = Collections.emptySet();

}
