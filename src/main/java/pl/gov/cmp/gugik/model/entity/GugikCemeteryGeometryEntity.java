package pl.gov.cmp.gugik.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import pl.gov.cmp.gugik.model.enums.CemeteryItemType;
import pl.gov.cmp.gugik.model.enums.GeometryType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "gugik_cemetery_geometries")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class GugikCemeteryGeometryEntity {

    private static final String ID_IIP_DELIMITER = "_";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gugik_cemetery_geometries_id_seq")
    @SequenceGenerator(name = "gugik_cemetery_geometries_id_seq", sequenceName = "gugik_cemetery_geometries_id_seq", allocationSize = 1)
    private Long id;

    private String idIip;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private CemeteryItemType type;

    private String code;

    private LocalDate createDate;

    private LocalDate updateDate;

    @Size(max = 400)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private GeometryType geometryType;

    private Geometry geometry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cemetery_id")
    private GugikCemeteryEntity cemetery;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private GugikAddressEntity address;

    private Point addressPoint;

    public String getIdIipIdentifier() {
        String[] idParts = idIip.split(ID_IIP_DELIMITER);
        return idParts[0] + ID_IIP_DELIMITER + idParts[1];
    }
}
