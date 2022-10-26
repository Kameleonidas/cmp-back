package pl.gov.cmp.dictionary.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cemetery_sources_dictionary")
public class CemeterySourceDictionaryEntity {

    @Id
    private Long id;
    private String code;
    private String name;

}
