package pl.gov.cmp.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ApplicationCemeteryApplicantDto {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

}
