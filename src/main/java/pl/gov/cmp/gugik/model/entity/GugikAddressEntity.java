package pl.gov.cmp.gugik.model.entity;

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
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "gugik_addresses")
public class GugikAddressEntity {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gugik_addresses_id_seq")
    @SequenceGenerator(name = "gugik_addresses_id_seq", sequenceName = "gugik_addresses_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 2)
    private String voivodeshipTercCode;

    @Size(max = 100)
    private String voivodeship;

    @Size(max = 4)
    private String districtTercCode;

    @Size(max = 100)
    private String district;

    @Size(max = 7)
    private String communeTercCode;

    @Size(max = 100)
    private String commune;

    @Size(min = 7, max = 7)
    private String placeSimcCode;

    @Size(max = 100)
    private String place;
}
