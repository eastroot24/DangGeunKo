package com.danggeunko.course.service;

import java.util.List;

import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.MapPoint;
import com.danggeunko.course.dto.SearchCondition;

public interface CourseService {
	
	// 코스 등록
	public boolean addCourse(Course course);
		
	// 코스 전체 조회
	public List<Course> getAllCourses(Integer userId);
		
	// 코스 아이디 조회
	public Course getCourseById(int id, Integer userId);
		
	//코스 상세 업데이트
	public Course updateCourseDetail(int id, Integer userId);
	
	// 코스 업데이트
	public boolean updateCourse(Course course);
		
	// 코스 삭제
	public boolean deleteCourse(int id);
		
	// 게시글 검색
	public List<Course> search(SearchCondition condition);
	
	// 게시글 정렬
	public List<Course> getWeeklyRanking(SearchCondition condition);

	public int addCourseWithPoints(Course course);
	
	boolean addLike(int userId, int courseId);
	
	List<MapPoint> getMapPoints(int courseId);

}
