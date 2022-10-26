package pl.gov.cmp.cemetery.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
public class CemeteryPageRequest {

    private String sortColumn;
    private Sort.Direction sortOrder;
    @Min(0)
    private int pageIndex;
    @Min(1)
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
