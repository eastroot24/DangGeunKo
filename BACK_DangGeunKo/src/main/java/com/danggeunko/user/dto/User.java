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
	private String userCity;
	private String userDistrict;
	private LocalDateTime createdAt; // 가입일자 (created_at)
	private LocalDateTime updatedAt; // 수정일자 (updated_at)
	private String profileImg; // 프로필 이미지파일명
	// 선호 정보 (User 테이블 기반)
	private double prefDistance; // 선호 거리(km) (pref_distance)
	private String prefDifficulty; // 선호 난이도 (pref_difficulty)

	// --- Constructor, Getters, and Setters below ---

	// 기본 생성자
	public User() {
	}

	public User(int userId, String userName, String nickname, String userEmail, String userPassword, String gender,
			int age, String userCity, String userDistrict, LocalDateTime createdAt, LocalDateTime updatedAt,
			String profileImg, double prefDistance, String prefDifficulty) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.nickname = nickname;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.gender = gender;
		this.age = age;
		this.userCity = userCity;
		this.userDistrict = userDistrict;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.profileImg = profileImg;
		this.prefDistance = prefDistance;
		this.prefDifficulty = prefDifficulty;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}

	public String getUserDistrict() {
		return userDistrict;
	}

	public void setUserDistrict(String userDistrict) {
		this.userDistrict = userDistrict;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public double getPrefDistance() {
		return prefDistance;
	}

	public void setPrefDistance(double prefDistance) {
		this.prefDistance = prefDistance;
	}

	public String getPrefDifficulty() {
		return prefDifficulty;
	}

	public void setPrefDifficulty(String prefDifficulty) {
		this.prefDifficulty = prefDifficulty;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", nickname=" + nickname + ", userEmail="
				+ userEmail + ", userPassword=" + userPassword + ", gender=" + gender + ", age=" + age + ", userCity="
				+ userCity + ", userDistrict=" + userDistrict + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", profileImg=" + profileImg + ", prefDistance=" + prefDistance + ", prefDifficulty=" + prefDifficulty
				+ "]";
	}

}
