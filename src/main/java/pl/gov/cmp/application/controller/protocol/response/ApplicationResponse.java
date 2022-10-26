package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationResponse {

    private Long id;
    private Long cemeteryAppId;
    private String appNumber;
    private String appType;
    private String appStatus;
    private String objectName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    private String applicantFirstName;
    private String applicantLastName;
    private String userFirstName;
    private String userLastName;
    private String cemeteryFacilityType;
}
