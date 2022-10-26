package pl.gov.cmp.auth.controller.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserAccountResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String wkId;
    private String localId;
    private LocalDate birthDate;
}
