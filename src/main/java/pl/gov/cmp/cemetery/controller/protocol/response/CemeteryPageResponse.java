package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class CemeteryPageResponse {

    private List<CemeteryElementResponse> elements = Collections.emptyList();
    private int pageIndex;
    private int totalPages;
    private long totalElements;
    private String sortColumn;
    private Sort.Direction sortOrder;

}
