package pl.gov.cmp.application.model.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "application_name_local_government_unit")
public class ApplicationNameLocalGovernmentUnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_name_local_government_unit_id_seq")
    @SequenceGenerator(name = "application_name_local_government_unit_id_seq", sequenceName = "application_name_local_government_unit_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String source;
    private String codeSimc;
    private String levelName;
}
