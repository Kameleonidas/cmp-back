package pl.gov.cmp.process.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.gov.cmp.process.model.enums.ProcessCode;
import pl.gov.cmp.process.model.enums.ProcessState;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "system_processes")
public class ProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_processes_id_seq")
    @SequenceGenerator(name = "system_processes_id_seq", sequenceName = "system_processes_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProcessCode code;

    @NotNull
    @Size(max = 200)
    private String description;

    private LocalDateTime runDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProcessState state;
}
