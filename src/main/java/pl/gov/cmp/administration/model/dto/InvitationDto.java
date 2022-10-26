package pl.gov.cmp.administration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.gov.cmp.administration.model.enums.InstitutionType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class InvitationDto {

    private String name;

    private String email;

    private InstitutionType institutionType;

    private Long institutionId;

    private String requestIdentifier;
}
