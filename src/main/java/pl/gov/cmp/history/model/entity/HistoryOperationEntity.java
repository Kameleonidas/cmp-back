package pl.gov.cmp.history.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.gov.cmp.history.model.enums.HistoryOperationType;

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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "history_operations")
public class HistoryOperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_operations_id_seq")
    @SequenceGenerator(name = "history_operations_id_seq", sequenceName = "history_operations_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private HistoryOperationType type;

    @Size(max = 300)
    private String description;

    @CreationTimestamp
    private LocalDate operationDate;

    private Long applicationId;

    private Long cemeteryId;

    private Long graveId;

    private Long userAccountId;

    private Long applicationCemeteryStaffId;

    private Long cemeteryStaffId;

}
