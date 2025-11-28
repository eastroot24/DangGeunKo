package com.danggeunko.user.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.danggeunko.course.dto.Course;

public class User {

    // 사용자 기본 정보 (User 테이블 기반)
    private int userId; // 고유유저아이디 (user_id)
    private String userName; // 이름 (user_name)
    private String nickname; // 닉네임 (nickname)
    private String userEmail; // 이메일 (user_email)
    private String userPassword; // 비밀번호 (user_password)
    private String gender; // 성별 (gender)
    private int age; // 나이 (age)
    private int region; // 동네정보 (region)
    private LocalDateTime createdAt; // 가입일자 (created_at)
    private LocalDateTime updatedAt; // 수정일자 (updated_at) 

    // 선호 정보 (User 테이블 기반)
    private double prefDistance; // 선호 거리(km) (pref_distance)
    private String prefDifficulty; // 선호 난이도 (pref_difficulty)

    // Like/찜하기
    // 코스 찜 목록
    private List<Course> likeList; 
  
    // --- Constructor, Getters, and Setters below ---
    
    // 기본 생성자
    public User() {
    }

    // 모든 필드를 포함하는 생성자 (비밀번호는 제외할 수 있음)
    public User(int userId, String userName, String nickname, String userEmail, String userPassword, String gender, int age, int region, LocalDateTime createdAt, float prefDistance, String prefDifficulty, LocalDateTime updatedAt) {
        this.userId = userId;
        this.userName = userName;
        this.nickname = nickname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.gender = gender;
        this.age = age;
        this.region = region;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.prefDistance = prefDistance;
        this.prefDifficulty = prefDifficulty;
        this.likeList = null;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUserName() {
        return userName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public int getRegion() {
        return region;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrefDistance() {
        return prefDistance;
    }

    public String getPrefDifficulty() {
        return prefDifficulty;
    }

    public List<Course> getLikeCount() {
        return likeList;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPrefDistance(float prefDistance) {
        this.prefDistance = prefDistance;
    }

    public void setPrefDifficulty(String prefDifficulty) {
        this.prefDifficulty = prefDifficulty;
    }

    public void setLikeCount(List<Course> likeList) {
        this.likeList = likeList;
    }

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", nickname=" + nickname + ", userEmail="
				+ userEmail + ", userPassword=" + userPassword + ", gender=" + gender + ", age=" + age + ", region="
				+ region + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", prefDistance=" + prefDistance
				+ ", prefDifficulty=" + prefDifficulty + ", likeList=" + likeList + "]";
	}
    
}
