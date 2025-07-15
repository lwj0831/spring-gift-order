package gift.product.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.exception.dto.ErrorResponse;
import gift.product.dto.CreateProductOptionDto;
import gift.product.dto.CreateProductRequestDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductApiStructuredE2ETest {

    @LocalServerPort
    int port;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        this.restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @Test
    void 상품등록_빈_이름으로_요청시_400_응답반환() throws Exception {
        var request = new CreateProductRequestDto("   ", 1000, "설명입니다",
            "https://example.com/image.jpg", createValidOptions());

        var response = createProductWithErrorResponse(request);
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.errorCode()).isEqualTo("GLOBAL-001");

        @SuppressWarnings("unchecked")
        List<Map<String, String>> invalidParams = (List<Map<String, String>>) errorResponse.extras()
            .get("invalid-params");

        assertThat(invalidParams).anySatisfy(param -> {
            assertThat(param.get("name")).isEqualTo("name");
            assertThat(param.get("reason")).contains("상품명은 필수값입니다");
        });
    }

    @Test
    void 상품등록_카카오포함된_이름으로_요청시_400_응답반환() throws Exception {
        var request = new CreateProductRequestDto("카카오 초콜릿", 1000, "설명입니다",
            "https://example.com/image.jpg", createValidOptions());

        var response = createProductWithErrorResponse(request);
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.errorCode()).isEqualTo("PRODUCT-002");
    }

    @Test
    void 상품등록_유효하지않은_가격으로_요청시_400_응답반환() throws Exception {
        var request = new CreateProductRequestDto("상품이름", -10, "설명입니다",
            "https://example.com/image.jpg", createValidOptions());

        var response = createProductWithErrorResponse(request);
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.errorCode()).isEqualTo("GLOBAL-001");

        @SuppressWarnings("unchecked")
        List<Map<String, String>> invalidParams = (List<Map<String, String>>) errorResponse.extras()
            .get("invalid-params");

        assertThat(invalidParams).anySatisfy(param -> {
            assertThat(param.get("name")).isEqualTo("price");
            assertThat(param.get("reason")).contains("0원 이상");
        });
    }

    @Test
    void 상품등록_옵션_없을경우_400_반환() throws Exception {
        var request = new CreateProductRequestDto("정상상품", 1000, "설명입니다",
            "https://example.com/image.jpg", List.of());

        var response = createProductWithErrorResponse(request);
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorResponse.errorCode()).isEqualTo("GLOBAL-001");
        assertThat(errorResponse.errorMessage()).contains("상품에는 최소 하나 이상의 옵션이 있어야 합니다.");

    }

    @Test
    void 상품리스트조회_잘못된_정렬필드_400_반환() throws Exception {
        var response = restClient.get()
            .uri("/api/products?page=0&size=10&sort=nonExistentField,asc")
            .exchange((req, res) -> {
                var body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                return ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders())
                    .body(body);
            });

        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = mapper.readValue(response.getBody(), ErrorResponse.class);

        assertAll(() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
            () -> assertThat(errorResponse.errorCode()).isEqualTo("PRODUCT-003"),
            () -> assertThat(errorResponse.errorMessage()).contains("다음 정렬 필드는 허용되지 않습니다"));
    }

    private List<CreateProductOptionDto> createValidOptions() {
        return List.of(new CreateProductOptionDto("옵션1", 100));
    }

    private CreateProductRequestDto createValidProductRequest() {
        return new CreateProductRequestDto(
            "정상상품",
            1000,
            "정상적인 설명입니다.",
            "https://example.com/image.jpg",
            createValidOptions()
        );
    }

    private ResponseEntity<Void> createProduct(CreateProductRequestDto request) {
        return restClient.post()
            .uri("/api/products")
            .body(request)
            .exchange((req, res) -> ResponseEntity.status(res.getStatusCode())
                .headers(res.getHeaders())
                .body(null));
    }

    private ResponseEntity<String> createProductWithErrorResponse(CreateProductRequestDto request) {
        return restClient.post()
            .uri("/api/products")
            .body(request)
            .exchange((req, res) -> {
                var body = new String(res.getBody().readAllBytes(), StandardCharsets.UTF_8);
                return ResponseEntity.status(res.getStatusCode()).headers(res.getHeaders())
                    .body(body);
            });
    }

    private Long createTestProduct() {
        var request = createValidProductRequest();
        var response = createProduct(request);

        URI location = response.getHeaders().getLocation();
        return Long.valueOf(location.getPath().substring(location.getPath().lastIndexOf("/") + 1));
    }
}
