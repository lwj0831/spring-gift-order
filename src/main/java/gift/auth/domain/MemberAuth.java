package gift.auth.domain;

import gift.global.common.jpa.TimeBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member_auth")
public class MemberAuth extends TimeBaseEntity {

    @Id
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String refreshToken;

    public MemberAuth() {
    }

    public MemberAuth(Long id, String email, String password, String refreshToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
    }

    public static MemberAuth withId(Long id, String email, String password) {
        return new MemberAuth(id, email, password, null);
    }

    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void expiredRefreshToken() {
        this.refreshToken = null;
    }

    public boolean matchRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
