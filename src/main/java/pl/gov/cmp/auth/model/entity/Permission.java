package pl.gov.cmp.auth.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "permissions")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissions_id_seq")
    @SequenceGenerator(name = "permissions_id_seq", sequenceName = "permissions_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String description;
}
