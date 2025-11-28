package com.danggeunko.review.service;

import java.util.List;

import com.danggeunko.review.dto.Review;
import com.danggeunko.review.dto.ReviewSearchCondition;

public interface ReviewService {
		//리뷰 등록
		public boolean addReview(Review review);
		//특정 코스에 대한 리뷰 전체 조회
		public List<Review> getReviewsByCourse(ReviewSearchCondition reviewSearchCondition);
		//리뷰 정보 수정
		public boolean updateReview(int reviewId, Review review);
		//리뷰 삭제
		public boolean deleteReivew(int reviewId);
}
