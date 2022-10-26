package pl.gov.cmp.administration.controller.protocol.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserResponse {
    @Schema(description="The user id")
    private Long id;
    @Schema(description="The user first name")
    private String firstName;
    @Schema(description="The user last name")
    private String lastName;
    @Schema(description="The user birth date")
    private LocalDate birthDate;
    @Schema(description="The user status")
    private String status;
    @Schema(description="The user categories")
    private Set<String> categories;
}
