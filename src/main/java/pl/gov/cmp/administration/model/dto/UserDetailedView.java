package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Value
@Builder
public class UserDetailedView {

    long id;
    String firstName;
    String lastName;
    String personalIdentifier;
    String status;
    LocalDate birthDate;
    Instant firstLoginAt;
    Instant lastLoginAt;
    List<UserAccountToSubjectDetailedView> subjects;

}
