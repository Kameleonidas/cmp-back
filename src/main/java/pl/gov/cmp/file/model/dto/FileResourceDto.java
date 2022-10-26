package pl.gov.cmp.file.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileResourceDto {

    private Long id;
    private String fileHashedName;
    private String fileName;
    private int size;
    private byte[] bytes;

}
