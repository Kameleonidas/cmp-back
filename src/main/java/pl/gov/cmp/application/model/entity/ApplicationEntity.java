package pl.gov.cmp.application.model.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import pl.gov.cmp.application.model.enums.ApplicationStatus;
import pl.gov.cmp.application.model.enums.ApplicationType;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "applications")
@TypeDef(name = "enum", typeClass = PostgreSQLEnumType.class)
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applications_id_seq")
    @SequenceGenerator(name = "applications_id_seq", sequenceName = "applications_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 100)
    private String appNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private ApplicationType appType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "enum")
    private ApplicationStatus appStatus;

    @Size(max = 100)
    private String objectName;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @OneToOne
    @JoinColumn(name="user_account_id", referencedColumnName = "id")
    private UserAccountEntity operator;

    private String rejectionReasonDescription;

    @OneToOne
    @JoinColumn(name="application_cemetery_id", referencedColumnName = "id")
    private ApplicationCemeteryEntity application;

     private static final String APPLICATION_NUMBER_PREFIX = "WN";
     private static final String NUMBER_FORMAT = "000000";

    public static ApplicationEntity createNewApplication(String objectName, Long applicationNumber) {
        var currentDate = LocalDateTime.now();
        ApplicationEntity application = new ApplicationEntity();
        application.setAppNumber(APPLICATION_NUMBER_PREFIX + "-" + currentDate.getYear() + "-" + new DecimalFormat(NUMBER_FORMAT).format(applicationNumber));
        application.setAppStatus(ApplicationStatus.SENT);
        application.setAppType(ApplicationType.CEMETERY_REGISTRATION);
        application.setObjectName(objectName);
        application.setUpdateDate(currentDate);
        application.setCreateDate(currentDate);
        return application;
    }
}
