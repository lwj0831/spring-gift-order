package gift.auth.controller;

import gift.auth.domain.CustomUserDetails;
import gift.auth.dto.LoginRequestDto;
import gift.auth.dto.LoginResponseDto;
import gift.auth.dto.RefreshTokenRequestDto;
import gift.auth.dto.RegisterMemberRequestDto;
import gift.auth.dto.RegisterMemberResponseDto;
import gift.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterMemberResponseDto> registerMember(
        @RequestBody RegisterMemberRequestDto dto) {
        RegisterMemberResponseDto responseDto = authService.registerMember(dto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto responseDto = authService.login(dto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto dto) {
        LoginResponseDto responseDto = authService.refreshToken(dto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        authService.logout(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
