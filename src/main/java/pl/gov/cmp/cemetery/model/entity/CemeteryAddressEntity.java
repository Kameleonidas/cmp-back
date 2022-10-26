package pl.gov.cmp.cemetery.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Entity
@Table(name = "cemetery_addresses")
public class CemeteryAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemetery_addresses_id_seq")
    @SequenceGenerator(name = "cemetery_addresses_id_seq", sequenceName = "cemetery_addresses_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 2)
    private String voivodeshipTercCode;

    @Size(min = 4, max = 4)
    private String districtTercCode;

    @Size(min = 7, max = 7)
    private String communeTercCode;

    @Size(min = 7, max = 7)
    private String placeSimcCode;

    @Size(max = 100)
    private String voivodeship;

    @Size(max = 100)
    private String district;

    @Size(max = 100)
    private String commune;

    @Size(max = 100)
    private String place;

    @Size(max = 100)
    private String street;

    @Size(max = 50)
    private String number;

    @Size(max = 50)
    private String zipCode;

    @Size(max = 100)
    private String postName;

}
