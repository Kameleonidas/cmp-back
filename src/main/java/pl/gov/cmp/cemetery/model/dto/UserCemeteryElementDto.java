package pl.gov.cmp.cemetery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCemeteryElementDto {
    private Long id;
    private String name;
    private String type;
    private LocalDate createDate;
    private CemeteryAddressDto locationAddress;
}
