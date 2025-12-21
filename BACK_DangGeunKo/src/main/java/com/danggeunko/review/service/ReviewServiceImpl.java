package com.danggeunko.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danggeunko.course.dao.CourseDao;
import com.danggeunko.review.dao.ReviewDao;
import com.danggeunko.review.dto.Review;
import com.danggeunko.review.dto.ReviewSearchCondition;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	private final ReviewDao reviewDao;
	private final CourseDao courseDao;
	
	public ReviewServiceImpl(ReviewDao reviewDao, CourseDao courseDao) {
		this.reviewDao = reviewDao;
		this.courseDao = courseDao;
	}
	private void updateCourseRating(int courseId) {
        double avgRating = reviewDao.selectAverageRating(courseId);
        courseDao.updateCourseAvgRating(avgRating, courseId);
    }

	@Override
	@Transactional
	public boolean addReview(Review review) {
		boolean completed = reviewDao.insertReview(review) > 0;
		updateCourseRating(review.getCourseId());
		return completed;
	}


	@Override
	@Transactional
	public List<Review> getReviewsByCourse(ReviewSearchCondition reviewSearchCondition) {
		return reviewDao.getReviewsByCourse(reviewSearchCondition);
	}


	@Override
	@Transactional
	public boolean updateReview(int reviewId, Review review) {
		boolean completed = reviewDao.updateReview(reviewId, review) > 0;
		updateCourseRating(review.getCourseId());
		return completed;
	}


	@Override
	@Transactional
	public boolean deleteReivew(int reviewId) {
		Review review = reviewDao.getReviewById(reviewId);
		if (review != null) {
		boolean completed = reviewDao.deleteReview(reviewId) > 0;
		updateCourseRating(review.getCourseId());
		return completed;
		}
		return false;
		
	}


}
