package pl.gov.cmp.administration.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gov.cmp.administration.model.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    @Query(value = "select m from MessageEntity m left join m.emailSender s left join m.accountSender a " +
            "where s.emailTo = :email or a.email = :email order by m.sendDate")
    Page<MessageEntity> findByEmail(@Param("email") String email, Pageable pageable);
}
