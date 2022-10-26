package pl.gov.cmp.application.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import pl.gov.cmp.application.model.enums.TerritorialUnitType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "application_cemetery_owners")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class ApplicationCemeteryOwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_cemetery_owners_id_seq")
    @SequenceGenerator(name = "application_cemetery_owners_id_seq", sequenceName = "application_cemetery_owners_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 50)
    private String nip;

    @Size(max = 50)
    private String regon;

    @Size(max = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private TerritorialUnitType territorialUnitType;

    private Long ownerCategoryId;

    private Long ownerSubcategoryId;

    private Long religionId;

    private Boolean applicationIsOwner;

    private String ownerCompanyName;

    private String unitWithoutLegalPersonalityName;

    private Long churchNotRegulatedByLawId;

    private Long churchRegulatedByLawId;

    private String nameOfParish;

    private Long perpetualOwnerTypeId;

    private String userInsteadOwnerOrPerpetualUser;

    private String  nameAssociationLocalGovernment;

    private String workEmailInstitutionObjectOwner;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="community_name_id", referencedColumnName = "id")
    private ApplicationCommunityNameEntity communityName;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="owner_community_name_id", referencedColumnName = "id")
    private ApplicationOwnerCommunityNameEntity ownerCommunityName;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="name_local_government_unit_id", referencedColumnName = "id")
    private ApplicationNameLocalGovernmentUnitEntity nameLocalGovernmentUnit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private ApplicationAddressEntity address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "representative_id")
    private ApplicationCemeteryRepresentativeEntity representative;

    @OneToOne
    @JoinColumn(name = "application_id")
    private ApplicationCemeteryEntity application;

}
