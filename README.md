# 🥕 당근코 – 지역 기반 러닝 코스 공유 플랫폼

> **“어디서 뛰어야 할지 모르는 초보 러너들을 위한 로컬 러닝 코스 공유 플랫폼”**  
> 당근코는 **동네 기반 러닝 코스 추천**과 **경험 공유 커뮤니티**를 통해  
> 부담 없이 러닝을 시작하고 지속할 수 있도록 돕는 서비스입니다.

---

## ✨ 프로젝트 개요

| 항목 | 내용 |
|------|------|
| **프로젝트명** | 당근코 (당신 근처의 코스) |
| **목적** | 초보 러너의 진입장벽을 낮추고, 런닝의 습관화에 기여 |
| **기간** | 2025.11 ~ 2025.12 |
| **참여자** | 주세빈(PM, FE, BE), 양동근(FE, BE) |
| **기술 스택** | **Backend**: Spring Boot, MyBatis · **DB**: MySQL · **Frontend**: Vue 3, Pinia, Vue Router · <br> **Etc**: (Naver Map API/Playwright map renderer/gemini API, Node.js) |

---

## 🚩 서비스 핵심 가치

### 😣 Problem
- 기존 러닝 앱은 **기록 중심** → **어디서 뛸 지**에 대한 문제 미해결
- 여러 플랫폼에 **분산된 코스 정보**와 줄글/캡처 이미지 등 **직관적이지 못한 형식**
- 러닝은 “그냥 집 근처에서 뛰면 되겠지”로 가볍게 시작하지만,  **정해진 장소가 없어 첫 코스 선택이 어렵고**,  **탐색 비용이 커져 포기 가능성**이 높아짐

### 🔍 Solution
#### A. 정보 분산 문제 → “플랫폼화”
- 포털/커뮤니티에 흩어진 코스 정보를 **하나의 플랫폼**으로 통합해 탐색 시간 감소
- 랭킹/좋아요/리뷰를 통해 **검증된 코스부터 접근** 가능

#### B. 직관성 부족 문제 → “지도 기반 + 정해진 양식(폼)”
- 글/캡처 이미지 대신, **지도 위 경로로 코스 시각화**
- **거리/난이도/편의시설** 같은 핵심 정보가 **정해진 폼으로 구조화**

#### C. 정확도 부족 문제 → “필터링/조건 검색”
- 지역/거리/난이도/편의시설 등 **필터 기반 검색**으로 수준별/개인별 탐색 가능
- 탐색 부담을 낮춰 **러닝 지속성(습관화)**을 높임

---

## 📌 핵심 기능

### ✅ 코스
- 📍 **위치/지역 기반 코스 탐색**
- 🔎 **코스 검색**: 키워드 + 지역/난이도/거리 등 조건 검색
- 🧭 **코스 상세 정보**
  - 거리, 소요시간, 페이스(분/㎞), 난이도
  - 편의시설 여부(화장실/횡단보도 등)
  - 평균 평점, 조회수, 등록일 등
- 🗺️ **코스 경로(포인트) 저장**
  - 코스는 여러 개의 포인트로 구성(순서 보장)

### ✅ 상호작용
- ❤️ **코스 좋아요(찜)**
  - 사용자별 좋아요 여부 토글
  - 마이페이지에서 좋아요 코스 모아보기
- ⭐ **리뷰 & 평가(별점/댓글)**

### ✅ 커뮤니티/소셜
- 👥 **팔로우 기능**
  - 다른 유저를 팔로우하여 관심 러너의 활동을 쉽게 추적
  - 다른 유저의 마이페이지에 들어가 **등록 코스 / 찜(좋아요) 코스**를 확인 가능

### ✅ AI 기능
- 🧠 **AI 러닝 코치**
  - 사용자 목표/상태에 맞춘 러닝 가이드(예: 페이스/거리/빈도) 제공
- 🏆 **AI 랭킹 요약**
  - 랭킹 1위 코스(또는 상위 코스)의 핵심 특징을 **자동 요약**해 빠르게 파악 가능

### ✅ 개인화
- 👤 **마이페이지**
  - 등록 코스 / 좋아요 코스 카드형 노출
  - “더보기”로 전체 목록 페이지 이동

### ✅ 랭킹/트렌드
- 🏆 **인기 코스 랭킹**
  - 좋아요/조회/평점 등 지표 기반 정렬(정책 확장 가능)
 
---

## 📦 ERD
<img width="800" height="450" alt="당근코-ERD" src="https://github.com/user-attachments/assets/49b74e61-fa5e-49f8-953a-4d4e48c5ae73" />

---

<details>
<summary><b>🗂️ 폴더 구조</b></summary>

```text
📦 BACK_DangGeunKo
┣ 📂 src/main/java/com/danggeunko
┃ ┣ 📂 ai
┃ ┣ 📂 auth
┃ ┃ ┣ 📂 jwt
┃ ┣ 📂 common
┃ ┃ ┣ 📂 config
┃ ┃ ┃ ┣ 📜 MyBatisConfig.java
┃ ┃ ┃ ┣ 📜 SecurityConfig.java
┃ ┃ ┃ ┣ 📜 SwaggerConfig.java
┃ ┃ ┃ ┗ 📜 WebConfig.java
┃ ┣ 📂 course
┃ ┣ 📂 review
┃ ┣ 📂 user
┃ ┗ 📂 thumbnail

📦 FRONT_DangGeunKo
┣ 📂 front-danggeunko
┃ ┣ 📂 src
┃ ┃ ┣ 📂 api
┃ ┃ ┃ ┗ 📜 axios.js
┃ ┃ ┣ 📂 assets
┃ ┃ ┃ ┣ 📂 font
┃ ┃ ┃ ┣ 📂 img
┃ ┃ ┃ ┣ 📜 script.js
┃ ┃ ┃ ┗ 📜 style.css
┃ ┃ ┣ 📂 components
┃ ┃ ┃ ┣ 📂 Course
┃ ┃ ┃ ┃ ┣ 📂 Create
┃ ┃ ┃ ┃ ┃ ┣ 📜 CourseForm.vue
┃ ┃ ┃ ┃ ┃ ┗ 📜 DrawingMap.vue
┃ ┃ ┃ ┃ ┣ 📂 Update
┃ ┃ ┃ ┃ ┃ ┣ 📜 UpdateCourse.vue
┃ ┃ ┃ ┃ ┃ ┗ 📜 UpdateDrawingMap.vue
┃ ┃ ┃ ┃ ┣ 📜 CourseCard.vue
┃ ┃ ┃ ┃ ┣ 📜 CourseDetail.vue
┃ ┃ ┃ ┃ ┗ 📜 CourseList.vue
┃ ┃ ┃ ┣ 📂 Footer
┃ ┃ ┃ ┃ ┣ 📂 AICoach
┃ ┃ ┃ ┃ ┃ ┣ 📜 CoachLoading.vue
┃ ┃ ┃ ┃ ┃ ┗ 📜 CoachModal.vue
┃ ┃ ┃ ┃ ┣ 📜 CoachButton.vue
┃ ┃ ┃ ┃ ┣ 📜 CourseNavBar.vue
┃ ┃ ┃ ┃ ┣ 📜 CousreFooter.vue
┃ ┃ ┃ ┃ ┗ 📜 CreateCourseBtn.vue
┃ ┃ ┃ ┣ 📂 Main
┃ ┃ ┃ ┃ ┣ 📜 CourseMap.vue
┃ ┃ ┃ ┃ ┣ 📜 CourseMapCreate.vue
┃ ┃ ┃ ┃ ┣ 📜 CourseSearchBar.vue
┃ ┃ ┃ ┃ ┗ 📜 StaticCourseMap.vue
┃ ┃ ┃ ┣ 📂 MyPage
┃ ┃ ┃ ┃ ┣ 📜 FollowSearchBar.vue
┃ ┃ ┃ ┃ ┣ 📜 LikeCourse.vue
┃ ┃ ┃ ┃ ┣ 📜 Profile.vue
┃ ┃ ┃ ┃ ┗ 📜 RegistCourse.vue
┃ ┃ ┃ ┣ 📂 Ranking
┃ ┃ ┃ ┃ ┣ 📜 AIcomment.vue
┃ ┃ ┃ ┃ ┣ 📜 AICommentLoading.vue
┃ ┃ ┃ ┃ ┣ 📜 RankingFilter.vue
┃ ┃ ┃ ┃ ┗ 📜 RankingList.vue
┃ ┃ ┃ ┣ 📂 Review
┃ ┃ ┃ ┃ ┣ 📜 Review.vue
┃ ┃ ┃ ┃ ┣ 📜 ReviewCard.vue
┃ ┃ ┃ ┃ ┗ 📜 ReviewForm.vue
┃ ┃ ┃ ┗ 📜 CourseFilter.vue
┃ ┃ ┣ 📂 router
┃ ┃ ┃ ┗ 📜 index.js
┃ ┃ ┣ 📂 stores
┃ ┃ ┃ ┣ 📜 ai.js
┃ ┃ ┃ ┣ 📜 course.js
┃ ┃ ┃ ┣ 📜 map.js
┃ ┃ ┃ ┣ 📜 review.js
┃ ┃ ┃ ┗ 📜 user.js
┃ ┃ ┣ 📂 utils
┃ ┃ ┃ ┗ 📜 timeUtils.js
┃ ┃ ┣ 📂 views
┃ ┃ ┃ ┣ 📂 Course
┃ ┃ ┃ ┃ ┣ 📜 CoureseListView.vue
┃ ┃ ┃ ┃ ┣ 📜 CourseDetailView.vue
┃ ┃ ┃ ┃ ┣ 📜 CreateCourseView.vue
┃ ┃ ┃ ┃ ┣ 📜 RankingListView.vue
┃ ┃ ┃ ┃ ┗ 📜 UpdateCourseView.vue
┃ ┃ ┃ ┣ 📂 MyPage
┃ ┃ ┃ ┃ ┣ 📜 EditProfileView.vue
┃ ┃ ┃ ┃ ┣ 📜 FollowView.vue
┃ ┃ ┃ ┃ ┣ 📜 MyCourseListView.vue
┃ ┃ ┃ ┃ ┗ 📜 MyInfoView.vue
┃ ┃ ┃ ┣ 📜 HomeView.vue
┃ ┃ ┃ ┣ 📜 LoginView.vue
┃ ┃ ┃ ┣ 📜 OnBoardingView.vue
┃ ┃ ┃ ┗ 📜 SignUpView.vue
┃ ┃ ┣ 📜 App.vue
┃ ┃ ┗ 📜 main.js
┃ ┣ 📜 .gitignore
┃ ┣ 📜 index.html
┃ ┣ 📜 jsconfig.json
┃ ┣ 📜 package-lock.json
┃ ┣ 📜 package.json
┃ ┣ 📜 README.md
┃ ┗ 📜 vite.config.js
```
</details>

---

## ⚙️ 실행 방법 (로컬)

### 1) 필수 요구사항
- Java
- MySQL
- Node.js (프론트 및 지도 렌더러 사용 시)

### 2) DB 준비
1. MySQL에 DB 생성
2. 테이블 생성 DDL 실행(프로젝트 SQL 파일 기준)

### 3) 백엔드 실행
1. `application.xml` 설정

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/danggeunko?serverTimezone=Asia/Seoul
    username: YOUR_USERNAME
    password: YOUR_PASSWORD
