package gift.product.controller;

import gift.global.common.dto.PageResponseDto;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.GetProductResponseDto;
import gift.product.dto.UpdateProductRequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProductResponseDto> getProductById(
        @PathVariable(name = "productId") Long productId) {
        GetProductResponseDto responseDto = productService.getProductById(productId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<GetProductResponseDto>> getProductByPageRequest(
        @Valid @PageableDefault Pageable pageable
    ) {
        PageResponseDto<GetProductResponseDto> pageResponseDto = productService.getAllByPage(
            pageable);
        return ResponseEntity.ok(pageResponseDto);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody CreateProductRequestDto dto) {
        Long id = productService.createProduct(dto);
        URI uri = URI.create("/api/products/" + id);
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable(name = "id") Long id,
        @Valid @RequestBody UpdateProductRequestDto dto) {
        productService.updateProduct(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
