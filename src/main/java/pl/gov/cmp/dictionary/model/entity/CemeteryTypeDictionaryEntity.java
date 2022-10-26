package pl.gov.cmp.dictionary.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cemetery_types_dictionary")
public class CemeteryTypeDictionaryEntity {

    @Id
    private Long id;
    private String code;
    private String name;

    public static CemeteryTypeDictionaryEntity createWithId(Long id) {
        CemeteryTypeDictionaryEntity entity = new CemeteryTypeDictionaryEntity();
        entity.setId(id);
        return entity;
    }

}
