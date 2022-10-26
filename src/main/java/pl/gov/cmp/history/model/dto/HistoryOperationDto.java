package pl.gov.cmp.history.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import pl.gov.cmp.history.model.enums.HistoryOperationType;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class HistoryOperationDto {

    private HistoryOperationType type;
    private Long applicationId;
    private Long cemeteryId;
    private Long graveId;
    private Long userAccountId;
    private Long applicationCemeteryStaffId;
    private Long cemeteryStaffId;
    @Singular
    private List<String> params;

}
