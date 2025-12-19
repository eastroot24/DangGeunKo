package com.danggeunko.course.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danggeunko.course.dao.CourseDao;
import com.danggeunko.course.dao.CoursePointDao;
import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.CoursePoint;
import com.danggeunko.course.dto.MapPoint;
import com.danggeunko.course.dto.SearchCondition;


@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private CoursePointDao coursePointDao;

	@Transactional
	@Override
	public boolean addCourse(Course course) {
		return courseDao.insertCourse(course) > 0;
	}
	
	@Transactional
	@Override
	public List<Course> getAllCourses(Integer userId) {
		List<Course> courses = courseDao.selectAllCourses(userId);

	    for (Course course : courses) {
	        List<CoursePoint> points =
	            coursePointDao.selectPointsByCourseId(course.getCourseId());
	        course.setCoursePoints(points);
	    }

	    return courses;
	}
	
	@Transactional
	@Override
	public Course getCourseById(int id, Integer userId) {
		courseDao.updateViewCnt(id);
		// 코스 기본 정보
	    Course course = courseDao.selectCourseById(id, userId);

	    // 코스 포인트 조회
	    List<CoursePoint> points =
	        coursePointDao.selectPointsByCourseId(id);

	    course.setCoursePoints(points);

	    return course;
	}
	
	@Transactional
	@Override
	public Course updateCourseDetail(int id, Integer userId) {
		return courseDao.selectCourseById(id, userId);
	}

	@Transactional
	@Override
	public boolean updateCourse(Course course) {
		return courseDao.updateCourse(course) > 0;
	}

	@Transactional
	@Override
	public boolean deleteCourse(int id) {
		return courseDao.deleteCourse(id) > 0;
	}
	
	@Transactional
	@Override
	public List<Course> search(SearchCondition condition) {
		return courseDao.search(condition);
	}

	@Override
	public List<Course> getWeeklyRanking(SearchCondition condition) {
		return courseDao.selectWeeklyRanking(condition);
	}
	
	@Transactional
	@Override
	public int addCourseWithPoints(Course course) {
		// 1. 코스 저장
	    int result = courseDao.insertCourse(course);
	    if(result <= 0) return 0;

	    // 2. course_id가 생성되었으므로 point에 세팅
	    int courseId = course.getCourseId();
	    
	    List<CoursePoint> points = course.getCoursePoints();
	    
	    // **null 체크 및 빈 리스트 체크 (안정성 향상)**
	    if (points == null || points.isEmpty()) {
	        return courseId;
	    }

	    // 포인트 리스트에 course_id 세팅
	    for(CoursePoint point : points) {
	        point.setCourseId(courseId);
	    }

	    // 3. 코스 포인트 저장 (일반적으로 List<CoursePoint>를 받아서 bulk insert 수행)
	    int count = coursePointDao.insertCoursePoints(points);
	    
	    // 포인트 저장 실패 시 0 반환 (이때 트랜잭션이 롤백되어야 함)
	    if(count <= 0) {
	        throw new RuntimeException("코스 포인트 저장 중 오류가 발생했습니다.");
	    }

	    // 4. 최종적으로 새로 생성된 코스 ID 반환
	    return courseId;
	}
	
	@Transactional
	@Override
	public boolean addLike(int userId, int courseId) {
		int exists = courseDao.existsCourseLike(userId, courseId);

	    if (exists > 0) {
	        courseDao.deleteCourseLike(userId, courseId);
	        return false; // 좋아요 취소됨
	    } else {
	        courseDao.insertCourseLike(userId, courseId);
	        return true; // 좋아요 추가됨
	    }
	}

	@Transactional(readOnly = true)
	@Override
	public List<MapPoint> getMapPoints(int courseId) {

	    List<CoursePoint> points =
	        coursePointDao.selectPointsByCourseId(courseId);

	    if (points == null || points.isEmpty()) {
	        return List.of();
	    }

	    return points.stream()
	        .sorted(Comparator.comparingInt(CoursePoint::getSequence))
	        .map(p -> new MapPoint(
	            p.getLatitude(),
	            p.getLongitude()
	        ))
	        .collect(Collectors.toList());
	}

	@Override
	public List<Course> getMyRegistCourse(Map<String, Object> param) {
		return courseDao.selectMyRegistCourse(param);
	}

	@Override
	public List<Course> getMyLikeCourse(Map<String, Object> param) {
		return courseDao.selectMyLikeCourse(param);
	}


	

}
