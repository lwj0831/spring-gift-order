# spring-gift-order

---

## API 테스트 가이드

### 배포 상태

* EC2 배포 완료: Java 21 + MySQL 8.0 환경
* 데이터베이스 설정: 더미 데이터 포함
* 환경변수 구성: MYSQL DB, 카카오 OAuth 관련 변수 설정 완료
* 프론트엔드: 별도 뷰 없음 (API 테스트 전용)

---

### 더미 데이터 정보

#### 상품 데이터 (Products)

* 상품 ID: 100\~110 (총 11개)
* 예시:

    * ID 100: 테스트 상품0 (100원)
    * ID 101: 테스트 상품1 (1,000원)
    * ...
    * ID 110: 테스트 상품10 (10,000원)

#### 테스트 계정 (Members)

* 사용자 ID: 100
* 이름: lee
* 이메일: [wjl0831@gmail.com](mailto:wjl0831@gmail.com)
* 비밀번호: 1234 (암호화 저장됨)
* 타입: 일반 회원 (0)

#### 위시리스트 데이터

* 사용자 ID 100의 위시리스트:

    * 상품 100, 101, 103, 105, 107, 109, 110 (총 7개)

#### 상품 옵션 데이터

* 상품 ID 100의 옵션들:

    * 옵션 ID 100: 옵션명1 (재고 100개)
    * ...
    * 옵션 ID 104: 옵션명5 (재고 500개)

---

### 테스트 엔드포인트

#### 인증 (Authentication)

* 로그인
  `POST http://3.37.117.41:8080/api/auth/login`

  ```json
  {
      "email": "wjl0831@gmail.com",
      "password": "1234"
  }
  ```

* 회원가입
  `POST http://3.37.117.41:8080/api/auth/register`

  ```json
  {
      "username": "testuser",
      "email": "test@example.com",
      "password": "1234"
  }
  ```

* 로그아웃
  `POST http://3.37.117.41:8080/api/auth/logout`
  Header: `Authorization: Bearer {access_token}`

* 토큰 재발급
  `POST http://3.37.117.41:8080/api/auth/refresh`

  ```json
  {
      "refreshToken": "{refresh_token}"
  }
  ```

---

#### 상품 관리 (Products)

* 상품 목록 조회
  `GET http://3.37.117.41:8080/api/products?page=0&size=10&sort=name,asc`

* 특정 상품 상세 조회
  `GET http://3.37.117.41:8080/api/products/100`

* 상품 생성 (인증 필요)
  `POST http://3.37.117.41:8080/api/products`
  Header: `Authorization: Bearer {access_token}`

  ```json
  {
      "name": "새로운 테스트 상품",
      "price": 15000,
      "description": "API로 생성한 상품입니다",
      "imageUrl": "https://example.com/image.jpg"
  }
  ```

* 상품 수정
  `PUT http://3.37.117.41:8080/api/products/100`

* 상품 삭제
  `DELETE http://3.37.117.41:8080/api/products/110`

---

#### 상품 옵션 (Product Options)

* 옵션 목록 조회
  `GET http://3.37.117.41:8080/api/products/100/options?page=0&size=10&sort=id,asc`

* 단일 옵션 등록
  `POST http://3.37.117.41:8080/api/products/100/options`

* 여러 옵션 등록
  `POST http://3.37.117.41:8080/api/products/101/options`

  ```json
  {
      "optionRequestDtoList": [
          { "name": "블랙", "quantity": 10 },
          { "name": "화이트", "quantity": 15 }
      ]
  }
  ```

* 옵션 삭제
  `DELETE http://3.37.117.41:8080/api/options/104`

---

#### 위시리스트 (Wishlist)

* 위시리스트 조회
  `GET http://3.37.117.41:8080/api/wish-items`

* 위시 아이템 추가
  `POST http://3.37.117.41:8080/api/wish-items`

  ```json
  {
      "productId": 102
  }
  ```

* 위시 아이템 삭제
  `DELETE http://3.37.117.41:8080/api/wish-items/{wish_item_id}`

---

#### 주문 (Orders)

* 주문 조회
  `GET http://3.37.117.41:8080/api/orders`

* 주문하기
  `POST http://3.37.117.41:8080/api/orders`

  ```json
  {
      "orderProductList": [
          { "productId": 100, "productOptionId": 100, "orderQuantity": 2 },
          { "productId": 100, "productOptionId": 101, "orderQuantity": 3 }
      ]
  }
  ```

---