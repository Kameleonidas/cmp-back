package pl.gov.cmp.auth.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_account_activity_periods")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class UserAccountActivityPeriodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_account_activity_periods_id_seq")
    @SequenceGenerator(name = "user_account_activity_periods_id_seq", sequenceName = "user_account_activity_periods_id_seq", allocationSize = 1)
    private Long id;

    private LocalDate employedFrom;
    private LocalDate employedTo;
    private String roleInInstitution;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private UserAccountToSubjectEntity userAccountToSubject;
}
