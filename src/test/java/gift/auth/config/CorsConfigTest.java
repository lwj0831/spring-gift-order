package gift.auth.config;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CorsConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ALLOWED_ORIGIN = "http://localhost:3000";
    private static final String DISALLOWED_ORIGIN = "http://malicious-site.com";
    private static final String TEST_API_PATH = "/api/products";

    @Test
    @DisplayName("preflight 요청 - 허용된 Origin, Method, Header 요청 시 응답이 성공적으로 반환된다")
    void testAllowedOriginPreflightRequest() throws Exception {
        mockMvc.perform(options(TEST_API_PATH)
                .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name())
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                    String.join(",", HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION)))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ALLOWED_ORIGIN))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                containsString(HttpHeaders.CONTENT_TYPE)))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                containsString(HttpHeaders.AUTHORIZATION)))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allOf(
                containsString("GET"),
                containsString("POST"),
                containsString("PUT"),
                containsString("DELETE"),
                containsString("OPTIONS")
            )));
    }

    @Test
    @DisplayName("preflight 요청 - 허용되지 않은 Origin 요청 시 응답으로 403(FORBIDDEN) + CORS 헤더가 없어야 한다")
    void testDisallowedOriginPreflightRequest() throws Exception {
        mockMvc.perform(options(TEST_API_PATH)
                .header(HttpHeaders.ORIGIN, DISALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name()))
            .andExpect(status().isForbidden())
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    @DisplayName("SOP 요청 - Origin 헤더 없는 SOP 요청은 응답으로 200(OK) + CORS 헤더가 없어야 한다")
    void testRequestWithoutOriginHeader() throws Exception {
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk())
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    @DisplayName("Simple Request - 허용된 Origin 요청 시 응답이 성공적으로 반환된다")
    void testAllowedOriginActualRequest() throws Exception {
        mockMvc.perform(get(TEST_API_PATH)
                .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ALLOWED_ORIGIN))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test
    @DisplayName("Simple Request - 허용되지 않은 Origin 요청 시 403(FORBIDDEN) + CORS 헤더가 없어야 한다")
    void testDisallowedOriginActualRequest() throws Exception {
        mockMvc.perform(get(TEST_API_PATH)
                .header(HttpHeaders.ORIGIN, DISALLOWED_ORIGIN))
            .andExpect(status().isForbidden())
            .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/api/products",
        "/api/wishItems",
        "/api/orders",
        "/api/auth/login"
    })
    @DisplayName("preflight 요청 - 여러 API 경로로 허용된 Origin, Method 요청 시 응답이 성공적으로 반환된다")
    void testCorsConsistencyAcrossEndpoints(String endpoint) throws Exception {
        mockMvc.perform(options(endpoint)
                .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpMethod.GET.name()))
            .andExpect(status().isOk())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ALLOWED_ORIGIN))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600"));
    }
}
