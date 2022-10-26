package pl.gov.cmp.application.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.application.model.enums.LegalForm;

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
@Table(name = "application_cemetery_managers")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class ApplicationCemeteryManagerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_cemetery_managers_id_seq")
    @SequenceGenerator(name = "application_cemetery_managers_id_seq", sequenceName = "application_cemetery_managers_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private LegalForm type;

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

    private Boolean managerDataTheSameAsPerpetualUserOrObjectOwner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "representative_id")
    private ApplicationCemeteryRepresentativeEntity representative;

    @OneToOne
    @JoinColumn(name = "application_id")
    private ApplicationCemeteryEntity application;

}
