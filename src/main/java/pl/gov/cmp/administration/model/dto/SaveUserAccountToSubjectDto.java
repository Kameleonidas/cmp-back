package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Value;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;

@Builder
@Value
public class SaveUserAccountToSubjectDto {

    long institutionId;
    ObjectCategoryEnum institutionType;
    String email;
    String phoneNumber;
    long userId;
}
