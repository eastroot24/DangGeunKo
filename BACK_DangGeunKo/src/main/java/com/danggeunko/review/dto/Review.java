package com.danggeunko.review.dto;

import java.time.LocalDateTime;

public class Review {

    private Integer reviewId;   // 리뷰 식별번호
    private Integer courseId;   // 코스 식별번호

    private String content;     // 리뷰 내용
    private Integer rating;     // 코스에 대한 별점

    private Integer userId;     // 작성자(고유 식별 유저 아이디)

    private LocalDateTime updatedAt; // 리뷰 수정 일시
    private LocalDateTime createdAt; // 리뷰 작성 일시


    /* ===============================
       생성자
    =============================== */

    public Review() {
    }

    public Review(Integer reviewId,
                     Integer courseId,
                     String content,
                     Integer rating,
                     Integer userId,
                     LocalDateTime updatedAt,
                     LocalDateTime createdAt) {

        this.reviewId = reviewId;
        this.courseId = courseId;
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }


    /* ===============================
       Getter / Setter
    =============================== */

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

	@Override
	public String toString() {
		return "Review [reviewId=" + reviewId + ", courseId=" + courseId + ", content=" + content + ", rating=" + rating
				+ ", userId=" + userId + ", updatedAt=" + updatedAt + ", createdAt=" + createdAt + "]";
	}
    
}

