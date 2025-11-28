package com.danggeunko.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danggeunko.course.dao.CourseDao;
import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.SearchCondition;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseDao courseDao;

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
	public boolean updateCourse(int id, Course course) {
		return courseDao.updateCourse(id, course) > 0;
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
	public List<Course> getWeeklyRanking(String region) {
		return courseDao.selectWeeklyRanking(region);
	}

}
