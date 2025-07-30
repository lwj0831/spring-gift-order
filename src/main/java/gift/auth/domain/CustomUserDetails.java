package gift.auth.domain;

import gift.member.domain.MemberType;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private Long memberId;
    private String email;
    private String password;
    private MemberType memberType;

    public CustomUserDetails(Long memberId, String email, String password, MemberType memberType) {
        this.memberId = memberId;
        this.email = email;
        this.password = null;
        this.memberType = memberType;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Long getUserId() {
        return memberId;
    }

    public MemberType getMemberType() {
        return memberType;
    }
}
