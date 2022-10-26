package pl.gov.cmp.file.repository.cemetery.image;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gov.cmp.file.model.entity.ApplicationImageEntity;

interface JpaImageApplicationRepository extends JpaRepository<ApplicationImageEntity,Long> {
}
