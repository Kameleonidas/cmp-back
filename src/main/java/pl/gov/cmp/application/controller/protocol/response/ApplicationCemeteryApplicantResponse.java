package pl.gov.cmp.application.controller.protocol.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApplicationCemeteryApplicantResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}
