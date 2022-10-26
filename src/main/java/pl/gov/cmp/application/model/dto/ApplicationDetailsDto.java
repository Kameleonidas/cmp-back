package pl.gov.cmp.application.model.dto;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;
import pl.gov.cmp.auth.model.dto.UserAccountDto;

import java.time.LocalDate;

@Getter
@Setter
public class ApplicationDetailsDto {
    private Long id;
    private String appNumber;
    private ApplicationType appType;
    private ApplicationStatus appStatus;
    private String objectName;
    private LocalDate createDate;
    private LocalDate updateDate;
    private UserAccountDto operator;
}
