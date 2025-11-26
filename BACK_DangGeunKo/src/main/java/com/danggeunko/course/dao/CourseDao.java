package com.danggeunko.course.dao;

import java.util.List;

import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.SearchCondition;

public interface CourseDao {
	
	// 코스 등록
	public int insertCourse(Course course);
	
	// 코스 전체 조회
	public List<Course> selectAllCourses();
	
	// 코스 아이디 조회
	public Course selectCourseById(int id);
	
	// 코스 업데이트
	public int updateCourse(int id, Course course);
	
	// 코스 삭제
	public int deleteCourse(int id);
	
	// 조회수 증가
	public void updateViewCnt(int id);
	
	// 게시글 검색
	public List<Course> search(SearchCondition condition);
}
