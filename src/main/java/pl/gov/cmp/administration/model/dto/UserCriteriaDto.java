package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.model.enums.UserAccountStatusEnum;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserCriteriaDto {
    private String sortColumn;
    private Sort.Direction sortOrder;
    private int pageIndex;
    private int pageSize;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Set<UserAccountStatusEnum> statuses;
    private Set<ObjectCategoryEnum> categories;
}
