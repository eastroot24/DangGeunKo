package com.danggeunko.course.dto;

import java.time.LocalDateTime;
import java.util.List;

public class Course {

    private Integer courseId;
    private int userId;

    private String courseName;
    private String courseCity;
    private String courseDistrict;
    private String startAddress;
    private String endAddress;

    private List<CoursePoint> coursePoints;

    private Double distanceKm;
    private Double durationMin;
    private Double paceMin;

    private Integer courseType;
    private String difficulty;
    private String description;

    private Boolean hasCrosswalk;
    private Boolean hasToilet;

    private Double avgRating;
    private Integer viewCnt;
    private Integer likeCnt;
    private Integer reviewCount;
    private boolean isLiked;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Course() {
    }

	public Course(Integer courseId, int userId, String courseName, String courseCity, String courseDistrict,
			String startAddress, String endAddress, List<CoursePoint> coursePoints, Double distanceKm,
			Double durationMin, Double paceMin, Integer courseType, String difficulty, String description,
			Boolean hasCrosswalk, Boolean hasToilet, Double avgRating, Integer viewCnt, Integer likeCnt,
			Integer reviewCount, boolean isLiked, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.courseId = courseId;
		this.userId = userId;
		this.courseName = courseName;
		this.courseCity = courseCity;
		this.courseDistrict = courseDistrict;
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.coursePoints = coursePoints;
		this.distanceKm = distanceKm;
		this.durationMin = durationMin;
		this.paceMin = paceMin;
		this.courseType = courseType;
		this.difficulty = difficulty;
		this.description = description;
		this.hasCrosswalk = hasCrosswalk;
		this.hasToilet = hasToilet;
		this.avgRating = avgRating;
		this.viewCnt = viewCnt;
		this.likeCnt = likeCnt;
		this.reviewCount = reviewCount;
		this.isLiked = isLiked;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseCity() {
		return courseCity;
	}

	public void setCourseCity(String courseCity) {
		this.courseCity = courseCity;
	}

	public String getCourseDistrict() {
		return courseDistrict;
	}

	public void setCourseDistrict(String courseDistrict) {
		this.courseDistrict = courseDistrict;
	}

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public List<CoursePoint> getCoursePoints() {
		return coursePoints;
	}

	public void setCoursePoints(List<CoursePoint> coursePoints) {
		this.coursePoints = coursePoints;
	}

	public Double getDistanceKm() {
		return distanceKm;
	}

	public void setDistanceKm(Double distanceKm) {
		this.distanceKm = distanceKm;
	}

	public Double getDurationMin() {
		return durationMin;
	}

	public void setDurationMin(Double durationMin) {
		this.durationMin = durationMin;
	}

	public Double getPaceMin() {
		return paceMin;
	}

	public void setPaceMin(Double paceMin) {
		this.paceMin = paceMin;
	}

	public Integer getCourseType() {
		return courseType;
	}

	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getHasCrosswalk() {
		return hasCrosswalk;
	}

	public void setHasCrosswalk(Boolean hasCrosswalk) {
		this.hasCrosswalk = hasCrosswalk;
	}

	public Boolean getHasToilet() {
		return hasToilet;
	}

	public void setHasToilet(Boolean hasToilet) {
		this.hasToilet = hasToilet;
	}

	public Double getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(Double avgRating) {
		this.avgRating = avgRating;
	}

	public Integer getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(Integer viewCnt) {
		this.viewCnt = viewCnt;
	}

	public Integer getLikeCnt() {
		return likeCnt;
	}

	public void setLikeCnt(Integer likeCnt) {
		this.likeCnt = likeCnt;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
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

	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", userId=" + userId + ", courseName=" + courseName + ", courseCity="
				+ courseCity + ", courseDistrict=" + courseDistrict + ", startAddress=" + startAddress + ", endAddress="
				+ endAddress + ", coursePoints=" + coursePoints + ", distanceKm=" + distanceKm + ", durationMin="
				+ durationMin + ", paceMin=" + paceMin + ", courseType=" + courseType + ", difficulty=" + difficulty
				+ ", description=" + description + ", hasCrosswalk=" + hasCrosswalk + ", hasToilet=" + hasToilet
				+ ", avgRating=" + avgRating + ", viewCnt=" + viewCnt + ", likeCnt=" + likeCnt + ", reviewCount="
				+ reviewCount + ", isLiked=" + isLiked + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

	
	

   
}