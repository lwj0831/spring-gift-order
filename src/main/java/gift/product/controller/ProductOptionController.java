package gift.product.controller;

import gift.global.common.dto.PageResponseDto;
import gift.product.dto.CreateProductOptionsRequestDto;
import gift.product.dto.GetProductOptionResponseDto;
import gift.product.service.ProductOptionService;
import jakarta.validation.Valid;
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
@RequestMapping("/api")
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    public ProductOptionController(ProductOptionService productOptionService) {
        this.productOptionService = productOptionService;
    }

    @PostMapping("/products/{productId}/options")
    public ResponseEntity<Void> registerProductOption(
        @PathVariable(name = "productId") Long productId,
        @Valid @RequestBody CreateProductOptionsRequestDto dto) {
        productOptionService.registerProductOptionList(productId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{productId}/options")
    public ResponseEntity<PageResponseDto<GetProductOptionResponseDto>> getProductOptions(
        @PathVariable(name = "productId") Long productId, @PageableDefault(sort = "name")
    Pageable pageable) {
        return ResponseEntity.ok(productOptionService.getProductOptions(productId, pageable));
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<Void> deleteProductOption(@PathVariable("optionId") Long optionId) {
        productOptionService.deleteProductOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
