package pl.gov.cmp.gugik.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "gugik_cemeteries")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class GugikCemeteryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gugik_cemeteries_id_seq")
    @SequenceGenerator(name = "gugik_cemeteries_id_seq", sequenceName = "gugik_cemeteries_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 200)
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate importDate;

    @OneToMany
    private List<GugikCemeteryGeometryEntity> cemeteryGeometries;
}
