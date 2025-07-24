package gift.member.exception;

import gift.global.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {


    public MemberNotFoundException(Long id) {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
        addArgument("회원 ID", id);
    }

    public MemberNotFoundException(String email) {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
        addArgument("회원 Email", email);
    }

}
