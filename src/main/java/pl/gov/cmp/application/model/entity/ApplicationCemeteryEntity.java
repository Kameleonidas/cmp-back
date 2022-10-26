package pl.gov.cmp.application.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;
import pl.gov.cmp.dictionary.model.entity.CemeteryFacilityTypeDictionaryEntity;
import pl.gov.cmp.file.model.entity.ApplicationAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationCemeteryAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationImageEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "application_cemeteries")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class ApplicationCemeteryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_cemeteries_id_seq")
    @SequenceGenerator(name = "application_cemeteries_id_seq", sequenceName = "application_cemeteries_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private CemeteryStatus cemeteryStatus;

    @Size(max = 100)
    private String objectName;

    @Size(max = 4000)
    private String objectDescription;

    @Size(max = 100)
    private String contactEmail;

    @Size(max = 50)
    private String contactPhoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private TermType openTermType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private TermType closeTermType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String fieldsToBeCompleted;

    private String openDate;

    private String closeDate;

    @Size(max = 50)
    private String openTerm;

    @Size(max = 50)
    private String closeTerm;

    @Size(max = 200)
    private String otherType;

    @Size(max = 200)
    private String otherReligion;

    private boolean substitutePerformance;

    private boolean perpetualUse;

    private boolean churchPerpetualUser;

    private boolean churchOwner;

    private boolean churchRegulatedByLaw;

    private boolean managerExists;

    private boolean userAdminExists;

    private Long cemeteryTypeId;

    @OneToOne
    @JoinColumn(name="facility_type_id", referencedColumnName = "id")
    private CemeteryFacilityTypeDictionaryEntity facilityType;

    private Long religionId;

    private Long boundCemeteryId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY    )
    @JoinColumn(name = "location_address_id")
    private ApplicationAddressEntity locationAddress;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_address_id")
    private ApplicationAddressEntity contactAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationCemeteryApplicantEntity applicant;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationCemeteryPerpetualUserEntity perpetualUser;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationCemeteryOwnerEntity owner;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationCemeteryManagerEntity manager;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationCemeteryUserAdminEntity userAdmin;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationAttachmentEntity> applicationAttachmentFiles;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationCemeteryAttachmentEntity> cemeteryAttachmentFiles;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApplicationImageEntity> image;

}
