package pl.gov.cmp.application.controller.protocol.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Builder
@Getter
@Setter
public class ApplicationDraftPageResponse {

    private List<ApplicationDraftResponse> elements;

    private int pageIndex;
    private int totalPages;
    private long totalElements;
    private String sortColumn;
    private Sort.Direction sortOrder;
}
