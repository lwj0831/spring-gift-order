package gift.wishlist.service;

import gift.auth.exception.ForbiddenException;
import gift.global.common.dto.PageResponseDto;
import gift.member.domain.Member;
import gift.member.service.MemberService;
import gift.product.domain.Product;
import gift.product.service.ProductService;
import gift.wishlist.domain.WishItem;
import gift.wishlist.dto.GetWishItemResponseDto;
import gift.wishlist.dto.RegisterWishItemRequestDto;
import gift.wishlist.exception.WishItemAlreadyExistsException;
import gift.wishlist.exception.WishItemNotFoundException;
import gift.wishlist.repository.WishItemJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishItemService {

    private final WishItemJpaRepository wishItemRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public WishItemService(WishItemJpaRepository wishItemRepository, MemberService memberService,
        ProductService productService) {
        this.wishItemRepository = wishItemRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    @Transactional
    public Long registerWishItem(Long memberId, RegisterWishItemRequestDto dto) {
        Member member = memberService.findMemberOrThrow(memberId);
        Product product = productService.findProductOrThrow(dto.productId());

        if (wishItemRepository.findByMemberIdAndProductId(member.getId(), product.getId())
            .isPresent()) {
            throw new WishItemAlreadyExistsException();
        }

        return wishItemRepository.save(WishItem.of(member, product)).getId();
    }

    @Transactional(readOnly = true)
    public PageResponseDto<GetWishItemResponseDto> findWishItemsByPage(Long memberId,
        Pageable pageable) {
        memberService.findMemberOrThrow(memberId);

        Page<GetWishItemResponseDto> pagedDto = wishItemRepository.findAllWithProductByMemberId(
            memberId, pageable).map(GetWishItemResponseDto::from);
        return PageResponseDto.from(pagedDto);
    }

    @Transactional
    public void deleteWishItem(Long memberId, Long wishItemId) {
        WishItem wishItem = findWishItemOrThrow(wishItemId);

        if (!memberId.equals(wishItem.getMember().getId())) {
            throw new ForbiddenException();
        }
        wishItemRepository.deleteById(wishItemId);
    }

    @Transactional(readOnly = true)
    public WishItem findWishItemOrThrow(Long id) {
        return wishItemRepository.findById(id).orElseThrow(() -> new WishItemNotFoundException(id));
    }
}
