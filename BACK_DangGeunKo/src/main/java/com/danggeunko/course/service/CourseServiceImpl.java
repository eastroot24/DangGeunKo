package com.danggeunko.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danggeunko.course.dao.CourseDao;
import com.danggeunko.course.dao.CoursePointDao;
import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.CoursePoint;
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
	public List<Course> getAllCourses() {
		return courseDao.selectAllCourses();
	}
	
	@Transactional
	@Override
	public Course getCourseById(int id) {
		courseDao.updateViewCnt(id);
		return courseDao.selectCourseById(id);
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
	public boolean addCourseWithPoints(Course course) {
		// 1. 코스 저장
	    int result = courseDao.insertCourse(course);
	    if(result <= 0) return false;

	    // 2. course_id가 생성되었으므로 point에 세팅
	    int courseId = course.getCourseId();
	    //여기서 null이 잡히는 것 같음..
	    for(CoursePoint point : course.getCoursePoints()) {
	        point.setCourseId(courseId);
	    }

	    // 3. 코스 포인트 저장
	    int count = coursePointDao.insertCoursePoints(course.getCoursePoints());
	    if(count <= 0) return false;

	    return true;
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

}
