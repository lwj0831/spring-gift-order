# 🔐 STEP4 1단계 - 카카오 로그인 연동

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

# STEP4 2단계 - 주문하기

---

## 구현 내용

### 1. 주문 도메인 개발
- [x] Order 엔티티 정의
  - Member-Order(1:N), Order-Product(N:N)
  - Order-Product(N:N) 중간 테이블 역할 OrderProduct 정의 (Order-OrderProduct-Product)
  - 도메인 로직 구현
- [x] 주문 레포지토리 구현
  - fetch join으로 N+1 문제 개선
- [x] 주문 서비스 구현
  - 주문하기 관련 비즈니스 정책 구체화
    - 카카오 소셜 로그인 회원의 경우 카카오 메세지 수신
    - 기존 일반 회원의 경우 카카오 메세지 수신 안함
  - 비즈니스 로직 구현
    - 주문하기 시 카카오 메세지 기능 도입
    - 주문하기 시 연관 위시아이템 차감
  - 트랜잭션 관리
- [x] 주문 컨트롤러 개발
  - API 경로 설정
- [x] 주문 관련 예외 정의 및 핸들러 구현

### 2. 카카오 토큰 저장 구현
- [x] KakaoAuth 엔티티 정의
  - 카카오 accessToken, refreshToken 정보 담는 역할 담당
- [x] KakaoAuth 레포지토리, 서비스 구현

---
# STEP4 3단계 - 배포하기
---
## 구현 내용
### 1. CORS 정책 구현
- [x] 스프링 시큐리티 설정 내 CORS 정책 구현
- [x] CORS 구현 사항 테스트 코드 작성
### 2. HTTPS 적용
- [x] 모든 요청에 대해 HTTPS 적용하도록 설정
### 3. 배포하기
- [x] github actions를 이용한 CD파이프라인 구축
  - 배포 스크립트 작성
- [x] EC2 인스턴스 생성 및 초기 세팅
  - Java, MySQL 설치
  - MYSQL 유저 및 DB 생성
- [x] 배포 후 Postman으로 API호출 테스트 진행
