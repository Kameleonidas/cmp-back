package pl.gov.cmp.file.repository.cemetery.attachment;

import pl.gov.cmp.file.model.dto.FileResourceDto;

public interface CemeteryAttachmentRepository {

    void saveCemeteryAttachmentRepository(FileResourceDto fileResourceDto, Long applicationId);
}
