package gift.wishlist.exception;

import gift.global.exception.BusinessException;

public class WishItemNotFoundException extends BusinessException {

    public WishItemNotFoundException(Long id) {
        super(WishItemErrorCode.WISH_ITEM_NOT_FOUND);
        addArgument("위시아이템 ID", id);
    }

}
