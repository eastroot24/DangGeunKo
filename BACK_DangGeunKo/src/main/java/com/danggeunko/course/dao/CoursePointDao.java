package com.danggeunko.course.dao;

import java.util.List;

import com.danggeunko.course.dto.CoursePoint;

public interface CoursePointDao {
	
	 int insertCoursePoint(CoursePoint point);

	    int insertCoursePoints(List<CoursePoint> points);

	    List<CoursePoint> selectPointsByCourseId(int courseId);

	    int deletePointsByCourseId(int courseId);

	    int deletePoint(int pointId);
}
