package pl.gov.cmp.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Collection;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class UserAccountDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String roleId;
    private String wkId;
    private String localId;
    private LocalDate birthDate;
    private Collection<UserAccountToSubjectDto> subjects;

}
