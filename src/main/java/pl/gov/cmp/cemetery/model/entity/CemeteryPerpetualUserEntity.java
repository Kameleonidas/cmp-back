package pl.gov.cmp.cemetery.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.application.model.enums.PerpetualUseType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@Entity
@Table(name = "cemetery_perpetual_users")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryPerpetualUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemetery_perpetual_users_id_seq")
    @SequenceGenerator(name = "cemetery_perpetual_users_id_seq", sequenceName = "cemetery_perpetual_users_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private PerpetualUseType type;

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 50)
    private String nip;

    @Size(max = 50)
    private String regon;

    @Size(max = 100)
    private String email;

    private Long religionId;

    private Long perpetualChurchRegulatedByLawId;

    private Long perpetualChurchNotRegulatedByLawId;

    private String perpetualChurchesRegulatedByLawOrNo;

    private String nameOfParishPerpetualUse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "representative_id")
    private CemeteryRepresentativeEntity representative;

}
