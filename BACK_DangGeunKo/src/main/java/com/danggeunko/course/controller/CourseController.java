package com.danggeunko.course.controller;

import java.util.List;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.danggeunko.course.dto.MapPoint;

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
import com.danggeunko.course.dto.CoursePoint;
import com.danggeunko.course.dto.SearchCondition;
import com.danggeunko.course.service.CourseService;

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
	
	private static final String THUMBNAIL_DIR = "thumbnail";

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
	@GetMapping(
		    value = "/course/{id}/thumbnail",
		    produces = MediaType.IMAGE_PNG_VALUE
		)
		public ResponseEntity<byte[]> getCourseThumbnail(@PathVariable int id)
		    throws Exception {

		    // 1. 썸네일 파일 경로
		    Path dir = Paths.get(THUMBNAIL_DIR);
		    if (!Files.exists(dir)) {
		        Files.createDirectories(dir);
		    }

		    Path thumbnailPath = dir.resolve("course_" + id + ".png");

		    // 2. 이미 캐시가 있으면 바로 반환
		    if (Files.exists(thumbnailPath)) {
		        byte[] cachedImage = Files.readAllBytes(thumbnailPath);
		        return ResponseEntity.ok()
		            .contentType(MediaType.IMAGE_PNG)
		            .header("Cache-Control", "max-age=86400")
		            .body(cachedImage);
		    }

		    // 3. 없으면 → Node 서버 호출
		    List<MapPoint> points = courseService.getMapPoints(id);
		    if (points.isEmpty()) {
		        return ResponseEntity.noContent().build();
		    }

		    Map<String, Object> payload = Map.of(
		        "points", points,
		        "width", 400,
		        "height", 300
		    );

		    ObjectMapper mapper = new ObjectMapper();
		    String json = mapper.writeValueAsString(payload);

		    HttpRequest request = HttpRequest.newBuilder()
		        .uri(URI.create("http://localhost:4001/render"))
		        .header("Content-Type", "application/json")
		        .POST(HttpRequest.BodyPublishers.ofString(json))
		        .build();

		    HttpClient client = HttpClient.newHttpClient();
		    HttpResponse<byte[]> response =
		        client.send(request, HttpResponse.BodyHandlers.ofByteArray());

		    if (response.statusCode() != 200) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		    }

		    byte[] imageBytes = response.body();

		    // 4. 파일로 저장 (캐싱)
		    Files.write(thumbnailPath, imageBytes);

		    return ResponseEntity.ok()
		        .contentType(MediaType.IMAGE_PNG)
		        .header("Cache-Control", "no-cache, no-store, must-revalidate")
		        .body(imageBytes);
		}


	// 내가 등록한 코스 조회
	@GetMapping("/course/regist")
	public ResponseEntity<?> getMyRegistCourse(@RequestParam int userId) {
		List<Course> list = courseService.getMyRegistCourse(userId);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	// 내가 찜한 코스 조회
	@GetMapping("/course/like")
	public ResponseEntity<?> getMyLikeCourse(@RequestParam int userId) {
		List<Course> list = courseService.getMyLikeCourse(userId);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
}