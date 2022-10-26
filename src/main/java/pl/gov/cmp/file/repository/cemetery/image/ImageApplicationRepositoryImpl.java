package pl.gov.cmp.file.repository.cemetery.image;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.model.mapper.FileMapperDto;

@AllArgsConstructor
@Repository
class ImageApplicationRepositoryImpl implements ImageCemeteryRepository {

    private final JpaImageApplicationRepository imageApplicationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveCemeteryImage(FileResourceDto fileResourceDto, Long applicationId) {
        imageApplicationRepository.save(FileMapperDto.toImageEntity(applicationId, fileResourceDto));
    }

}
