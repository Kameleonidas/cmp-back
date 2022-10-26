package pl.gov.cmp.application.controller.protocol.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import pl.gov.cmp.application.model.enums.RepresentativeType;

@Getter
@Setter
public class ApplicationCemeteryRepresentativeRequest {

    @Schema(description="The representative type")
    private RepresentativeType type;
    @Schema(description="The representative first name")
    private String firstName;
    @Schema(description="The representative last name")
    private String lastName;
    @Schema(description="The representative email")
    private String email;
    @Schema(description="Name of the person representing the facility manager")
    private String nameRepresentationPersonObjectManagerLegalPerson;
    @Schema(description="Surname of the person representing the facility manager")
    private String surnameRepresentationPersonObjectManagerLegalPerson;
    @Schema(description="Email of the person representing the facility manager")
    private String emailRepresentationPersonObjectManagerLegalPerson;
}
