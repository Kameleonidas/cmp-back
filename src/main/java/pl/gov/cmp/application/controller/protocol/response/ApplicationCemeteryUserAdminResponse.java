package pl.gov.cmp.application.controller.protocol.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationCemeteryUserAdminResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String adminDataTheSameAsObjManagerOrPerpUserOrObjOwner;
}
