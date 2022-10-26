package pl.gov.cmp.file.repository.cemetery.attachment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.model.mapper.FileMapperDto;

@AllArgsConstructor
@Repository
class CemeteryAttachmentRepositoryImpl implements CemeteryAttachmentRepository {

    private final JpaCemeteryAttachmentRepository jpaCemeteryAttachmentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveCemeteryAttachmentRepository(FileResourceDto fileResourceDto, Long applicationId) {
        jpaCemeteryAttachmentRepository.save(FileMapperDto.toCemeteryAttachmentEntity(applicationId, fileResourceDto));
    }

}
