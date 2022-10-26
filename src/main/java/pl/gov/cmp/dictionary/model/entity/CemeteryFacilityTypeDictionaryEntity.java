package pl.gov.cmp.dictionary.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cemetery_facility_types_dictionary")
public class CemeteryFacilityTypeDictionaryEntity {

    @Id
    private Long id;
    private String code;
    private String name;

    public static CemeteryFacilityTypeDictionaryEntity createWithId(Long id) {
        CemeteryFacilityTypeDictionaryEntity entity = new CemeteryFacilityTypeDictionaryEntity();
        entity.setId(id);
        return entity;
    }
}
