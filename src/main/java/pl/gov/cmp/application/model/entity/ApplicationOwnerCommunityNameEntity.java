package pl.gov.cmp.application.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="application_owner_community_name")
public class ApplicationOwnerCommunityNameEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_owner_community_name_id_seq")
        @SequenceGenerator(name = "application_owner_community_name_id_seq", sequenceName = "application_owner_community_name_id_seq", allocationSize = 1)
        private Long id;
        private String name;
        private String source;
        private String codeSimc;
        private String levelName;
}
