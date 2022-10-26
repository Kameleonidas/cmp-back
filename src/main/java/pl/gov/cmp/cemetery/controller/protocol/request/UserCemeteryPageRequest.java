package pl.gov.cmp.cemetery.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;
import java.time.LocalDate;

@Getter
@Setter
public class UserCemeteryPageRequest {
    @Schema(description="The sort column", example = "name, type, createDate")
    private String sortColumn;
    @Schema(description="The sort order")
    private Sort.Direction sortOrder;
    @Schema(description="The page index")
    @Min(0)
    private int pageIndex;
    @Schema(description="The page size")
    @Min(1)
    private int pageSize;
}
