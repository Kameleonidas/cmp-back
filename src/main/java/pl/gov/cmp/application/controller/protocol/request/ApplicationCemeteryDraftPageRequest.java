package pl.gov.cmp.application.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

@Getter
@Setter
public class ApplicationCemeteryDraftPageRequest {

    private String sortColumn;
    private Sort.Direction sortOrder;
    @Min(0)
    private int pageIndex;
    @Min(1)
    private int pageSize;

    private long userId;
}
