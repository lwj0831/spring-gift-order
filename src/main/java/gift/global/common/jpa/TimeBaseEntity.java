package gift.global.common.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimeBaseEntity {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime registerDate;

    @LastModifiedDate
    protected LocalDateTime updateDate;

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}

