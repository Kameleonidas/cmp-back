package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CemeteryUserAdminResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String adminDataTheSameAsObjManagerOrPerpUserOrObjOwner;
}
