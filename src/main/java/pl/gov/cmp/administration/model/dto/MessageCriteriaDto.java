package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MessageCriteriaDto {
    private int pageIndex;
    private int pageSize;

    private Long userAccountToSubjectId;
}
