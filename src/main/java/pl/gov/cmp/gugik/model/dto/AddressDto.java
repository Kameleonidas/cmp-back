package pl.gov.cmp.gugik.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private String voivodeshipTercCode;

    private String voivodeship;

    private String districtTercCode;

    private String district;

    private String communeTercCode;

    private String commune;

    private String place;

    private String placeSimcCode;
}
