package pl.gov.cmp.dictionary.model.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryCemeteryOwnerCategoryDto {
    private Long id;
    private String code;
    private String name;
    private boolean perpetualUse;
    private Long parentId;
}
