package com.danggeunko.course.dto;

import java.time.LocalDateTime;
import java.util.List;

public class Course {

    // PK
    private Integer courseId;

    // 사용자 ID
    private Integer userId;

    // 좋아요 수
    private Integer likeCount;

    // 기본 정보
    private String courseName;
    private Integer courseRegion;
    private String startAddress;
    private String endAddress;
    private List<CoursePoint> coursePoints;

    // 러닝 정보
    private double distanceKm;
    private double durationMin;
    private double paceMin;

    // 코스 속성
    private Integer courseType;
    private String difficulty;
    private String description;

    // 편의시설
    private Boolean hasCrosswalk;
    private Boolean hasToilet;

    // 평점
    private double avgRatings;
    
    // 조회수
    private int viewCnt;

    // 시스템 시간
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    /* ===============================
       생성자
    =============================== */

    public Course() {
    }

    public Course(Integer courseId, Integer userId, Integer likeCount, String courseName,
                     Integer courseRegion, String startAddress, String endAddress,
                     double distanceKm, double durationMin, double paceMin,
                     Integer courseType, String difficulty, String description,
                     Boolean hasCrosswalk, Boolean hasToilet, double avgRatings, int viewCnt, 
                     LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.courseId = courseId;
        this.userId = userId;
        this.likeCount = likeCount;
        this.courseName = courseName;
        this.courseRegion = courseRegion;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.durationMin = durationMin;
        this.paceMin = paceMin;
        this.courseType = courseType;
        this.difficulty = difficulty;
        this.description = description;
        this.hasCrosswalk = hasCrosswalk;
        this.hasToilet = hasToilet;
        this.avgRatings = avgRatings;
        this.viewCnt = viewCnt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    /* ===============================
       Getter / Setter
    =============================== */

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseRegion() {
        return courseRegion;
    }

    public void setCourseRegion(Integer courseRegion) {
        this.courseRegion = courseRegion;
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

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public double getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(double durationMin) {
        this.durationMin = durationMin;
    }

    public double getPaceMin() {
        return paceMin;
    }

    public void setPaceMin(double paceMin) {
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

    public double getAvgRatings() {
        return avgRatings;
    }

    public void setAvgRatings(double avgRatings) {
        this.avgRatings = avgRatings;
    }
    
    public int getViewCnt() {
		return viewCnt;
	}

	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}

	public List<CoursePoint> getCoursePoints() {
		return coursePoints;
	}

	public void setCoursePoints(List<CoursePoint> coursePoints) {
		this.coursePoints = coursePoints;
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
		return "CourseDto [courseId=" + courseId + ", userId=" + userId + ", like=" + likeCount + ", courseName="
				+ courseName + ", courseRegion=" + courseRegion + ", startAddress=" + startAddress + ", endAddress="
				+ endAddress + ", distanceKm=" + distanceKm + ", durationMin=" + durationMin + ", paceMin=" + paceMin
				+ ", courseType=" + courseType + ", difficulty=" + difficulty + ", description=" + description
				+ ", hasCrosswalk=" + hasCrosswalk + ", hasToilet=" + hasToilet + ", avgRatings=" + avgRatings
				+ ", viewCnt=" + viewCnt + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
}

