package pl.gov.cmp.cemetery.controller.protocol.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CemeteryOwnerSimpleResponse {

    private String name;
    private String nip;
    private String regon;
    private String representative;
}