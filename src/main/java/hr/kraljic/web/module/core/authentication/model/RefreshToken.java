package hr.kraljic.web.module.core.authentication.model;

import hr.kraljic.web.module.core.authentication.TokenType;
import hr.kraljic.web.module.core.user.model.ApplicationUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(exclude = {"user"})
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "REFRESH_TOKEN_HASH", nullable = false)
    private String refreshTokenHash;

    /**
     * Not saved to database, can't be accessed when retrieved from database
     */
    @Transient
    private String refreshToken;

    @Column(name = "ACCESS_TOKEN_HASH", nullable = false)
    private String accessTokenHash;

    /**
     * Not saved to database, can't be accessed when retrieved from database
     */
    @Transient
    private String accessToken;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "TOKEN_TYPE", nullable = false)
    private TokenType tokenType;

    @Column(name = "valid", nullable = false)
    private boolean valid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private ApplicationUser user;
}
