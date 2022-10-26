package pl.gov.cmp.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@ToString
@NoArgsConstructor
public class UserWkDto {

    private String personIdentifier;
    private String firstName;
    private String familyName;
    private String ackToken;
    private String sessionIndex;
    private String nameId;
    private String roleId;
    private LocalDate birthDate;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date authnInstant;

}
