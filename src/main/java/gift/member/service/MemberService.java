package gift.member.service;

import gift.member.domain.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberJpaRepository memberRepository;

    public MemberService(MemberJpaRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
