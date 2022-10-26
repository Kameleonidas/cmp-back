package pl.gov.cmp.administration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gov.cmp.administration.model.entity.InvitationEntity;
import pl.gov.cmp.administration.model.enums.InstitutionType;
import pl.gov.cmp.administration.model.enums.MessageStatus;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<InvitationEntity, Long> {
    @Query(value = "select i from InvitationEntity i " +
            "where i.message.emailSender.emailTo = :email and i.institutionType = :institutionType and i.institutionId = :institutionId")
    Optional<InvitationEntity> findByEmailAndInstitutionTypeAndInstitutionId(@Param("email") String email,
                                                                             @Param("institutionType") InstitutionType institutionType,
                                                                             @Param("institutionId") Long institutionId);
    @Query(value = "select i from InvitationEntity i " +
            "where i.message.status = :status and i.requestIdentifier = :requestIdentifier")
    Optional<InvitationEntity> findByRequestIdentifierAndStatus(@Param("requestIdentifier") String requestIdentifier,
                                                                @Param("status") MessageStatus status);

    Optional<InvitationEntity> findByRequestIdentifier(String requestIdentifier);
}
