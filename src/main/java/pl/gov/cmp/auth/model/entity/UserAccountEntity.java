package pl.gov.cmp.auth.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.auth.model.enums.UserAccountStatusEnum;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_accounts")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_accounts_id_seq")
    @SequenceGenerator(name = "user_accounts_id_seq", sequenceName = "user_accounts_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String wkId;

    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    private LocalDate birthDate;

    @Email
    private String email;

    private String phoneNumber;

    private String localId;

    private Long roleId;

    private String localPassword;

    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private UserAccountStatusEnum status;

    @OneToMany(mappedBy = "userAccount")
    private Collection<UserAccountToSubjectEntity> subjects;

    private Instant firstLoginAt;

    private Instant lastLoginAt;
}
