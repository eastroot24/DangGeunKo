-- 문자셋 설정 (원하면 빼도 됨)
DROP DATABASE IF EXISTS danggeunko;
CREATE DATABASE IF NOT EXISTS danggeunko
  DEFAULT CHARACTER SET utf8mb4 -- mac -> window 인코딩 생길까봐 설정
  DEFAULT COLLATE utf8mb4_general_ci; --  대소문자 구분없이 정렬해줄려고 쓰는건데 없어도 괜춘!!!!!! 

USE danggeunko;
-- 1. 사용자
drop table IF exists `USER`;
CREATE TABLE `USER` (
    user_id        INT AUTO_INCREMENT PRIMARY KEY,          -- 유저 pk
    profile_img VARCHAR(255) DEFAULT NULL,				-- 프로필 사진 파일명
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
drop table IF exists `FOLLOW`;
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
drop table IF exists `COURSE`;
CREATE TABLE `COURSE` (
    course_id     INT AUTO_INCREMENT PRIMARY KEY,           -- 코스 pk
    user_id       INT NOT NULL,                             -- 유조 pk
    course_name   VARCHAR(50) NOT NULL,                     -- 코스 이름
    course_city   varchar(10),                              -- 코스 도시
    course_district   varchar(10),                              -- 코스 도시
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
drop table IF exists `COURSE_POINT`;
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
drop table IF exists `COURSE_LIKE`;
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
drop table IF exists `REVIEW`;
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

INSERT INTO `COURSE` (
    user_id, course_name, course_city, course_district, start_address, end_address, 
    distance_km, duration_min, pace_min, course_type, difficulty, description, 
    has_crosswalk, has_toilet, avg_rating, view_cnt
) VALUES
-- 1-10: 서울, 부산 지역 데이터
(1, '여의도 샛강 생태공원 코스', '서울특별시', '영등포구', '국회의사당역', '샛강생태공원', 
 7.5, 45.0, 6.0, 1, '초급', '평탄하고 뷰가 좋은 강변 코스입니다.', TRUE, TRUE, 4.5, 320),
(2, '남산타워 힐 트레이닝', '서울특별시', '중구', '남산 순환버스 정류장', '남산 팔각정', 
 8.0, 60.0, 7.5, 2, '고급', '경사가 있어 훈련에 좋은 코스, 야경이 매우 아름다움', FALSE, TRUE, 4.8, 510),
(3, '올림픽공원 호수 순환', '서울특별시', '송파구', '평화의 문', '몽촌토성역', 
 5.2, 30.0, 5.7, 1, '초급', '가볍게 뛰기 좋은 공원 내부 코스, 벚꽃 명소', TRUE, TRUE, 4.2, 150),
(4, '북악스카이 힐 코스', '서울특별시', '종로구', '팔각정 입구', '북악산 매표소', 
 12.0, 90.0, 7.5, 2, '고급', '고강도 훈련을 위한 최적의 힐 코스', FALSE, FALSE, 4.9, 600),
(5, '광안리 해변 런', '부산광역시', '수영구', '광안리 해변 입구', '민락수변공원', 
 6.8, 40.0, 5.9, 1, '초급', '부산의 대표적인 해변 러닝 코스', FALSE, TRUE, 4.7, 720),
(6, '해운대 달맞이 길', '부산광역시', '해운대구', '해월정 입구', '달맞이 고개 정상', 
 4.5, 45.0, 10.0, 2, '고급', '짧지만 경사가 급격한 힐 트레이닝', FALSE, FALSE, 4.6, 380),
(7, '온천천 시민공원 평지', '부산광역시', '동래구', '온천천 공원 입구', '세병교', 
 11.0, 70.0, 6.3, 1, '중급', '긴 거리를 안정적으로 뛸 수 있는 평지 코스', TRUE, TRUE, 4.4, 300),
(8, '대구 신천 둔치 장거리', '대구광역시', '수성구', '신천시장', '동신교', 
 9.0, 55.0, 6.1, 1, '중급', '대구 도심을 가로지르는 긴 강변 코스', TRUE, TRUE, 4.4, 270),
(9, '송도 센트럴파크 야간 런', '인천광역시', '연수구', '센트럴파크 정문', '보트하우스', 
 4.2, 25.0, 6.0, 1, '초급', '도심 속 아름다운 공원 러닝, 야경이 멋짐', TRUE, TRUE, 4.5, 250),
(10, '강화도 해안 드라이브 코스', '인천광역시', '강화군', '동막 해수욕장', '분오리돈대', 
 15.0, 100.0, 6.7, 3, '고급', '장거리 지구력 훈련에 적합한 해안 코스', FALSE, FALSE, 4.0, 180),

-- 11-20: 경기도 지역 데이터
(1, '일산 호수공원 트랙', '경기도', '고양시 일산동구', '호수공원 정문', '노래하는 분수대', 
 3.5, 20.0, 5.7, 1, '초급', '짧고 안전하며 접근성이 좋은 코스', TRUE, TRUE, 4.1, 120),
(2, '분당 탄천 합수부', '경기도', '성남시 분당구', '탄천종합운동장', '정자역 부근', 
 8.5, 50.0, 5.9, 1, '중급', '빠른 페이스를 유지하기 좋은 평탄한 코스', FALSE, TRUE, 4.3, 350),
(3, '수원 화성 성곽길', '경기도', '수원시 팔달구', '화서문', '장안문', 
 5.8, 40.0, 6.9, 2, '중급', '역사적인 장소를 따라 뛰는 코스 (계단 주의)', TRUE, TRUE, 4.7, 420),
(4, '용인 에버랜드 주변 임도', '경기도', '용인시 처인구', '마성 IC 부근', '에버랜드 주차장', 
 13.0, 95.0, 7.3, 2, '고급', '비포장도로가 일부 포함된 트레일 코스', FALSE, FALSE, 4.4, 230),
(5, '과천 서울대공원 외곽', '경기도', '과천시', '대공원역 2번 출구', '서울랜드 정문', 
 7.0, 48.0, 6.8, 2, '중급', '완만한 경사가 있는 쾌적한 환경의 코스', TRUE, TRUE, 4.2, 290),
(6, '구리 한강 시민공원', '경기도', '구리시', '구리 타워', '토평교', 
 10.0, 62.0, 6.2, 1, '중급', '한강변을 따라 길게 뻗은 평탄 코스', FALSE, TRUE, 4.1, 170),
(7, '김포 아라마루 전망대 순환', '경기도', '김포시', '아라뱃길 김포여객터미널', '아라마루 전망대', 
 6.0, 40.0, 6.7, 2, '중급', '바람이 시원하며 시야가 트인 코스', FALSE, FALSE, 4.5, 210),
(8, '안양천 합수부', '경기도', '안양시 만안구', '안양대교', '안양천 합수부', 
 9.2, 55.0, 6.0, 1, '중급', '자전거 도로와 분리되어 안전한 하천변 코스', TRUE, TRUE, 4.3, 330),
(9, '파주 헤이리 예술마을 외곽', '경기도', '파주시', '헤이리 게이트 1', '프로방스 마을', 
 5.0, 30.0, 6.0, 3, '초급', '예쁜 마을 주변을 도는 코스', TRUE, FALSE, 3.9, 140),
(10, '평택호 관광단지 트랙', '경기도', '평택시', '평택호 관광지 주차장', '평택호 전망대', 
 7.2, 45.0, 6.2, 1, '중급', '호수를 바라보며 뛰는 쾌적한 코스', FALSE, TRUE, 4.0, 200),

-- 21-30: 강원도, 충청도, 전라도, 경상도, 제주도 지역 데이터
(1, '강릉 경포호 벚꽃 런', '강원도', '강릉시', '경포호 주차장', '경포대', 
 4.7, 28.0, 6.0, 1, '초급', '봄에 특히 아름다운 호수 순환 코스', TRUE, TRUE, 4.6, 480),
(2, '춘천 의암호 자전거길', '강원도', '춘천시', '공지천 조각공원', '의암호 스카이워크', 
 10.5, 75.0, 7.1, 2, '고급', '완만한 오르막이 있는 긴 거리 코스', FALSE, FALSE, 4.4, 260),
(3, '천안 독립기념관 주변', '충청남도', '천안시 동남구', '독립기념관 정문', '겨레의 탑', 
 8.8, 60.0, 6.8, 2, '중급', '역사적 의미가 있는 넓은 공원 주변 코스', TRUE, TRUE, 4.2, 220),
(4, '청주 무심천변 런', '충청북도', '청주시 상당구', '무심천 롤러스케이트장', '청남교', 
 7.0, 42.0, 6.0, 1, '중급', '청주시를 가로지르는 하천변 평탄 코스', TRUE, TRUE, 4.3, 190),
(5, '전주 한옥마을 외곽', '전라북도', '전주시 완산구', '경기전 앞', '남천교', 
 3.8, 22.0, 5.8, 3, '초급', '도심 속에서 가볍게 뛰는 짧은 코스', TRUE, TRUE, 3.9, 140),
(6, '광양 백운산 등산로', '전남', '광양시', '마이산 등산로 입구', '봉우재', 
 18.0, 130.0, 7.2, 2, '고급', '장거리 힐 트레일 (난이도 높음)', FALSE, FALSE, 4.7, 310),
(7, '경주 보문호 순환 코스', '경북', '경주시', '보문단지 입구', '보문호 선착장', 
 7.8, 48.0, 6.1, 1, '초급', '관광객이 많은 평탄한 호수 주변 코스', TRUE, TRUE, 4.5, 390),
(8, '포항 영일대 해상누각 런', '경북', '포항시 남구', '영일대 해수욕장', '포항제철소 야경', 
 5.0, 30.0, 6.0, 1, '초급', '바다를 따라 뛰는 상쾌한 코스', TRUE, TRUE, 4.1, 100),
(9, '창원 주남저수지 트레일', '경남', '창원시 의창구', '주남저수지 탐방로', '주남저수지 관리소', 
 6.0, 42.0, 7.0, 2, '중급', '자연 친화적이지만 길이 고르지 않은 코스', FALSE, FALSE, 4.3, 160),
(10, '제주 해안도로 동부', '제주특별자치도', '제주시', '함덕 해수욕장', '김녕 해수욕장', 
 20.0, 140.0, 7.0, 3, '고급', '아름다운 풍경, 장거리 훈련 코스', FALSE, FALSE, 4.8, 650);

INSERT INTO COURSE_POINT (course_id, sequence, latitude, longitude)
VALUES
-- 1-10: 서울, 부산, 대구, 인천
(1, 1, 37.5318, 126.9142), -- 서울 영등포구 (국회의사당역)
(2, 1, 37.5512, 126.9882), -- 서울 중구 (남산 팔각정)
(3, 1, 37.5208, 127.1214), -- 서울 송파구 (올림픽공원 평화의문)
(4, 1, 37.5944, 126.9825), -- 서울 종로구 (북악산 팔각정)
(5, 1, 35.1531, 129.1185), -- 부산 수영구 (광안리 해변)
(6, 1, 35.1588, 129.1755), -- 부산 해운대구 (달맞이길)
(7, 1, 35.1965, 129.0805), -- 부산 동래구 (온천천 시민공원)
(8, 1, 35.8606, 128.6231), -- 대구 수성구 (신천 동신교)
(9, 1, 37.3923, 126.6384), -- 인천 연수구 (송도 센트럴파크)
(10, 1, 37.6068, 126.4862), -- 인천 강화군 (동막 해수욕장)

-- 11-20: 경기도 지역
(11, 1, 37.6582, 126.7635), -- 경기 고양시 (일산 호수공원)
(12, 1, 37.3821, 127.1184), -- 경기 성남시 (분당 탄천)
(13, 1, 37.2844, 127.0123), -- 경기 수원시 (화성 장안문)
(14, 1, 37.2911, 127.2023), -- 경기 용인시 (에버랜드 부근)
(15, 1, 37.4273, 127.0171), -- 경기 과천시 (서울대공원)
(16, 1, 37.5947, 127.1512), -- 경기 구리시 (한강 시민공원)
(17, 1, 37.5992, 126.6961), -- 경기 김포시 (아라뱃길)
(18, 1, 37.3916, 126.9234), -- 경기 안양시 (안양천 합수부)
(19, 1, 37.7712, 126.6961), -- 경기 파주시 (헤이리 마을)
(20, 1, 36.9102, 126.9384), -- 경기 평택시 (평택호 관광단지)

-- 21-30: 전국 주요 거점
(21, 1, 37.7951, 128.8966), -- 강원 강릉시 (경포호)
(22, 1, 37.8741, 127.7082), -- 강원 춘천시 (의암호)
(23, 1, 36.7831, 127.2231), -- 충남 천안시 (독립기념관)
(24, 1, 36.6291, 127.4812), -- 충북 청주시 (무심천)
(25, 1, 35.8141, 127.1523), -- 전북 전주시 (한옥마을)
(26, 1, 35.1061, 127.5841), -- 전남 광양시 (백운산)
(27, 1, 35.8441, 129.2712), -- 경북 경주시 (보문호)
(28, 1, 36.0461, 129.3782), -- 경북 포항시 (영일대)
(29, 1, 35.3211, 128.6923), -- 경남 창원시 (주남저수지)
(30, 1, 33.5431, 126.6682); -- 제주 제주시 (함덕 해수욕장)

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

select * 
FROM USER;

select * 
from course;

select *
from course_like;
