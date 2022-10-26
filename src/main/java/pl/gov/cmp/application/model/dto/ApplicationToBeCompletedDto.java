package pl.gov.cmp.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApplicationToBeCompletedDto {
    private Long applicationId;
    private String fieldsToBeCompleted;
}
