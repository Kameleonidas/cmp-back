package pl.gov.cmp.dictionary.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cemetery_owner_categories_dictionary")
public class CemeteryOwnerCategoryDictionaryEntity {

    @Id
    private Long id;
    private String code;
    private String name;
    private boolean perpetualUse;
    private Long parentId;

    public static CemeteryOwnerCategoryDictionaryEntity createWithId(Long id) {
        CemeteryOwnerCategoryDictionaryEntity entity = new CemeteryOwnerCategoryDictionaryEntity();
        entity.setId(id);
        return entity;
    }
}
