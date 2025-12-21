package com.danggeunko.course.dao;

import java.util.List;
import java.util.Map;

import com.danggeunko.course.dto.Course;
import com.danggeunko.course.dto.SearchCondition;

public interface CourseDao {
	
	// 코스 등록
	public int insertCourse(Course course);
	
	// 코스 전체 조회
	public List<Course> selectAllCourses(Integer userId);
	
	// 코스 아이디 조회
	public Course selectCourseById(int id, Integer userId);
	
	// 코스 업데이트
	public int updateCourse(Course course);
	
	// 코스 삭제
	public int deleteCourse(int id);
	
	// 조회수 증가
	public void updateViewCnt(int id);
	
	// 게시글 검색
	public List<Course> search(SearchCondition condition);
	
	// 게시글 정렬
	public List<Course> selectWeeklyRanking(SearchCondition condition);
	
	// 좋아요 여부 확인
	int existsCourseLike(int userId, int courseId);

	// 좋아요 추가
	int insertCourseLike(int userId, int courseId);

	// 좋아요 삭제
	 int deleteCourseLike(int userId, int courseId);

	// 좋아요 개수 조회
	int countCourseLike(int courseId);
	
	//내가 등록한 코스 조회
	List<Course> selectMyRegistCourse(Map<String, Object> param);
	//내가 찜한 코스 조회
	List<Course> selectMyLikeCourse(Map<String, Object> param);
	
	public int updateCourseAvgRating(double avgRating, int courseId);
}
