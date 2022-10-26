package pl.gov.cmp.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDto {

    private String fileHashedName;
    private String fileName;
    private int size;

}
