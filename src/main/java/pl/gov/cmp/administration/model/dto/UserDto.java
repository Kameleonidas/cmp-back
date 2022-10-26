package pl.gov.cmp.administration.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String status;
    private Set<String> categories;
}
