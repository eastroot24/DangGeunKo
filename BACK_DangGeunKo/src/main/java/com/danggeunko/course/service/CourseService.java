package com.danggeunko.course.service;

import java.util.List;

import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.SearchCondition;

public interface CourseService {
	
	// 코스 등록
	public boolean addCourse(Course course);
		
	// 코스 전체 조회
	public List<Course> getAllCourses();
		
	// 코스 아이디 조회
	public Course getCourseById(int id);
		
	// 코스 업데이트
	public boolean updateCourse(int id, Course course);
		
	// 코스 삭제
	public boolean deleteCourse(int id);
		
	// 게시글 검색
	public List<Course> search(SearchCondition condition);
}
