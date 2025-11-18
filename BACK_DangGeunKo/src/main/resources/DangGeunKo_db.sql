CREATE DATABASE if not EXISTS danggeunco;
USE danggeunco;

CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    user_email VARCHAR(100) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    gender ENUM('M', 'F', 'OTHER') NULL,
    age INT NULL,
	preferred_region VARCHAR(100),        -- 선호 지역
    preferred_pace VARCHAR(50),           -- 선호 페이스
    preferred_difficulty ENUM('LOW','MID','HIGH'),
    preferred_distance VARCHAR(50),       -- 선호 거리
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE follow (
    follow_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_follow_follower
        FOREIGN KEY (follower_id) REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_follow_following
        FOREIGN KEY (following_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE course (
    course_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,                  -- 작성자
    title VARCHAR(100) NOT NULL,
    region VARCHAR(100),                      -- 지역
    distance DOUBLE,                          -- km
    duration INT,                             -- 분
    pace VARCHAR(20),
    difficulty ENUM('LOW', 'MID', 'HIGH'),
    course_type ENUM('ROUND', 'CIRCULAR', 'NORMAL'),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_course_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE course_point (
    point_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    seq INT NOT NULL,        -- 경로 순서
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,

    CONSTRAINT fk_coursepoint_course
        FOREIGN KEY (course_id) REFERENCES course(course_id)
        ON DELETE CASCADE
);

CREATE TABLE course_like (
    like_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_like_course
        FOREIGN KEY (course_id) REFERENCES course(course_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_like_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE,

    UNIQUE KEY unique_course_user_like (course_id, user_id)
);

CREATE TABLE review (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review_course
        FOREIGN KEY (course_id) REFERENCES course(course_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_review_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);
