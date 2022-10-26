package pl.gov.cmp.file.model.mapper;

import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.model.entity.ApplicationAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationCemeteryAttachmentEntity;
import pl.gov.cmp.file.model.entity.ApplicationImageEntity;

public interface FileMapperDto {

    static ApplicationAttachmentEntity toAttachmentEntity(ApplicationCemeteryEntity applicationId, FileResourceDto fileProtocolMapper) {
        return ApplicationAttachmentEntity
                .builder()
                .application(applicationId.getId())
                .fileName(fileProtocolMapper.getFileName())
                .fileHashedName(fileProtocolMapper.getFileHashedName())
                .size(fileProtocolMapper.getSize())
                .build();
    }

    static ApplicationImageEntity toImageEntity(Long cemeteryId, FileResourceDto fileProtocolMapper) {
        return ApplicationImageEntity
                .builder()
                .fileHashedName(fileProtocolMapper.getFileHashedName())
                .application(cemeteryId)
                .size(fileProtocolMapper.getSize())
                .build();
    }

    static ApplicationCemeteryAttachmentEntity toCemeteryAttachmentEntity(Long applicationId, FileResourceDto fileProtocolMapper) {
        return ApplicationCemeteryAttachmentEntity
                .builder()
                .application(applicationId)
                .fileName(fileProtocolMapper.getFileName())
                .fileHashedName(fileProtocolMapper.getFileHashedName())
                .size(fileProtocolMapper.getSize())
                .build();
    }

}
