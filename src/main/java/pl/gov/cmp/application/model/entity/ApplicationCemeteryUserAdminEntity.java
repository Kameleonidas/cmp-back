package pl.gov.cmp.application.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "application_cemetery_user_admins")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class ApplicationCemeteryUserAdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_cemetery_user_admins_id_seq")
    @SequenceGenerator(name = "application_cemetery_user_admins_id_seq", sequenceName = "application_cemetery_user_admins_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String email;

    private String adminDataTheSameAsObjManagerOrPerpUserOrObjOwner;

    @OneToOne
    @JoinColumn(name = "application_id")
    private ApplicationCemeteryEntity application;

}
