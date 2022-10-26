package pl.gov.cmp.administration.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.administration.model.enums.MessageStatus;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "messages")
@ToString
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_id_seq")
    @SequenceGenerator(name = "messages_id_seq", sequenceName = "messages_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 50)
    @NotBlank
    private String templateName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private MessageStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email_sender_id")
    private EmailSenderEntity emailSender;

    @OneToOne
    @JoinColumn(name = "user_account_to_subject_id")
    private UserAccountToSubjectEntity accountSender;

    private LocalDateTime createDate;

    private LocalDateTime sendDate;

    private LocalDateTime confirmDate;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<MessageParameterEntity> parameters;
}
