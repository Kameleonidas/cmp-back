package pl.gov.cmp.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountToSubjectDto {
    private Long id;
    private boolean activeEmployee;
    private String email;
    private Long cemeteryId;
    private Long ipnId;
    private Long voivodshipId;
    private Long crematoriumId;
    private ObjectCategoryEnum category;
}
