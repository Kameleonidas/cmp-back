package pl.gov.cmp.file.repository.application.attachment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.model.mapper.FileMapperDto;

@AllArgsConstructor
@Repository
class FileApplicationRepositoryImpl implements FileApplicationRepository {

    private final JpaFileApplicationRepository fileApplicationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveFileApplicationRepository(FileResourceDto fileResourceDto, ApplicationCemeteryEntity applicationId) {
        fileApplicationRepository.save(FileMapperDto.toAttachmentEntity(applicationId, fileResourceDto));
    }

}
