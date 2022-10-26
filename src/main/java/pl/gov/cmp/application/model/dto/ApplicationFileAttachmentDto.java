package pl.gov.cmp.application.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFileAttachmentDto {
    private String fileName;
    private String fileHashedName;
}
