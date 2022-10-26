package pl.gov.cmp.administration.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "email_senders")
@ToString
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class EmailSenderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_senders_id_seq")
    @SequenceGenerator(name = "email_senders_id_seq", sequenceName = "email_senders_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 100)
    @NotBlank
    private String emailFrom;

    @Size(max = 100)
    @NotBlank
    private String emailTo;
}
