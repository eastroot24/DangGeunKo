package com.danggeunko.course.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.SearchCondition;
import com.danggeunko.course.service.CourseService;

@RestController
@RequestMapping("/api-course")
public class CourseController {
	
	CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}
	
	// 코스 전체 조회
	@GetMapping("/course")
	public ResponseEntity<?> list(){
		List<Course> list = courseService.getAllCourses();
		if(list == null || list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	// 코스 상세 조회
	@GetMapping("/course/{id}")
	public ResponseEntity<?> detail(@PathVariable("id") int id){
		Course course = courseService.getCourseById(id);
		if(course != null) {
			return ResponseEntity.status(HttpStatus.OK).body(course);
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	// 코스 등록
	@PostMapping("/course")
	public ResponseEntity<?> add(@RequestBody Course course){
		boolean completed = courseService.addCourseWithPoints(course);
		if(completed) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
	// 코스 수정
	@PutMapping("/course/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Course course){
		course.setCourseId(id);
		boolean completed = courseService.updateCourse(id, course);
		if(completed) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
	// 코스 삭제
	@DeleteMapping("/course/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id){
		boolean completed = courseService.deleteCourse(id);
		if(completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();			
		}
	}
	
	// 코스 검색
	@GetMapping("/course/search")
	public ResponseEntity<?> search(@RequestBody SearchCondition condition){
		List<Course> list = courseService.search(condition);
	    if(list == null || list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	// 코스 정렬
	@GetMapping("/course/ranking")
	public ResponseEntity<?> weeklyRanking(@RequestParam String region){
	    List<Course> ranking = courseService.getWeeklyRanking(region);
	    if(ranking == null || ranking.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(ranking);
		}else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
}
