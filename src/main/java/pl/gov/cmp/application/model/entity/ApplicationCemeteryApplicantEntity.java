package pl.gov.cmp.application.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "application_cemetery_applicants")
public class ApplicationCemeteryApplicantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_cemetery_applicants_id_seq")
    @SequenceGenerator(name = "application_cemetery_applicants_id_seq", sequenceName = "application_cemetery_applicants_id_seq", allocationSize = 1)
    private Long id;

    private Long userId;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id")
    private ApplicationCemeteryEntity application;

}
