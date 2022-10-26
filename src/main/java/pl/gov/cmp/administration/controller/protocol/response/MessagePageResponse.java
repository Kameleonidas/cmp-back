package pl.gov.cmp.administration.controller.protocol.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class MessagePageResponse {
    private List<MessageResponse> elements;

    private int pageIndex;
    private int totalPages;
    private long totalElements;
}
