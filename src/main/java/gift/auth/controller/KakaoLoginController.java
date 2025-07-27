package gift.auth.controller;

import gift.auth.config.KakaoOauthProperties;
import gift.auth.dto.KakaoTokenResponseDto;
import gift.auth.dto.LoginResponseDto;
import gift.auth.service.KakaoApiClient;
import gift.auth.service.KakaoOauthService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/auth/kakao")
public class KakaoLoginController {

    private final KakaoOauthService kakaoOauthService;

    public KakaoLoginController(KakaoOauthService kakaoOauthService) {
        this.kakaoOauthService = kakaoOauthService;
    }

    @GetMapping("/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String uri = kakaoOauthService.createAuthorizeRedirectUrl();
        response.sendRedirect(uri);
    }

    @GetMapping("/callback")
    public ResponseEntity<LoginResponseDto> redirectKakaoLogin(@RequestParam(name="code")String code){
        return ResponseEntity.ok(kakaoOauthService.loginKakaoUser(code));
    }

}
