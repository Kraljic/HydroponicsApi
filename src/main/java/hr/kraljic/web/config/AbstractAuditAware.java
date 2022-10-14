package hr.kraljic.web.config;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditAware {
    @CreatedBy
    @Column(nullable = true, updatable = false, columnDefinition = "varchar(45) default 'system'")
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedBy
    @Column(nullable = true, columnDefinition = "varchar(45) default 'system'")
    private String modifiedBy;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime modifiedAt = LocalDateTime.now();
}
