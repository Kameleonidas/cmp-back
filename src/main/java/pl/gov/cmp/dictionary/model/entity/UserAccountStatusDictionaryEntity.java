package pl.gov.cmp.dictionary.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_account_statuses_dictionary")
public class UserAccountStatusDictionaryEntity {

    @Id
    private Long id;
    private String code;
    private String name;
}
