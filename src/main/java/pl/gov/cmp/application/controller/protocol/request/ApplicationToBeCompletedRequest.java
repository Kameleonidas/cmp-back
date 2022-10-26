package pl.gov.cmp.application.controller.protocol.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationToBeCompletedRequest {
    private String applicationNumber;
    private String fieldsToBeCompleted;
}
