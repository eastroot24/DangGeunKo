package com.danggeunko.review.dao;

import java.util.List;

import com.danggeunko.review.dto.Review;
import com.danggeunko.review.dto.ReviewSearchCondition;

public interface ReviewDao {
	// 리뷰 추가
	public int insertReview(Review review);

	//리뷰 아이디로 조회
	public Review getReviewById(int reviewId);
	
	// 특정 코스에 대한 리뷰 조회
	public List<Review> getReviewsByCourse(ReviewSearchCondition reviewSearchCondition);

	// 리뷰 수정
	public int updateReview(int reviewId, Review review);

	// 리뷰 삭제
	public int deleteReview(int id);
	
	//평균 별점 불러오기
	public double selectAverageRating(int courseId);
}
