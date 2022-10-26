package pl.gov.cmp.auth.model.entity;

import com.google.common.collect.Sets;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.auth.model.enums.ObjectCategoryEnum;
import pl.gov.cmp.exception.InconsistentDataException;
import pl.gov.cmp.permission_group.PermissionGroupEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Sets.*;
import static java.lang.String.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_account_to_subjects")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class UserAccountToSubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_account_to_subjects_id_seq")
    @SequenceGenerator(name = "user_account_to_subjects_id_seq", sequenceName = "user_account_to_subjects_id_seq", allocationSize = 1)
    private Long id;

    private Long cemeteryId;

    private Long ipnId;

    private Long voivodshipId;

    private Long crematoriumId;

    private boolean activeEmployee;

    @Email
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private ObjectCategoryEnum category;

    @OneToMany(mappedBy = "userAccountToSubject")
    private Collection<UserAccountActivityPeriodEntity> userAccountActivityPeriod;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account_id")
    private UserAccountEntity userAccount;

    @ManyToMany
    @JoinTable(
            name = "user_account_to_subject_permission_group",
            joinColumns = @JoinColumn(name = "user_account_to_subject_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_group_id"))
    private Set<PermissionGroupEntity> permissionGroups = newHashSet();

    public void setInstitutionId(long institutionId, ObjectCategoryEnum institutionType) {
        switch (institutionType) {
            case CEMETERY:
                setCemeteryId(institutionId);
                break;
            case CREMATORIUM:
                setCrematoriumId(institutionId);
                break;
            case IPN:
                setIpnId(institutionId);
                break;
            case VOIVODSHIP_OFFICE:
                setVoivodshipId(institutionId);
                break;
            default:
                throw new InconsistentDataException(format("Setting institutionId for institution of type %s is not supported", institutionType));
        }
    }
}
