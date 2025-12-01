CREATE DATABASE if not EXISTS danggeunco;
USE danggeunco;

-- =============================
-- 0. 기존 테이블이 있다면 삭제
-- =============================
DROP TABLE IF EXISTS course_point;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS follow;
DROP TABLE IF EXISTS user;

-- =============================
-- 1. 사용자(User)
-- =============================
CREATE TABLE user (
    user_id        INT PRIMARY KEY AUTO_INCREMENT,   -- PK
    user_name      VARCHAR(30)  NOT NULL,            -- 이름
    nickname       VARCHAR(50)  NOT NULL,            -- 닉네임
    user_email     VARCHAR(50)  NOT NULL UNIQUE,     -- 로그인 이메일
    user_password  VARCHAR(50)  NOT NULL,            -- 비밀번호
    gender         VARCHAR(10),                      -- 성별
    age            INT,                              -- 나이
    region         INT,                              -- 동네 정보(코드)
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP, 
    pref_distance  FLOAT,                            -- 선호 거리(km)
    pref_difficulty VARCHAR(10),                     -- 초급/중급/고급
    updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP
                  ON UPDATE CURRENT_TIMESTAMP        -- 회원정보 수정일시
);

-- =============================
-- 2. 팔로우(Follow)
-- =============================
CREATE TABLE follow (
    following_id INT NOT NULL,                       -- 팔로잉 하는 사람
    follower_id  INT NOT NULL,                       -- 팔로워(나)
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP, -- 팔로우 시작 시간
    PRIMARY KEY (following_id, follower_id),
    FOREIGN KEY (following_id) REFERENCES user(user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (follower_id)  REFERENCES user(user_id)
        ON DELETE CASCADE
);

-- =============================
-- 3. 러닝코스(Course)
-- =============================
CREATE TABLE course (
    course_id     INT PRIMARY KEY AUTO_INCREMENT,    -- 코스 식별번호
    user_id       INT NOT NULL,                      -- 작성자(사용자 PK)
    like_count    INT DEFAULT 0,                     -- 코스 찜한 사용자 수
    course_name   VARCHAR(50) NOT NULL,              -- 코스 이름
    course_region INT,                               -- 코스 지역(구 코드 등)
    start_address VARCHAR(200),                      -- 출발지 주소
    end_address   VARCHAR(200),                      -- 도착지 주소
    distance_km   FLOAT,                             -- 전체 거리
    duration_min  FLOAT,                             -- 소요 시간(분)
    pace_min      FLOAT,                             -- 페이스
    course_type   INT,                               -- 코스 유형(코드)
    difficulty    VARCHAR(30),                       -- 코스 난이도
    description   TEXT,                              -- 코스 설명
    has_crosswalk BOOLEAN DEFAULT FALSE,             -- 횡단보도 유무
    has_toilet    BOOLEAN DEFAULT FALSE,             -- 공중화장실 유무
    avg_rating    FLOAT DEFAULT 0,                   -- 평균 별점
    view_cnt      INT   DEFAULT 0,                   -- 조회수
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP, 
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP
                 ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
        ON DELETE CASCADE
);

-- =============================
-- 4. 경로 상세 좌표(Course_point)
-- =============================
CREATE TABLE course_point (
    point_id  INT PRIMARY KEY AUTO_INCREMENT,        -- 좌표 식별번호
    course_id INT NOT NULL,                          -- 코스 FK
    sequence  INT NOT NULL,                          -- 경로 순서
    latitude  FLOAT NOT NULL,                        -- 위도
    longitude FLOAT NOT NULL,                        -- 경도
    FOREIGN KEY (course_id) REFERENCES course(course_id)
        ON DELETE CASCADE
);

-- =============================
-- 5. 코스 리뷰(Review)
-- =============================
CREATE TABLE review (
    review_id   INT PRIMARY KEY AUTO_INCREMENT,      -- 리뷰 식별번호
    course_id   INT NOT NULL,                        -- 코스 식별번호
    content     TEXT,                                -- 리뷰 내용
    rating      INT,                                 -- 코스에 대한 별점
    user_id     INT NOT NULL,                        -- 작성자(사용자 PK)
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP
                ON UPDATE CURRENT_TIMESTAMP,         -- 리뷰 수정 일시
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 리뷰 작성 일시
    FOREIGN KEY (course_id) REFERENCES course(course_id)
        ON DELETE CASCADE,
    FOREIGN KEY (user_id)   REFERENCES user(user_id)
        ON DELETE CASCADE
);

INSERT INTO user (
user_id, user_name, nickname, user_email, user_password,
gender, age, region, pref_distance, pref_difficulty,
created_at, updated_at
) VALUES
(1, '김민수', 'MoonRunner', 'moon@carrot.co', 'pass1234', '남', 29, 1102, 5.0, '중급', NOW(), NOW()),
(2, '이서연', 'SkyBlue', 'sky@carrot.co', 'pass1234', '여', 26, 1102, 4.0, '초급', NOW(), NOW()),
(3, '박현우', 'HarborWalker', 'harbor@carrot.co', 'pass1234', '남', 34, 1102, 6.0, '중급', NOW(), NOW()),
(4, '정지훈', 'Jinjin', 'jin@carrot.co', 'pass1234', '남', 31, 1102, 5.5, '중급', NOW(), NOW()),
(5, '최유나', 'HarborNova', 'nova@carrot.co', 'pass1234', '여', 28, 1102, 4.5, '초급', NOW(), NOW()),
(6, '한지민', 'BlueRiver', 'blue@carrot.co', 'pass1234', '여', 27, 1102, 6.5, '중상', NOW(), NOW()),
(7, '오준석', 'RunnerB', 'runnerb@carrot.co', 'pass1234', '남', 36, 1102, 5.0, '중급', NOW(), NOW()),
(8, '서하늘', 'SunsetR', 'sunset@carrot.co', 'pass1234', '여', 22, 1102, 3.5, '초급', NOW(), NOW()),
(9, '강도윤', 'PixelRunner', 'pixel@carrot.co', 'pass1234', '남', 40, 1102, 7.0, '고급', NOW(), NOW()),
(10,'윤나라', 'NaraRun', 'nara@carrot.co', 'pass1234', '여', 25, 1102, 4.2, '초급', NOW(), NOW());

select * from user;

-- course 데이터
INSERT INTO course (
course_id, user_id, like_count, course_name, course_region,
start_address, end_address, distance_km, duration_min, pace_min,
course_type, difficulty, description, has_crosswalk, has_toilet,
avg_rating, view_cnt, created_at, updated_at
) VALUES
(101, 1, 128, 'Seocho Seorae Park Loop', 1102, '서울 서초구 반포대로 15', '서울 서초구 반포대로 16', 4.2, 26.5, 6.3, 1, '중상', 'Seorae Park 주변 산책로와 조깅로를 연결한 원형 코스. 평탄하고 경치가 좋으며 가벼운 언덕 구간이 포함된다.', TRUE, TRUE, 4.6, 1578, '2025-11-01 09:00:00', '2025-11-28 08:45:00'),
(102, 2, 210, 'Bangbae-dong Yangjaecheon Run', 1102, '서울 서초구 방배로 68', '서울 서초구 방배로 92', 3.8, 22.5, 5.93, 2, '초급', '양재천 산책로를 따라 이어지는 편안한 코스. 강변 풍경과 조용한 분위기가 특징.', TRUE, TRUE, 4.5, 980, '2025-11-02 10:15:00', '2025-11-28 09:50:00'),
(103, 3, 89, 'Seocho Cultural Park Circuit', 1102, '서울 서초구 남부순환로 1234', '서울 서초구 남부순환로 1235', 4.1, 25.0, 6.1, 1, '중', '남부문화예술회관 주변 조깅 루프. 아트와 공원이 어우러진 도심 핫스팟.', TRUE, TRUE, 4.4, 640, '2025-11-03 11:20:00', '2025-11-28 09:50:00'),
(104, 4, 320, 'Yangjaecheon Riverside Pace', 1102, '서울 서초구 양재대로 123', '서울 서초구 양재대로 200', 5.2, 31.0, 5.96, 2, '초급-중급', '양재천 경관과 도심 풍경이 어우러진 중간 강도 코스.', TRUE, TRUE, 4.7, 1320, '2025-11-04 12:10:00', '2025-11-28 10:10:00'),
(105, 5, 512, 'Seocho Hillside Run', 1102, '서울 서초구 남부순환로 200', '서울 서초구 남부순환로 230', 3.9, 23.5, 6.02, 1, '중상', '힐 구간이 섞인 도시 공원 루프. 해질녘 조망이 좋습니다.', TRUE, TRUE, 4.5, 780, '2025-11-05 09:40:00', '2025-11-28 11:05:00'),
(106, 6, 210, 'Banpo Hangang Waterline Run', 1102, '서울 서초구 반포대로 22', '서울 서초구 반포대로 76', 6.0, 36.0, 6.0, 3, '중상', '한강변 산책로를 따라 이어지는 긴 러닝 루프. 야경이 특히 아름다워요.', TRUE, TRUE, 4.6, 1500, '2025-11-06 21:15:00', '2025-11-28 12:15:00'),
(107, 7, 145, 'Seocho Village Green Circuit', 1102, '서울 서초구 서리풀길 12', '서울 서초구 서리풀길 12-2', 4.6, 28.0, 6.08, 1, '초급-중급', '지역 공원과 주거가 잘 어우러진 이색 루프. 가족 단위도 많이 찾습니다.', TRUE, TRUE, 4.3, 540, '2025-11-07 08:30:00', '2025-11-28 12:40:00'),
(108, 8, 98, 'Seorae Garden Path', 1102, '서울 서초구 서초대로 300', '서울 서초구 서초대로 320', 3.2, 19.5, 6.09, 2, '초급', '서초구 중심부의 짧고 느긋한 코스. 도심 속 작은 정원을 지나갑니다.', TRUE, TRUE, 4.2, 420, '2025-11-08 07:50:00', '2025-11-28 13:05:00'),
(109, 9, 260, 'Bogwang Mountain Base Loop', 1102, '서울 서초구 방배로 111', '서울 서초구 방배로 166', 5.0, 29.0, 5.8, 4, '중상-상급', '방배동의 언덕이 있는 코스로 언덕 훈련에 좋습니다.', TRUE, FALSE, 4.6, 860, '2025-11-09 06:40:00', '2025-11-28 13:40:00'),
(110, 10, 310, 'Seocho Riverside Sunrise', 1102, '서울 서초구 반포대로 50', '서울 서초구 반포대로 60', 4.0, 24.0, 6.0, 3, '초급', '일출을 바라보며 시작하는 해돋이 러닝 루트. 한강과 도심이 한눈에 보입니다.', TRUE, TRUE, 4.4, 700, '2025-11-10 05:55:00', '2025-11-28 14:20:00');

select * from course;


-- course_point 데이터 (모든 좌표)
INSERT INTO course_point (course_id, sequence, latitude, longitude) VALUES
-- 101
(101,1,37.4935,127.011),(101,2,37.4945,127.0125),(101,3,37.495,127.014),(101,4,37.4955,127.016),(101,5,37.4948,127.018),(101,6,37.4935,127.017),
-- 102
(102,1,37.493,127.014),(102,2,37.495,127.016),(102,3,37.4968,127.0185),(102,4,37.498,127.02),(102,5,37.5,127.019),
-- 103
(103,1,37.4953,127.0155),(103,2,37.496,127.0168),(103,3,37.4972,127.0182),(103,4,37.498,127.02),(103,5,37.497,127.0215),(103,6,37.4953,127.0155),
-- 104
(104,1,37.468,127.03),(104,2,37.47,127.0315),(104,3,37.4718,127.033),(104,4,37.4725,127.0345),(104,5,37.474,127.033),
-- 105
(105,1,37.4938,127.018),(105,2,37.4945,127.0192),(105,3,37.4957,127.0208),(105,4,37.4968,127.0225),(105,5,37.496,127.021),(105,6,37.4938,127.018),
-- 106
(106,1,37.506,127.009),(106,2,37.508,127.0105),(106,3,37.5095,127.012),(106,4,37.5107,127.0135),(106,5,37.511,127.014),
-- 107
(107,1,37.4905,127.014),(107,2,37.492,127.0155),(107,3,37.493,127.017),(107,4,37.492,127.0185),(107,5,37.4908,127.017),(107,6,37.4905,127.014),
-- 108
(108,1,37.495,127.015),(108,2,37.496,127.0165),(108,3,37.4968,127.0185),(108,4,37.4958,127.0195),(108,5,37.495,127.018),
-- 109
(109,1,37.481,127.023),(109,2,37.4825,127.025),(109,3,37.484,127.0265),(109,4,37.486,127.024),(109,5,37.4845,127.023),
-- 110
(110,1,37.502,127.006),(110,2,37.5035,127.0075),(110,3,37.505,127.009),(110,4,37.506,127.0105),(110,5,37.507,127.012);


-- 코스 리뷰(Review) 더미 데이터
INSERT INTO review (course_id, content, rating, user_id, created_at) VALUES
-- 코스 101 (Seocho Seorae Park Loop, 중상) - 5개 리뷰
(101, '경치가 너무 아름답고, 가벼운 언덕 덕분에 훈련하기 좋았습니다. 추천!', 5, 2, '2025-11-01 10:30:00'),
(101, '평탄한 길이라 초보자에게도 괜찮아요. 다음에도 또 방문하고 싶네요.', 4, 3, '2025-11-02 12:45:00'),
(101, '주말에 사람이 많아요. 평일에 뛰는 것을 추천합니다.', 3, 4, '2025-11-03 15:00:00'),
(101, '관리도 잘 되어 있고, 코스 길이도 적당해서 만족스러웠습니다.', 5, 5, '2025-11-04 18:20:00'),
(101, '언덕이 생각보다 힘들었지만, 달성감은 최고!', 4, 6, '2025-11-05 20:10:00'),

-- 코스 102 (Bangbae-dong Yangjaecheon Run, 초급) - 4개 리뷰
(102, '양재천 코스는 언제나 옳습니다. 조용하고 편안한 러닝에 최적!', 5, 7, '2025-11-06 09:15:00'),
(102, '강변 풍경이 좋았고, 특히 일몰 때 분위기가 환상적입니다.', 5, 8, '2025-11-07 14:30:00'),
(102, '초급 코스라고 해서 가봤는데, 정말 부담 없이 뛸 수 있었어요.', 4, 9, '2025-11-08 17:50:00'),
(102, '길이 잘 포장되어 있어 무릎에 무리가 가지 않았습니다.', 4, 10, '2025-11-09 11:25:00'),

-- 코스 103 (Seocho Cultural Park Circuit, 중) - 3개 리뷰
(103, '공원이 아름다워서 눈이 즐거운 코스입니다. 예술회관 구경은 덤!', 4, 1, '2025-11-10 13:40:00'),
(103, '중급이지만 크게 힘들지 않았고, 접근성이 좋습니다.', 4, 2, '2025-11-11 16:00:00'),
(103, '도심 속에서 자연을 느낄 수 있는 좋은 루트였어요.', 5, 3, '2025-11-12 19:15:00'),

-- 코스 104 (Yangjaecheon Riverside Pace, 초급-중급) - 2개 리뷰
(104, '길이가 좀 있지만 경치가 워낙 좋아서 지루하지 않습니다. 재방문 의사 100%.', 5, 4, '2025-11-13 10:55:00'),
(104, '페이스 훈련하기 딱 좋은 길이와 평지입니다.', 4, 5, '2025-11-14 14:10:00'),

-- 코스 105 (Seocho Hillside Run, 중상) - 2개 리뷰
(105, '역시 힐사이드! 언덕이 많아서 제대로 훈련했습니다.', 4, 6, '2025-11-15 17:30:00'),
(105, '해질 녘 조망이 정말 멋있었어요. 다만 초보자는 조금 힘들 수 있습니다.', 4, 7, '2025-11-16 21:00:00'),

-- 코스 106 (Banpo Hangang Waterline Run, 중상) - 2개 리뷰
(106, '한강변 야경을 즐기며 뛰는 최고의 코스! 데이트 러닝으로도 추천합니다.', 5, 8, '2025-11-17 22:45:00'),
(106, '코스 길이가 넉넉해서 장거리 훈련에 좋습니다.', 5, 9, '2025-11-18 18:30:00'),

-- 코스 107 (Seocho Village Green Circuit, 초급-중급) - 1개 리뷰
(107, '가족들과 함께 가볍게 산책하기 좋은 코스입니다. 러닝용으로도 굿!', 4, 10, '2025-11-19 15:50:00'),

-- 코스 108 (Seorae Garden Path, 초급) - 1개 리뷰
(108, '도심 속 작은 힐링 공간. 짧아서 좋습니다.', 4, 1, '2025-11-20 08:20:00'),

-- 코스 109 (Bogwang Mountain Base Loop, 중상-상급) - 1개 리뷰
(109, '언덕 훈련의 성지! 중상급자에게는 도전해 볼 만한 가치가 있습니다.', 5, 2, '2025-11-21 07:00:00'),

-- 코스 110 (Seocho Riverside Sunrise, 초급) - 1개 리뷰
(110, '일출 러닝 정말 최고였어요. 상쾌하게 하루를 시작했습니다.', 5, 3, '2025-11-22 06:15:00'),

-- 추가 리뷰 (다양성 확보)
(101, '언덕이 좀 있어서 페이스 조절이 중요해요. 경치 덕분에 용서됩니다.', 4, 7, '2025-11-23 11:30:00'),
(102, '바람이 시원해서 여름에도 좋았습니다.', 5, 1, '2025-11-24 13:50:00'),
(103, '생각보다 뷰가 좋진 않았습니다.', 3, 5, '2025-11-25 17:10:00'),
(104, '너무 좋아요!', 5, 6, '2025-11-26 19:40:00'),
(106, '화장실이 잘 되어 있어서 좋았습니다.', 5, 10, '2025-11-27 23:00:00');
        
select *
FROM review;

