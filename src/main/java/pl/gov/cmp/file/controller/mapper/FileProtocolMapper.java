package pl.gov.cmp.file.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.gov.cmp.file.controller.response.FileResourceResponse;
import pl.gov.cmp.file.model.dto.FileResourceDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FileProtocolMapper {

    FileResourceResponse toFileResourceResponse(FileResourceDto fileResourceDto);

    default List<FileResourceResponse> toFileResourceResponse(List<FileResourceDto> fileResourceDto) {
        if(fileResourceDto == null) {
            return List.of();
        }

        return fileResourceDto
                .stream()
                .map(this::toFileResourceResponse)
                .collect(Collectors.toList());
    }

}
