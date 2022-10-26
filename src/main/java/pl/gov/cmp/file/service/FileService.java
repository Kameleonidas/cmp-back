package pl.gov.cmp.file.service;

import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.gov.cmp.application.model.dto.ApplicationFileAttachmentDto;
import pl.gov.cmp.application.model.dto.CemeteryAttachmentDto;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryEntity;
import pl.gov.cmp.file.configuration.FileConfiguration;
import pl.gov.cmp.file.exception.FileAcceptException;
import pl.gov.cmp.file.exception.FileDownloadException;
import pl.gov.cmp.file.exception.FileUploadException;
import pl.gov.cmp.file.model.dto.FileResourceDto;
import pl.gov.cmp.file.repository.application.attachment.FileApplicationRepository;
import pl.gov.cmp.file.repository.cemetery.attachment.CemeteryAttachmentRepository;
import pl.gov.cmp.file.repository.cemetery.image.ImageCemeteryRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class FileService {

    private static final String TMP_DIR = "tmp";
    private static final String APPLICATION_DIR = "application";
    private static final String CEMETERY_DIR = "cemetery";
    private static final int MAX_DIR_LEVELS_TO_SEARCH = 5;

    private final FileConfiguration fileConfiguration;
    private final FileApplicationRepository fileApplicationRepository;
    private final ImageCemeteryRepository imageCemeteryRepository;
    private final CemeteryAttachmentRepository fileCemeteryAttachmentRepository;

    public FileResourceDto uploadFile(MultipartFile file) {
        Path uploadPath = Paths.get(this.fileConfiguration.getRootPath(), TMP_DIR);
        createNonExistingDirectories(uploadPath);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (file.isEmpty()) {
            throw new FileUploadException(String.format("Failed to upload empty file %s", fileName));
        }

        final FileResourceDto fileResource = FileResourceDto.builder()
                .fileHashedName(UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName))
                .fileName(fileName)
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            fileResource.setSize(inputStream.available());
            Files.copy(inputStream, uploadPath.resolve(fileResource.getFileHashedName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileUploadException(String.format("Failed to upload file %s to location %s", fileName, uploadPath), e);
        }

        return fileResource;
    }

    public void acceptApplicationAttachments(ApplicationCemeteryEntity applicationId, List<ApplicationFileAttachmentDto> applicationAttachmentFilesHashName) {
        Path applicationPath = Paths.get(this.fileConfiguration.getAttachmentsStoragePath(), APPLICATION_DIR, String.valueOf(applicationId));
        createNonExistingDirectories(applicationPath);

        List<String> fileHashNames = applicationAttachmentFilesHashName
                .stream()
                .map(ApplicationFileAttachmentDto::getFileHashedName)
                .collect(Collectors.toList());

        List<FileResourceDto> fileResourceDto = this.acceptFiles(fileHashNames, applicationPath);
        fileResourceDto.forEach(file -> {
            var fileName = applicationAttachmentFilesHashName
                    .stream()
                    .filter(attachment -> file.getFileHashedName().equals(attachment.getFileHashedName()))
                    .map(ApplicationFileAttachmentDto::getFileName)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
            file.setFileName(fileName);
        });

        fileResourceDto
                .forEach(file -> fileApplicationRepository.saveFileApplicationRepository(file, applicationId));
    }

    public void acceptCemeteryAttachments(Long cemeteryId, List<CemeteryAttachmentDto> fileHashedNames) {
        Path cemeteryPath = Paths.get(this.fileConfiguration.getAttachmentsStoragePath(), CEMETERY_DIR, String.valueOf(cemeteryId));
        createNonExistingDirectories(cemeteryPath);
        List<String> fileHashNames = fileHashedNames
                .stream()
                .map(CemeteryAttachmentDto::getFileHashedName)
                .collect(Collectors.toList());
        List<FileResourceDto> fileResourceDto = this.acceptFiles(fileHashNames, cemeteryPath);

        fileResourceDto.forEach(file -> {
            var fileName = fileHashedNames
                    .stream()
                    .filter(attachment -> file.getFileHashedName().equals(attachment.getFileHashedName()))
                    .map(CemeteryAttachmentDto::getFileName)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
            file.setFileName(fileName);
        });

        fileResourceDto
                .forEach(file -> fileCemeteryAttachmentRepository.saveCemeteryAttachmentRepository(file, cemeteryId));
    }

    public List<FileResourceDto> acceptCemeteryImages(Long cemeteryId, List<String> fileHashedNames) {
        Path cemeteryPath = Paths.get(this.fileConfiguration.getImagesStoragePath(), CEMETERY_DIR, String.valueOf(cemeteryId));
        createNonExistingDirectories(cemeteryPath);
        var fileResourceDto = acceptFiles(fileHashedNames, cemeteryPath);
        fileResourceDto
                .forEach(file -> imageCemeteryRepository.saveCemeteryImage(file, cemeteryId));
        return fileResourceDto;
    }

    public FileResourceDto downloadApplicationAttachment(Long applicationId, String fileHashedName) {
        Path applicationPath = Paths.get(this.fileConfiguration.getAttachmentsStoragePath(), APPLICATION_DIR, String.valueOf(applicationId));
        return this.downloadFile(applicationPath, fileHashedName);
    }

    public FileResourceDto downloadCemeteryAttachment(Long cemeteryId, String fileHashedName) {
        Path cemeteryPath = Paths.get(this.fileConfiguration.getAttachmentsStoragePath(), CEMETERY_DIR, String.valueOf(cemeteryId));
        return this.downloadFile(cemeteryPath, fileHashedName);
    }

    public FileResourceDto downloadCemeteryImage(Long cemeteryId, String fileHashedName) {
        Path cemeteryPath = Paths.get(this.fileConfiguration.getImagesStoragePath(), CEMETERY_DIR, String.valueOf(cemeteryId));
        return this.downloadFile(cemeteryPath, fileHashedName);
    }

    private List<FileResourceDto> acceptFiles(List<String> fileHashedNames, Path targetPath) {
        Path sourcePath = Paths.get(this.fileConfiguration.getRootPath(), TMP_DIR);
        try (Stream<Path> stream = Files.walk(sourcePath)) {
            return stream.filter(sourceFile -> fileHashedNames.contains(sourceFile.getFileName().toString()))
                    .map(sourceFile -> move(sourceFile, targetPath.resolve(sourcePath.relativize(sourceFile))))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileAcceptException(String.format("Failed to accept files %s", fileHashedNames), e);
        }
    }

    private void createNonExistingDirectories(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new FileAcceptException(String.format("Failed to create directories in path %s", path), e);
        }
    }

    private FileResourceDto move(Path sourcePath, Path targetPath) {
        try {
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return downloadFile(targetPath);
        } catch (IOException e) {
            throw new FileAcceptException(String.format("Failed to move from %s to %s.", sourcePath, targetPath));
        }
    }

    private FileResourceDto downloadFile(Path dirPath, String fileHashedName) {
        String errorMessage = String.format("File %s not found in path %s", fileHashedName, dirPath);
        try (Stream<Path> stream = Files.find(dirPath, MAX_DIR_LEVELS_TO_SEARCH, (filePath, fileAttr) -> fileHashedName.equals(filePath.getFileName().toString()))) {
            Path filePath = stream.findFirst().orElseThrow(() -> new FileDownloadException(errorMessage));
            return downloadFile(filePath);
        } catch (IOException e) {
            throw new FileDownloadException(errorMessage, e);
        }
    }

    private FileResourceDto downloadFile(Path filePath) {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            byte[] bytes = ByteStreams.toByteArray(inputStream);
            return FileResourceDto.builder()
                    .fileHashedName(filePath.getFileName().toString())
                    .size(bytes.length)
                    .bytes(bytes)
                    .build();
        } catch (Exception e) {
            throw new FileDownloadException(String.format("Could not read file %s", filePath), e);
        }
    }

}
