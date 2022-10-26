package pl.gov.cmp.file.model.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Entity
@Table(name = "application_attachment")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_id_generator")
    @SequenceGenerator(name = "attachment_id_generator", sequenceName = "application_attachment_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "application_id")
    private Long application;

    @NotBlank
    @Size(max = 255)
    private String fileName;

    @NotBlank
    @Size(max = 255)
    private String fileHashedName;

    @NotNull
    private int size;

}
