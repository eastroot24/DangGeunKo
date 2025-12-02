package com.danggeunko.course.dto;

public class CourseLike {
	private int userId;
	private int course_id;
	
	public CourseLike(int userId, int course_id) {
		this.userId = userId;
		this.course_id = course_id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCourse_id() {
		return course_id;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	@Override
	public String toString() {
		return "CourseLike [userId=" + userId + ", course_id=" + course_id + "]";
	}
	
	
	
	
}
