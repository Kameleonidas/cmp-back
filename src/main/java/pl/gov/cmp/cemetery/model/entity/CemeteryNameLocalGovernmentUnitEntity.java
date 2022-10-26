package pl.gov.cmp.cemetery.model.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cemetery_name_local_government_unit")
public class CemeteryNameLocalGovernmentUnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemetery_name_local_government_unit_id_seq")
    @SequenceGenerator(name = "cemetery_name_local_government_unit_id_seq", sequenceName = "cemetery_name_local_government_unit_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
