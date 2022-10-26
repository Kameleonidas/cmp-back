package pl.gov.cmp.administration.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.auth.model.enums.UserAccountStatusEnum;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserPageRequest {
    @Schema(description="The sort column", example = "firstName, lastName, birthDate, status")
    private String sortColumn;
    @Schema(description="The sort order")
    private Sort.Direction sortOrder;
    @Schema(description="The page index")
    @Min(0)
    private int pageIndex;
    @Schema(description="The page size")
    @Min(1)
    private int pageSize;

    @Schema(description="The user first name")
    private String firstName;
    @Schema(description="The user last name")
    private String lastName;
    @Schema(description="The user birth date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @Schema(description="Filter statuses")
    private Set<UserAccountStatusEnum> statuses;
    @Schema(description="Filter categories")
    private Set<ObjectCategoryEnum> categories;
}
