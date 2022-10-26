package pl.gov.cmp.cemetery.model.dto;


import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryAttachmentDto {
    private Long id;
    private Long cemetery;
    private String fileName;
    private String fileHashedName;
    private int size;
}
