package pl.gov.cmp.administration.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "message_parameters")
@ToString(exclude = "message")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class MessageParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_parameters_id_seq")
    @SequenceGenerator(name = "message_parameters_id_seq", sequenceName = "message_parameters_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 100)
    @NotBlank
    private String key;

    @Size(max = 200)
    private String value;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "massage_id")
    private MessageEntity message;
}
