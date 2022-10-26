package pl.gov.cmp.file.repository.application.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.file.model.entity.ApplicationAttachmentEntity;

interface JpaFileApplicationRepository extends JpaRepository<ApplicationAttachmentEntity,Long> {
}
