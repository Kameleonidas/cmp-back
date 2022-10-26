package pl.gov.cmp.dictionary.controller.protocol.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CemeteryOwnerCategoryResponse {

    private Long id;
    private String code;
    private String name;
    private boolean perpetualUse;
    private Long parentId;

}
