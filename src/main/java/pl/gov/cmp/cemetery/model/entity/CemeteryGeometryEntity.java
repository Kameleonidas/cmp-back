package pl.gov.cmp.cemetery.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.locationtech.jts.geom.Geometry;
import pl.gov.cmp.gugik.model.enums.GeometryType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cemetery_geometries")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class CemeteryGeometryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemetery_geometries_id_seq")
    @SequenceGenerator(name = "cemetery_geometries_id_seq", sequenceName = "cemetery_geometries_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 200)
    private String name;

    private String code;

    private LocalDate createDate;

    private LocalDate updateDate;

    @Size(max = 200)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private GeometryType geometryType;

    @NotNull
    private Geometry geometry;

    @ManyToOne
    @JoinColumn(name = "cemetery_id")
    private CemeteryEntity cemetery;

    @NotNull
    @Size(max = 200)
    private String idIipIdentifier;

    @NotNull
    @Size(max = 30)
    private String idIipVersion;
}
