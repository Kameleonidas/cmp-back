package pl.gov.cmp.file.repository.cemetery.image;

import pl.gov.cmp.file.model.dto.FileResourceDto;

public interface ImageCemeteryRepository {

    void saveCemeteryImage(FileResourceDto fileResourceDto, Long applicationId);
}
