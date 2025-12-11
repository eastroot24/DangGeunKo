package com.danggeunko.course.dto;

public class SearchCondition {
	private String key;	  // 제목, 작성자, 내용
	private String value; // Key에 해당하는 값
	
	private String courseCity; // 시/도
	private String courseDistrict; // 지역구
	private String difficulty; // 런린이, 러너, 런고수
	
	private double minRange; // 거리, 별점 범위
	private double maxRange; // 거리, 별점 범위
	private String orderBy;
	private String orderByDir;

	public SearchCondition() {
	}

	public SearchCondition(String key, String value, String courseCity, String courseDistrict, String difficulty,
			double minRange, double maxRange, String orderBy, String orderByDir) {
		super();
		this.key = key;
		this.value = value;
		this.courseCity = courseCity;
		this.courseDistrict = courseDistrict;
		this.difficulty = difficulty;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.orderBy = orderBy;
		this.orderByDir = orderByDir;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCourseCity() {
		return courseCity;
	}

	public void setCourseCity(String courseCity) {
		this.courseCity = courseCity;
	}

	public String getCourseDistrict() {
		return courseDistrict;
	}

	public void setCourseDistrict(String courseDistrict) {
		this.courseDistrict = courseDistrict;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public double getMinRange() {
		return minRange;
	}

	public void setMinRange(double minRange) {
		this.minRange = minRange;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderByDir() {
		return orderByDir;
	}

	public void setOrderByDir(String orderByDir) {
		this.orderByDir = orderByDir;
	}

	@Override
	public String toString() {
		return "SearchCondition [key=" + key + ", value=" + value + ", courseCity=" + courseCity + ", courseDistrict="
				+ courseDistrict + ", difficulty=" + difficulty + ", minRange=" + minRange + ", maxRange=" + maxRange
				+ ", orderBy=" + orderBy + ", orderByDir=" + orderByDir + "]";
	}

	

	
	
	
	
	
}
