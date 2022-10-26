package pl.gov.cmp.dictionary.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "churches_religions_dictionary")
public class ChurchReligionDictionaryEntity {

    @Id
    private Long id;
    private String code;
    private String name;
    private boolean regulatedByLaw;

    public static ChurchReligionDictionaryEntity createWithId(Long id) {
        ChurchReligionDictionaryEntity entity = new ChurchReligionDictionaryEntity();
        entity.setId(id);
        return entity;
    }
}
