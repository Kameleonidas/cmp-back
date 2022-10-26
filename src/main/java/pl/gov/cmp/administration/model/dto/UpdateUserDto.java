package pl.gov.cmp.administration.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserDto {
    private Long subjectId;
    private String email;
    private String phoneNumber;
}
