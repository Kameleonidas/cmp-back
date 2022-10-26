package pl.gov.cmp.administration.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.model.enums.MessageStatus;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invitations")
@ToString
public class InvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invitations_id_seq")
    @SequenceGenerator(name = "invitations_id_seq", sequenceName = "invitations_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 50)
    @NotBlank
    private String requestIdentifier;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InstitutionType institutionType;

    @NotNull
    private Long institutionId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "massage_id")
    private MessageEntity message;

    public InvitationEntity init() {
        this.message.setStatus(MessageStatus.NEW);
        this.message.setCreateDate(LocalDateTime.now());
        return this;
    }
}
