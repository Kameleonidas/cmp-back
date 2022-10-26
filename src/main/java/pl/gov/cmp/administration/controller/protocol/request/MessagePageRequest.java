package pl.gov.cmp.administration.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class MessagePageRequest {

    @Min(0)
    private int pageIndex;
    @Min(1)
    private int pageSize;

    private Long userAccountToSubjectId;
}
