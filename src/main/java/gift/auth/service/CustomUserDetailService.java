package gift.auth.service;

import gift.auth.domain.CustomUserDetails;
import gift.auth.domain.MemberAuth;
import gift.auth.repository.MemberAuthJpaRepository;
import gift.member.domain.Member;
import gift.member.repository.MemberJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final MemberAuthJpaRepository memberAuthRepository;
    private final MemberJpaRepository memberRepository;

    public CustomUserDetailService(MemberAuthJpaRepository memberAuthRepository,
        MemberJpaRepository memberRepository) {
        this.memberAuthRepository = memberAuthRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberAuth memberAuth = memberAuthRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberAuth.getId())
            .orElseThrow(() -> new UsernameNotFoundException("회원 정보를 찾을 수 없습니다"));

        return new CustomUserDetails(member.getId(), memberAuth.getEmail(),
            memberAuth.getPassword());
    }
}
