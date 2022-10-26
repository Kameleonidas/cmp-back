package pl.gov.cmp.cemetery.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CemeteryUserAdminDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String adminDataTheSameAsObjManagerOrPerpUserOrObjOwner;
}
