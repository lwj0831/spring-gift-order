# 🧱 1단계 - JPA 기반 도메인 전환 및 환경별 DB 구성

해당 단계는 기존 JDBC 기반 구조를 제거하고, **도메인 객체와 JPA Entity를 통합하여**
보다 일관된 모델링과 유지보수가 가능한 구조로 전환한 작업입니다.  
또한, **환경별로 사용하는 DB(H2 또는 MySQL)를 분리 설정**하여 개발/테스트 간 격리를 가능하게 했습니다.

---

## ✅ 구현 사항

### 📌 1. WishItem, Member, Product 도메인에 대한 JPA 전환

- [x] 기존 도메인 객체와 Entity 클래스를 통합하고 `@Entity`, `@Table`, `@Id` 등 JPA 어노테이션을 직접 도메인에 적용
- [x] WishItem → Member, Product 간 관계를 객체 참조 기반으로 매핑 (`@ManyToOne`)
- [x] 도메인 간 연관 관계를 기반으로 fetch join, DTO 조회 혼합 적용
- [x] N+1 문제를 방지하기 위해 일부 조회 쿼리에 `fetch join` 적용

### 📌 2. 개발/테스트 환경별 DB 구성

- [x] JPA Repository 구조는 고정
- [x] `default` : 리뷰어 실행환경 - **H2(In-memory)** (명시적으로 사용하지 않음)
- [x] `dev` : 개발환경 - **MYSQL**
- [x] `test` : 테스트환경 - **H2(In-memory)** (E2E테스트 시 시큐리티 인증 필터 무효화)

---

## 🚀 향후 확장 계획

- [ ] 각 도메인에 대한 JPA 기반 단위 테스트 작성
- [ ] 생성/수정 시각을 관리하는 `TimeBaseEntity` 공통 엔티티 도입
- [ ] 연관 관계 설정 고도화: `@OneToMany`, `cascade`, `orphanRemoval` 등 영속성 전이 정책 적용

---

# 📄 2단계 - 페이지네이션

해당 단계에서는 상품 목록 및 위시리스트 조회 API에 **페이지네이션 기능을 도입**하여,  
대용량 데이터에서도 효율적인 화면 렌더링 및 응답 처리가 가능하도록 구조를 개선하였습니다.  
또한 도메인별로 발생하는 `NotFoundException`에 대해 **식별자 정보를 포함한 상세 메시지 응답**이 가능하도록 변경하였습니다.

---

## ✅ 구현 내용

### 📌 1. 상품/위시리스트 목록 API에 Pageable 기반 페이지네이션 적용

- [x] Spring Data의 `Pageable`과 `@PageableDefault`를 활용하여 offset 기반 페이지네이션 구현
- [x] 클라이언트 요청에서 `page`, `size`, `sort` 파라미터로 페이지 위치 및 정렬 조건 지정 가능
- [x] 정렬 필드가 유효하지 않을 경우 `ProductValidator`를 통해 검증하여 400 Bad Request 반환

### 📌 2. 도메인별 NotFoundException 개선

- 상품, 회원, 위시리스트 등 조회 실패 시 발생하는 `NotFoundException`에 **식별자 값 포함**
- 클라이언트 디버깅을 돕기 위해 응답 메시지에 `"요청한 ID: 123"` 등의 구체적인 정보 제공

### 🚀 향후 구현 계획

- 커서 기반 페이지네이션 완성 및 API 전환
- 커서 기반 페이징 전략을 **정렬 필드별로 추상화**하여 **동적 처리 가능**하게 구성 또는 QueryDSL 도입하여 동적 쿼리 생성

---

## 3단계 - 상품 옵션

### ✅ 구현 내용

### 📌 1. 위시 아이템 삭제 시 리소스 소유권 검증 로직 추가

### 📌 2. ProductOption 요구 사항 구현

- Controller, Service, Repository 계층 구현
- 예외 처리 및 검증 책임을 분리한 `ProductOptionValidationService` 도입
- 검증 로직: 옵션 이름 유효성, 수량 범위 유효성
- 단위 테스트 및 통합 테스트 작성

### 📌 3. Product 등록 시 최소 1개의 옵션이 포함되도록 검증 로직 및 로직 리팩토링

---

### 추후 구현 사항

- `delete` 시 Race Condition 해결
    - 다중 요청으로 인한 동시 삭제, 수량 차감 등에서의 정합성 이슈 대응 필요
    - 낙관적 락 또는 비관적 락, 버전 필드를 활용한 전략 고려

- Soft Delete 도입
    - 물리 삭제 대신 삭제 여부 필드(`deleted`, `is_active` 등)로 상태 관리


