package pl.gov.cmp.application.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;

import java.util.List;

@Getter
@Setter
@Builder
public class ApplicationCriteriaDto {
    private String sortColumn;
    private Sort.Direction sortOrder;
    private int pageIndex;
    private int pageSize;
    private Long userId;

    private String appNumber;
    private ApplicationType appType;
    private List<ApplicationStatus> appStatus;
    private String applicantFirstName;
    private String applicantLastName;
    private String userFirstName;
    private String userLastName;
    private String cemeteryFacilityType;
    private String objectName;
    private String createdDateFrom;
    private String createdDateTo;
    private String modificationDateFrom;
    private String modificationDateTo;

}
