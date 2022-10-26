package pl.gov.cmp.file.repository.cemetery.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.file.model.entity.ApplicationCemeteryAttachmentEntity;

interface JpaCemeteryAttachmentRepository extends JpaRepository<ApplicationCemeteryAttachmentEntity,Long> {
}
