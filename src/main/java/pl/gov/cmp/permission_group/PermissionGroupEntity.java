package pl.gov.cmp.permission_group;

import lombok.AllArgsConstructor;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import lombok.NoArgsConstructor;
import pl.gov.cmp.auth.model.entity.Permission;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission_groups")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class PermissionGroupEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "permission_groups_id_seq")
    @SequenceGenerator(name = "permission_groups_id_seq", sequenceName = "permission_groups_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String description;

    @ElementCollection
    @CollectionTable(
            name = "permission_group_institution_types",
            joinColumns = @JoinColumn(name = "permission_group_id", referencedColumnName = "id"))
    @Column(name = "institution_type")
    @Enumerated(STRING)
    @Type(type = "enum")
    private Set<ObjectCategoryEnum> institutionTypes = newHashSet();

    @ManyToMany
    @JoinTable(
            name = "permission_groups_permissions",
            joinColumns = @JoinColumn(name = "permission_group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = newHashSet();
}
