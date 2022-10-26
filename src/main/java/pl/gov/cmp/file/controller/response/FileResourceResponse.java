package pl.gov.cmp.file.controller.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileResourceResponse {

    private String fileHashedName;
    private String fileName;
    private int size;
    private byte[] bytes;
}
