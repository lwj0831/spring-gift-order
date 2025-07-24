package gift.wishlist.exception;

import gift.global.exception.BusinessException;

public class WishItemAlreadyExistsException extends BusinessException {

    public WishItemAlreadyExistsException() {
        super(WishItemErrorCode.WISH_ITEM_ALREADY_EXISTS);
    }

}
