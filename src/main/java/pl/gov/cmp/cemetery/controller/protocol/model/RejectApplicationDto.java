package pl.gov.cmp.cemetery.controller.protocol.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.cemetery.controller.protocol.request.ReasonsRejectionApplication;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RejectApplicationDto {
    ReasonsRejectionApplication reasonRejectionApplication;
    Long applicationId;
    String description;
}
