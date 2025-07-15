package gift.wishlist.controller;

import gift.auth.annotation.LoginUser;
import gift.global.common.dto.PageResponseDto;
import gift.wishlist.dto.GetWishItemResponseDto;
import gift.wishlist.dto.RegisterWishItemRequestDto;
import gift.wishlist.service.WishItemService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wish-items")
public class WishItemController {

    private final WishItemService wishItemService;

    public WishItemController(WishItemService wishItemService) {
        this.wishItemService = wishItemService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<GetWishItemResponseDto>> getWishItems(
        @LoginUser Long memberId,
        @PageableDefault
        Pageable pageable) {
        PageResponseDto<GetWishItemResponseDto> wishItems = wishItemService.findWishItemsByPage(
            memberId,
            pageable);
        return ResponseEntity.ok(wishItems);
    }

    @PostMapping
    public ResponseEntity<Void> addWishItem(@LoginUser Long memberId,
        @Valid @RequestBody RegisterWishItemRequestDto dto) {
        Long id = wishItemService.registerWishItem(memberId, dto);
        URI uri = URI.create("/api/wish-items" + id);
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{wishItemId}")
    public ResponseEntity<Void> deleteWishItem(@LoginUser Long memberId,
        @PathVariable(name = "wishItemId") Long wishItemId) {
        wishItemService.deleteWishItem(memberId, wishItemId);
        return ResponseEntity.noContent().build();
    }

}
