package com.danggeunko.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danggeunko.review.dao.ReviewDao;
import com.danggeunko.review.dto.Review;
import com.danggeunko.review.dto.ReviewSearchCondition;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewDao reviewDao;
	
	public ReviewServiceImpl(ReviewDao reviewDao) {
		this.reviewDao = reviewDao;
	}


	@Override
	@Transactional
	public boolean addReview(Review review) {
		return reviewDao.insertReview(review) > 0;
	}


	@Override
	@Transactional
	public List<Review> getReviewsByCourse(ReviewSearchCondition reviewSearchCondition) {
		return reviewDao.getReviewsByCourse(reviewSearchCondition);
	}


	@Override
	@Transactional
	public boolean updateReview(int reviewId, Review review) {
		return reviewDao.updateReview(reviewId, review) > 0;
	}


	@Override
	@Transactional
	public boolean deleteReivew(int reviewId) {
		return reviewDao.deleteReview(reviewId) > 0;
	}


}
