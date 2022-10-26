package pl.gov.cmp.cemetery.model.entity;


import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cemetery_owner_community_name")
public class CemeteryOwnerCommunityNameEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cemetery_owner_community_name_id_seq")
        @SequenceGenerator(name = "cemetery_owner_community_name_id_seq", sequenceName = "cemetery_owner_community_name_id_seq", allocationSize = 1)
        private Long id;
        private String name;
        private String source;
        private String codeSimc;
        private String levelName;
}
