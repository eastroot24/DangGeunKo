package com.danggeunko.course.dto;

public class CoursePoint {

    private Integer pointId;
    private Integer courseId;
    private Integer sequence;

    private Float latitude;
    private Float longitude;

    /* ===============================
       생성자
    =============================== */

    public CoursePoint() {
    }

    public CoursePoint(Integer pointId, Integer courseId, Integer sequence,
                          Float latitude, Float longitude) {

        this.pointId = pointId;
        this.courseId = courseId;
        this.sequence = sequence;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    /* ===============================
       Getter / Setter
    =============================== */

    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

	@Override
	public String toString() {
		return "CoursePoint [pointId=" + pointId + ", courseId=" + courseId + ", sequence=" + sequence + ", latitude="
				+ latitude + ", longitude=" + longitude + "]";
	}
    
}
