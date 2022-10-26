package pl.gov.cmp.application.model.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryDraftRequest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "application_cemetery_drafts")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ApplicationCemeteryDraftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_cemetery_drafts_id_seq")
    @SequenceGenerator(name = "application_cemetery_drafts_id_seq", sequenceName = "application_cemetery_drafts_id_seq", allocationSize = 1)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String draft;

    @Column(columnDefinition = "draft_name")
    private String draftName;

    private Long userAccountId;

    public static ApplicationCemeteryDraftEntity createWithDraftAndUserId(ApplicationCemeteryDraftRequest request, Long userId) {
        ApplicationCemeteryDraftEntity applicationDraft = new ApplicationCemeteryDraftEntity();
        applicationDraft.setDraft(request.getRequest());
        applicationDraft.setUserAccountId(userId);
        applicationDraft.setDraftName(request.getDraftName());
        return applicationDraft;
    }

}
