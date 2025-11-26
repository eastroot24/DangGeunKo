package com.danggeunko.course.dto;

public class SearchCondition {
	private String key;
	private String value; // 난이도(런린이, 세미러너, 고수러너)
	private double minRange = 0; // 거리, 별점 범위
	private double maxRange = 0; // 거리, 별점 범위
	private String orderBy;
	private String orderByDir;

	public SearchCondition() {
	}

	public SearchCondition(String key, String value, double minRange, double maxRange, String orderBy,
			String orderByDir) {
		super();
		this.key = key; 
		this.value = value;
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
		return "SearchCondition [key=" + key + ", value=" + value + ", minRange=" + minRange + ", maxRange=" + maxRange
				+ ", orderBy=" + orderBy + ", orderByDir=" + orderByDir + "]";
	}

}
