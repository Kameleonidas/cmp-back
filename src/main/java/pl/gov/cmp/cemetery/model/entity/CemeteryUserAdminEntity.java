package pl.gov.cmp.cemetery.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cemetery_user_admins")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class CemeteryUserAdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemetery_user_admins_id_seq")
    @SequenceGenerator(name = "cemetery_user_admins_id_seq", sequenceName = "cemetery_user_admins_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String email;

    private String adminDataTheSameAsObjManagerOrPerpUserOrObjOwner;

}
