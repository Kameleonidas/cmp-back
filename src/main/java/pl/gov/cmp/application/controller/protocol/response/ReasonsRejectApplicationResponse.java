package pl.gov.cmp.application.controller.protocol.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.cemetery.controller.protocol.request.ReasonsRejectionApplication;

@Builder
@Getter
@Setter
public class ReasonsRejectApplicationResponse {
        Long applicationId;
        ReasonsRejectionApplication reasonsRejectionApplication;
        String description;
}
