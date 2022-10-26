package pl.gov.cmp.cemetery.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.application.model.enums.RepresentativeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@Entity
@Table(name = "cemetery_representatives")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
@AllArgsConstructor
@NoArgsConstructor
public class CemeteryRepresentativeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemetery_representatives_id_seq")
    @SequenceGenerator(name = "cemetery_representatives_id_seq", sequenceName = "cemetery_representatives_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private RepresentativeType type;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String email;

    private String nameRepresentationPersonObjectManagerLegalPerson;

    private String surnameRepresentationPersonObjectManagerLegalPerson;

    private String emailRepresentationPersonObjectManagerLegalPerson;

}
