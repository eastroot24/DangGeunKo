-- 문자셋 설정 (원하면 빼도 됨)
CREATE DATABASE IF NOT EXISTS danggeunko
  DEFAULT CHARACTER SET utf8mb4 -- mac -> window 인코딩 생길까봐 설정
  DEFAULT COLLATE utf8mb4_general_ci; --  대소문자 구분없이 정렬해줄려고 쓰는건데 없어도 괜춘!!!!!! 

USE danggeunko;

-- 1. 사용자
CREATE TABLE `USER` (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,          -- 유저 pk
    user_name      VARCHAR(30)  NOT NULL,                   -- 사용자 이름
    nickname       VARCHAR(50)  NOT NULL,                   -- 사용자 별명
    user_email     VARCHAR(50)  NOT NULL UNIQUE,            -- 로그인 이메일
    user_password  VARCHAR(50)  NOT NULL,                   -- 비밀번호
    gender         VARCHAR(10),                             -- 성별
    age            INT,                                     -- 나이
    region         VARCHAR(10),                             -- 거주 동네(코드) ? 이거  어떤지? varchar
    created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP,  -- 가입일자
    pref_distance  FLOAT,                                   -- 러닝 선호 거리(km)
    pref_difficulty VARCHAR(10),                            -- 초급/중급/고급
    update_at      DATETIME     DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP  -- 회원정보 수정일시
) ENGINE=InnoDB;

-- 2. 팔로우
CREATE TABLE `FOLLOW` (
    following_id INT NOT NULL,                              -- 팔로잉(내가 팔로우 하는 사람)
    follower_id  INT NOT NULL,                              -- 팔로워(나를 팔로우 하는 사람) // 이거 순서 중요 안함
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,        -- 팔로우 시작 시간 // 없어도 괜춘?
    PRIMARY KEY (following_id, follower_id),
    CONSTRAINT fk_follow_following
      FOREIGN KEY (following_id) REFERENCES `USER`(user_id)
      ON DELETE CASCADE,
    CONSTRAINT fk_follow_follower
      FOREIGN KEY (follower_id)  REFERENCES `USER`(user_id)
      ON DELETE CASCADE
) ENGINE=InnoDB;

-- 3. 러닝코스
CREATE TABLE `COURSE` (
    course_id     INT AUTO_INCREMENT PRIMARY KEY,           -- 코스 pk
    user_id       INT NOT NULL,                             -- 유조 pk
    course_name   VARCHAR(50) NOT NULL,                     -- 코스 이름
    course_region INT,                                      -- 코스 지역
    start_address VARCHAR(200),                             -- 출발지 주소
    end_address   VARCHAR(200),                             -- 도착지 주소
    distance_km   FLOAT,                                    -- 전체 거리
    duration_min  FLOAT,                                    -- 소요 시간
    pace_min      FLOAT,                                    -- 페이스
    course_type   INT,                                      -- 러닝 유형
    difficulty    VARCHAR(30),                              -- 코스 난이도
    description   TEXT,                                     -- 코스 설명
    has_crosswalk BOOLEAN,                                  -- 횡단보도 유무
    has_toilet    BOOLEAN,                                  -- 공중화장실 유무
    avg_rating    FLOAT,                                    -- 별점 평균
    view_cnt      INT DEFAULT 0,                            -- 조회수
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,       -- 등록 시각
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,-- 수정 시각
    CONSTRAINT fk_course_user
      FOREIGN KEY (user_id) REFERENCES `USER`(user_id)
      ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. 경로 상세 좌표
CREATE TABLE `COURSE_POINT` (
    point_id   INT AUTO_INCREMENT PRIMARY KEY,              -- 좌표 pk
    course_id  INT NOT NULL,                                -- 코스 pk
    sequence   INT NOT NULL,                                -- 경로 순서
    latitude   FLOAT NOT NULL,                              -- 위도
    longitude  FLOAT NOT NULL,                              -- 경도
    CONSTRAINT fk_point_course
      FOREIGN KEY (course_id) REFERENCES `COURSE`(course_id)
      ON DELETE CASCADE
) ENGINE=InnoDB;


-- 5. 코스 좋아요
CREATE TABLE `COURSE_LIKE` (
    user_id    INT NOT NULL,                               -- 좋아요 누른 유저 pk
    course_id  INT NOT NULL,                               -- 좋아요 대상 코스 pk
    PRIMARY KEY (user_id, course_id),                      -- 같은 유저가 같은 코스 두 번 못 누르게 // 백 처리하면 필요 없음
    CONSTRAINT fk_course_like_user
        FOREIGN KEY (user_id)   REFERENCES `USER`(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_course_like_course
        FOREIGN KEY (course_id) REFERENCES `COURSE`(course_id)
        ON DELETE CASCADE
) ENGINE=InnoDB;


-- 6. 코스 리뷰
CREATE TABLE `REVIEW` (
    review_id   INT AUTO_INCREMENT PRIMARY KEY,             -- 리뷰 pk
    course_id   INT NOT NULL,                               -- 코스 pk
    content     TEXT,                                       -- 리뷰 내용
    rating      INT,                                        -- 별점
    user_id     INT NOT NULL,                               -- 작성자 (유저 pk)
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP,  -- 리뷰 수정 일시
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,         -- 리뷰 작성 일시
    CONSTRAINT fk_review_course
      FOREIGN KEY (course_id) REFERENCES `COURSE`(course_id)
      ON DELETE CASCADE,
    CONSTRAINT fk_review_user
      FOREIGN KEY (user_id) REFERENCES `USER`(user_id)
      ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO USER (user_name, nickname, user_email, user_password, gender, age, region, pref_distance, pref_difficulty)
VALUES
('김철수', '철수런너', 'chulsoo@example.com', 'pass1234', 'M', 28, '해운대', 5.0, '초급'),
('이영희', '희희', 'younghee@example.com', 'pass1234', 'F', 26, '수영구', 10.0, '중급'),
('박민수', '민수러너', 'minsu@example.com', 'pass1234', 'M', 32, '부산진구', 7.0, '초급'),
('최지현', '지지', 'jihyun@example.com', 'pass1234', 'F', 30, '남구', 12.0, '고급'),
('정우성', '우성이형', 'woosung@example.com', 'pass1234', 'M', 35, '동래구', 8.0, '중급'),
('한가을', '가을담당', 'gaeul@example.com', 'pass1234', 'F', 24, '해운대', 3.0, '초급'),
('오준호', '준호달려', 'junho@example.com', 'pass1234', 'M', 29, '기장군', 15.0, '고급'),
('김보라', '보라빛밤', 'bora@example.com', 'pass1234', 'F', 27, '금정구', 6.0, '중급'),
('장동현', '동동', 'donghyun@example.com', 'pass1234', 'M', 31, '연제구', 9.0, '중급'),
('서지민', '지밍', 'jimin@example.com', 'pass1234', 'F', 25, '사하구', 4.0, '초급');

INSERT INTO FOLLOW (following_id, follower_id)
VALUES
(1, 2),
(2, 1),
(3, 1),
(4, 2),
(5, 1),
(6, 7),
(7, 3),
(8, 4),
(9, 5),
(10, 1);

INSERT INTO COURSE (user_id, course_name, course_region, start_address, end_address,
distance_km, duration_min, pace_min, course_type, difficulty, description,
has_crosswalk, has_toilet)
VALUES
(1, '광안리 해변 코스', 1, '광안리역 1번 출구', '광안대교 아래', 5.2, 35, 6.7, 1, '초급', '광안리 해변을 따라 뛰는 쉬운 코스', TRUE, TRUE),
(2, '해운대 달맞이 코스', 1, '해운대 해수욕장', '달맞이고개 전망대', 7.8, 50, 6.4, 2, '중급', '오르막이 있는 도전 코스', TRUE, TRUE),
(3, '부산 시민공원 코스', 2, '시민공원 정문', '시민공원 후문', 4.1, 28, 6.8, 1, '초급', '공원 내부 순환 코스', FALSE, TRUE),
(4, '이기대 해안산책로', 3, '오륙도 스카이워크', '이기대 입구', 10.3, 70, 6.8, 2, '고급', '절벽 해안 따라 걷는 러닝', TRUE, FALSE),
(5, '동래 온천천 코스', 4, '온천천역', '수안역 부근', 6.5, 42, 6.5, 1, '중급', '천을 따라 달리는 평지 코스', TRUE, TRUE),
(6, '기장 해안 코스', 5, '임랑해수욕장', '기장군청 근처', 12.0, 80, 6.6, 3, '고급', '바다 보면서 장거리 러닝', TRUE, FALSE),
(7, '금정산 초입 코스', 6, '범어사 입구', '금정산 초입', 3.3, 25, 7.5, 2, '초급', '산책로 기반의 짧은 코스', FALSE, FALSE),
(8, '사직동 둘레 코스', 7, '사직야구장', '사직운동장', 8.2, 55, 6.7, 1, '중급', '운동장 근처 도심 러닝', TRUE, TRUE),
(9, '송정 해수욕장 코스', 1, '송정 서핑구역', '구덕포', 6.1, 40, 6.5, 1, '초급', '해안 길 따라 달리기 좋음', TRUE, TRUE),
(10, '감천문화마을 코스', 8, '감천문화마을 입구', '전망대', 4.8, 38, 7.1, 2, '중급', '계단과 언덕이 많은 코스', TRUE, FALSE);

INSERT INTO COURSE_POINT (course_id, sequence, latitude, longitude)
VALUES
(1, 1, 35.1531, 129.1185),
(1, 2, 35.1540, 129.1200),
(2, 1, 35.1588, 129.1603),
(3, 1, 35.1661, 129.0550),
(4, 1, 35.1193, 129.1280),
(5, 1, 35.2057, 129.0725),
(6, 1, 35.2634, 129.2303),
(7, 1, 35.2490, 129.0600),
(8, 1, 35.1940, 129.0647),
(9, 1, 35.1791, 129.1984);

INSERT INTO COURSE_LIKE (user_id, course_id)
VALUES
(1, 2),
(2, 1),
(3, 4),
(4, 3),
(5, 1),
(6, 6),
(7, 8),
(8, 5),
(9, 2),
(10, 7);

INSERT INTO REVIEW (course_id, content, rating, user_id)
VALUES
(1, '초급자도 달리기 너무 좋아요!', 5, 1),
(2, '오르막이 좀 힘들었지만 뿌듯합니다.', 4, 2),
(3, '공원이라 안전하고 좋아요.', 5, 3),
(4, '난이도 높은데 풍경 최고!', 5, 4),
(5, '도심 러닝으로 딱이에요.', 4, 5),
(6, '장거리 코스로 추천!', 5, 6),
(7, '짧지만 상쾌한 코스였어요.', 4, 7),
(8, '사직동 러닝 조아요!', 4, 8),
(9, '바다 보고 달리는 게 힐링입니다.', 5, 9),
(10, '계단이 많지만 재미있었어요.', 4, 10);