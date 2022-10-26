package pl.gov.cmp.cemetery.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import pl.gov.cmp.application.model.enums.TermType;
import pl.gov.cmp.cemetery.model.enums.CemeteryStatus;
import pl.gov.cmp.dictionary.model.entity.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cemeteries")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class CemeteryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemeteries_id_seq")
    @SequenceGenerator(name = "cemeteries_id_seq", sequenceName = "cemeteries_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 200)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private CemeteryStatus status;

    @Size(max = 100)
    private String objectName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private TermType openTermType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private TermType closeTermType;

    private String openDate;

    private String closeDate;

    @Size(max = 200)
    private String otherType;

    @Size(max = 200)
    private String otherReligion;

    private Boolean substitutePerformance;

    private Boolean perpetualUse;

    private Boolean churchPerpetualUser;

    private Boolean churchOwner;

    private Boolean churchRegulatedByLaw;

    private Boolean managerExists;

    private Boolean userAdminExists;

    private Long cemeteryTypeId;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Size(max = 100)
    private String registrationNumber;

    private LocalDate liquidationDate;

    private LocalDate plannedLiquidationDate;

    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String phoneNumber;

    @Size(max = 4000)
    private String description;

    private boolean published;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private CemeterySourceDictionaryEntity source;

    @ManyToOne
    @JoinColumn(name = "facility_type_id")
    private CemeteryFacilityTypeDictionaryEntity facilityType;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private CemeteryTypeDictionaryEntity type;

    @ManyToOne
    @JoinColumn(name = "religion_id")
    private ChurchReligionDictionaryEntity religion;

    @ManyToOne
    @JoinColumn(name = "owner_category_id")
    private CemeteryOwnerCategoryDictionaryEntity ownerCategory;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_address_id")
    private CemeteryAddressEntity locationAddress;

    @OneToOne(mappedBy = "cemetery", cascade = CascadeType.ALL)
    private CemeteryGeometryEntity cemeteryGeometry;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_address_id")
    private CemeteryAddressEntity contactAddress;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="cemetery_perpetual_users_id", referencedColumnName = "id")
    private CemeteryPerpetualUserEntity cemeteryPerpetualUser;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="cemetery_owners_id", referencedColumnName = "id")
    private CemeteryOwnerEntity owner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="cemetery_managers_id", referencedColumnName = "id")
    private CemeteryManagerEntity manager;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="cemetery_user_admins_id", referencedColumnName = "id")
    private CemeteryUserAdminEntity userAdmin;

    @OneToMany(mappedBy = "cemetery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CemeteryAttachmentEntity> attachmentFiles;

}
