package pl.gov.cmp.application.model.dto;

import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;
import pl.gov.cmp.auth.model.dto.UserAccountDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationDto {

    private Long id;
    private String appNumber;
    private ApplicationType appType;
    private ApplicationStatus appStatus;
    private String objectName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private UserAccountDto operator;

    private ApplicationCemeteryDto application;
}
