package pl.gov.cmp.gugik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.gov.cmp.gugik.model.entity.GugikCemeteryGeometryEntity;

import java.util.List;
import java.util.Optional;

public interface GugikCemeteryGeometryRepository extends JpaRepository<GugikCemeteryGeometryEntity, Long> {


    String CEMETERIES_WITHOUT_ADDRESSES_QUERY =
            "from GugikCemeteryGeometryEntity geometry " +
                    "where " +
                    "(type = pl.gov.cmp.gugik.model.enums.CemeteryItemType.CEMETERY_SURFACE or " +
                    "(type = pl.gov.cmp.gugik.model.enums.CemeteryItemType.BURIAL_SURFACE and cemetery = null)) and address = null";

    @Query(value = "select geometry " + CEMETERIES_WITHOUT_ADDRESSES_QUERY)
    List<GugikCemeteryGeometryEntity> findCemeteriesWithoutAddresses();

    @Query(value = "select count(geometry) " + CEMETERIES_WITHOUT_ADDRESSES_QUERY)
    Long countCemeteriesWithoutAddresses();

    Optional<GugikCemeteryGeometryEntity> findByIdIipStartingWith(String idIip);
}
