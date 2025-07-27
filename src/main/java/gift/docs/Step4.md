# 🔐 3단계 - 카카오 로그인 연동

해당 단계에서는 **카카오 로그인 기능을 도입**하여 외부 인증 기반 사용자 로그인 흐름을 처리할 수 있도록 구현하였습니다.  
또한, **카카오톡 메시지 전송과 같은 API 활용을 위해 accessToken 발급 흐름까지 구성**하였으며, 추후 선택적 메시지 전송 기능으로 확장 가능한 구조로 설계하였습니다.

---

## ✅ 구현 내용

### 📌 1. 카카오 로그인 요청 URL 생성 및 리다이렉트 처리

- [x] `/api/auth/kakao/login` 요청 시, Kakao 인증 페이지로 리다이렉트
- [x] 인증 요청 URL은 `KakaoOauthService`에서 생성하여 Controller의 책임을 분리

### 📌 2. 카카오 인가 코드 수신 및 accessToken 발급

- [x] `/api/auth/kakao/callback`에서 `code` 파라미터 수신
- [x] `KakaoApiClient.getAccessToken(code)`를 통해 accessToken 및 refreshToken 발급
- [x] `KakaoApiClient.getUserInfo(accessToken)` 호출로 사용자 정보(email, id) 조회

### 📌 3. 카카오 회원 처리 및 JWT 발급

- [x] 조회된 이메일 기반으로 기존 회원 여부 확인
- [x] 없을 경우 `kakaoUser{ID}@kakao.com` 형식으로 사용자 등록
- [x] 이후 `accessToken`, `refreshToken`을 생성하여 JWT 응답 반환

### 📌 4. 기타 구성 요소

- [x] `KakaoApiClient` 클래스에서 카카오 API 호출 책임 분리
- [x] 인증 실패 및 서버 오류 대응을 위한 `KakaoApiClientException`, `KakaoApiServerException` 정의
- [x] `application.yml`에 `client_id`, `client_secret`, 각종 카카오 API URL 설정 값 추가

---

## 🚀 향후 구현 계획

- ✅ 테스트 코드 작성  
  → 카카오 로그인 및 메시지 전송 흐름에 대한 통합 테스트, 예외 발생 케이스 검증

- ✅ 자체 이메일 정책과 충돌 방지  
  → 일반 회원가입 시 `@kakao.com` 도메인을 사용할 수 없도록 검증 로직 추가 예정

---