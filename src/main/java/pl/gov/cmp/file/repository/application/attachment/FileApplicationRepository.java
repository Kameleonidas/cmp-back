package pl.gov.cmp.file.repository.application.attachment;

import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.file.model.dto.FileResourceDto;

public interface FileApplicationRepository {

    void saveFileApplicationRepository(FileResourceDto fileResourceDto, ApplicationCemeteryEntity applicationId);
}
