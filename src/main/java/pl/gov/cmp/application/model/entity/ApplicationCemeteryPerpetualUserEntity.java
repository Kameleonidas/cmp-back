package pl.gov.cmp.application.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.application.model.enums.PerpetualUseType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "application_cemetery_perpetual_users")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class ApplicationCemeteryPerpetualUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_cemetery_perpetual_users_id_seq")
    @SequenceGenerator(name = "application_cemetery_perpetual_users_id_seq", sequenceName = "application_cemetery_perpetual_users_id_seq", allocationSize = 1)
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
    private ApplicationCemeteryRepresentativeEntity representative;

    @OneToOne
    @JoinColumn(name = "application_id")
    private ApplicationCemeteryEntity application;

}
