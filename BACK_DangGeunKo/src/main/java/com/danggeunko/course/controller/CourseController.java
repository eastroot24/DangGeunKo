package com.danggeunko.course.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.danggeunko.course.dao.CoursePointDao;
import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.MapPoint;
import com.danggeunko.course.dto.SearchCondition;
import com.danggeunko.course.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api-course")
@CrossOrigin("*")
public class CourseController {

	CourseService courseService;
	CoursePointDao coursePointDao;

	public CourseController(CourseService courseService, CoursePointDao coursePointDao) {
		this.courseService = courseService;
		this.coursePointDao = coursePointDao;
	}

	@Value("${naver.map.gateway-id}")
	private String naverMapKeyId;
	@Value("${naver.map.gateway-key}")
	private String naverMapKeySecret;

	// 코스 전체 조회
	@GetMapping("/course")
	public ResponseEntity<?> getCourseList(@RequestParam(required = false) Integer userId) {
		List<Course> list = courseService.getAllCourses(userId);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 코스 상세 조회
	@GetMapping("/course/{id}")
	public ResponseEntity<?> getCourseDetailById(@PathVariable("id") int id,
			@RequestParam(required = false) boolean visited, @RequestParam(required = false) Integer userId) {
		Course course = null;
		if (visited) {
			course = courseService.updateCourseDetail(id, userId);
		} else {
			course = courseService.getCourseById(id, userId);
		}
		if (course != null) {
			return ResponseEntity.status(HttpStatus.OK).body(course);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 코스 등록
	@PostMapping("/course")
	public ResponseEntity<?> registCourse(@RequestBody Course course) {
		int newCourseId = courseService.addCourseWithPoints(course);
		if (newCourseId > 0) {
			return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("courseId", newCourseId));
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 코스 수정
	@PutMapping("/course/{id}")
	public ResponseEntity<?> updateCourseById(@PathVariable("id") int id, @RequestBody Course course) {
		course.setCourseId(id);
		boolean completed = courseService.updateCourse(course);
		if (completed) {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 코스 삭제
	@DeleteMapping("/course/{id}")
	public ResponseEntity<?> deleteCourseById(@PathVariable("id") int id) {
		boolean completed = courseService.deleteCourse(id);
		if (completed) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 코스 검색
	@GetMapping("/course/search")
	public ResponseEntity<?> searchCourseById(@ModelAttribute SearchCondition condition) {
		List<Course> list = courseService.search(condition);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 코스 정렬
	@GetMapping("/course/ranking")
	public ResponseEntity<?> getWeeklyRanking(@ModelAttribute SearchCondition condition) {
		List<Course> ranking = courseService.getWeeklyRanking(condition);
		if (ranking != null && !ranking.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(ranking);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	// 코스 좋아요
	@PostMapping("/course/{courseId}/like")
	public ResponseEntity<?> addLike(@PathVariable int courseId, @RequestParam int userId) {
		boolean liked = courseService.addLike(userId, courseId);
		return ResponseEntity.ok(Map.of("courseId", courseId, "userId", userId, "liked", liked));
	}

	// 지도 캡처 이미지 반환
	@GetMapping(value = "/course/{id}/thumbnail", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getCourseThumbnail(@PathVariable int id) throws Exception {

		List<MapPoint> points = courseService.getMapPoints(id);

		if (points.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		// Node 서버로 보낼 payload
		Map<String, Object> payload = Map.of("points", points, "width", 400, "height", 300);

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(payload);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:4001/render"))
				.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

		HttpClient client = HttpClient.newHttpClient();
		HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

		if (response.statusCode() != 200) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).header("Cache-Control", "no-store")
				.body(response.body());
	}

	// 내가 등록한 코스 조회
	@GetMapping("/course/regist")
	public ResponseEntity<?> getMyRegistCourse(@RequestParam int targetUserId, 
		    @RequestParam(required = false) Integer loginUserId){
	    Map<String, Object> params = new HashMap<>();
	    params.put("targetUserId", targetUserId);
	    params.put("loginUserId", loginUserId);
	    
	    return ResponseEntity.ok(courseService.getMyRegistCourse(params));
	}
	// 내가 찜한 코스 조회
	@GetMapping("/course/like")
	public ResponseEntity<?> getMyLikeCourse(@RequestParam int targetUserId, 
		    @RequestParam(required = false) Integer loginUserId) {
	    
	    Map<String, Object> params = new HashMap<>();
	    params.put("targetUserId", targetUserId);
	    params.put("loginUserId", loginUserId);
	    
	    return ResponseEntity.ok(courseService.getMyLikeCourse(params));
	}
}