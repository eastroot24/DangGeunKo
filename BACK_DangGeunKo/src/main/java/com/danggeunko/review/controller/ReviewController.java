package com.danggeunko.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danggeunko.review.dto.Review;
import com.danggeunko.review.dto.ReviewSearchCondition;
import com.danggeunko.review.service.ReviewService;

@RestController
@RequestMapping("api-review/review")
public class ReviewController {
	
	private final ReviewService reviewService;
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	//리뷰 등록
	@PostMapping
	public ResponseEntity<?> addReview(@RequestBody Review review){
		boolean completed = reviewService.addReview(review);
		if(completed) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
	//코스에 대한 리뷰 목록 조회
	@GetMapping
	public ResponseEntity<List<Review>> getReviewsByCourse(
	    @RequestParam("courseId") int courseId,
	    @ModelAttribute ReviewSearchCondition condition) {
	    
	    // DTO에 courseId를 설정합니다. (url parameter로 받았지만 DTO에 최종적으로 통합)
	    condition.setCourseId(courseId);
	    
	    // Service 계층에 SearchCondition 객체를 전달하여 동적 조회 요청
	    List<Review> reviews = reviewService.getReviewsByCourse(condition);

	    if (reviews != null && !reviews.isEmpty()) {
	        return ResponseEntity.ok(reviews);
	    } else {
	        // 리뷰가 없는 경우 204 No Content 또는 200 OK와 빈 리스트 반환
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	    }
	}
	
	//리뷰 수정
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody Review review){
		boolean completed = reviewService.updateReview(id, review);
		if(completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
	//리뷰 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
		boolean completed = reviewService.deleteReivew(id);
		if(completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
}
