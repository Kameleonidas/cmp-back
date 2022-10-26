package pl.gov.cmp.cemetery.controller.protocol.response;


import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CemeteryAttachmentResponse {
    private Long id;
    private Long cemetery;
    private String fileName;
    private String fileHashedName;
    private int size;
}
