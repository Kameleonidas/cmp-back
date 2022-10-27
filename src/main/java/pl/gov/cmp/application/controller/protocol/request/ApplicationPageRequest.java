package pl.gov.cmp.application.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import pl.gov.cmp.application.model.enums.ApplicationStatus;

import javax.validation.constraints.Min;
import java.util.List;

@Getter
@Setter
public class ApplicationPageRequest {

    private String sortColumn;
    private Sort.Direction sortOrder;
    @Min(0)
    private int pageIndex;
    @Min(1)
    private int pageSize;

    private String appNumber;
    private String appType;
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
