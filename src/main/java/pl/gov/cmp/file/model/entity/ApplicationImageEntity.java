package pl.gov.cmp.file.model.entity;


import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Entity
@Table(name = "application_cemetery_image")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_generator")
    @SequenceGenerator(name = "image_id_generator", sequenceName = "application_cemetery_image_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "application_id")
    private Long application;

    @NotBlank
    @Size(max = 255)
    private String fileHashedName;

    @NotNull
    private int size;
}
