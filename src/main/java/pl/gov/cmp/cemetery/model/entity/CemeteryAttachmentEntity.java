package pl.gov.cmp.cemetery.model.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Entity
@Table(name = "cemetery_attachment")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_id_generator")
    @SequenceGenerator(name = "attachment_id_generator", sequenceName = "application_attachment_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cemetery_id")
    private CemeteryEntity cemetery;

    @NotBlank
    @Size(max = 255)
    private String fileName;

    @NotBlank
    @Size(max = 255)
    private String fileHashedName;

    @NotNull
    private int size;

}
