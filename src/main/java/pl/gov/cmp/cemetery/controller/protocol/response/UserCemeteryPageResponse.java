package pl.gov.cmp.cemetery.controller.protocol.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class UserCemeteryPageResponse {

    private List<UserCemeteryElementResponse> elements = Collections.emptyList();
    @Schema(description="The page index")
    private int pageIndex;
    @Schema(description="Total number of pages")
    private int totalPages;
    @Schema(description="Total number of elements")
    private long totalElements;
    @Schema(description="The column the collection is sorted by")
    private String sortColumn;
    @Schema(description="The order the collection is sorted by")
    private Sort.Direction sortOrder;

}
