# Lesson82 - 온라인 학습 플랫폼

Lesson82는 스프링 부트로 구축된 종합적인 온라인 학습 플랫폼입니다. 강사와 학생을 연결하여 다양한 카테고리의 폭넓은 강좌와 수업을 제공합니다.

## 주요 기능

- 사용자 인증 및 권한 부여
- 강좌/수업 관리
- 장바구니 시스템
- 수강 신청 및 결제 처리
- 사용자 프로필 (학생 및 강사)
- 관리자 대시보드
- 카테고리 기반 강좌 탐색

## 사용 기술

- Java 17
- Spring Boot 3.3.0
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Gradle
- Lombok

## 시작하기

### 필수 조건

- JDK 17
- MySQL
- Gradle

### 설정

1. 저장소 복제:
   ```
   git clone https://github.com/your-username/lesson82.git
   ```

2. 프로젝트 디렉토리로 이동:
   ```
   cd lesson82
   ```

3. `src/main/resources/application.properties`에서 데이터베이스 연결 설정:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. 프로젝트 빌드:
   ```
   ./gradlew build
   ```

5. 애플리케이션 실행:
   ```
   ./gradlew bootRun
   ```

이제 애플리케이션이 `http://localhost:8080`에서 실행되어야 합니다.

## 프로젝트 구조

- `com.hkit.lessons`: 메인 패키지
  - `banner`: 배너 관리
  - `cart`: 장바구니 기능
  - `category`: 강좌 카테고리
  - `enrollment`: 수강 신청
  - `lesson`: 수업 관리
  - `member`: 사용자 관리
  - `options`: 수업 옵션
  - `pro`: 강사 프로필

## 기여하기

기여 방법과 행동 강령에 대한 자세한 내용은 [CONTRIBUTING.md](CONTRIBUTING.md)를 참조해 주세요.

## 라이선스

이 프로젝트는 MIT 라이선스 하에 있습니다. 자세한 내용은 [LICENSE.md](LICENSE.md) 파일을 참조하세요.

## 감사의 말

- Lesson82를 만드는 데 도움을 주신 모든 기여자분들께 감사드립니다.
- 훌륭한 문서와 지원을 제공해 주신 스프링 부트와 자바 커뮤니티에 특별히 감사드립니다.

