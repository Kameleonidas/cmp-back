package pl.gov.cmp.cemetery.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Builder
@Getter
@Setter
public class UserCemeteryCriteriaDto {

    private String sortColumn;
    private Sort.Direction sortOrder;
    private int pageIndex;
    private int pageSize;
}
