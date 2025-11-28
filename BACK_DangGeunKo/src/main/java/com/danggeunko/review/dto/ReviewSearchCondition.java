package com.danggeunko.review.dto;

import java.util.Objects;

/**
 * 리뷰 조회를 위한 검색 조건 및 정렬/페이징 정보를 담는 DTO (Data Transfer Object).
 * 코스별 리뷰 조회 시, 정렬 기준(별점순, 최신순 등)을 동적으로 처리하기 위해 사용됩니다.
 */
public class ReviewSearchCondition {

    // --- 1. 필수 조건: 코스 식별자 (FK) ---
    private int courseId; 

    // --- 2. 정렬 조건 (Sorting Criteria) ---
    
    // 정렬 기준 필드 (예: "rating" 또는 "createdAt")
    // 기본값: 최신순 조회를 위해 "createdAt"으로 설정합니다.
    private String sortBy = "createdAt"; 

    // 정렬 방향 (예: "desc" - 내림차순, "asc" - 오름차순)
    // 기본값: 높은 별점/최신순을 위해 "desc"로 설정합니다.
    private String sortDirection = "desc"; 
    
    // --- 3. 페이징 정보 (Paging Information) ---
    
    // 페이지 번호 (0부터 시작)
    private Integer page = 0; 
    
    // 페이지 당 항목 수
    private Integer size = 10; 
    
    // --- 4. [선택적] 필터링 조건 (Optional Filter) ---
    
    // 최소 별점 필터링을 위한 필드 (예: 4점 이상 리뷰만 조회)
    // private Integer minRating; 

    // --- 5. Constructor (생성자) ---
    
    // 기본 생성자
    public ReviewSearchCondition() {
    }

    // 모든 필드를 포함하는 생성자 
    public ReviewSearchCondition(int courseId, String sortBy, String sortDirection, Integer page, Integer size) {
        this.courseId = courseId;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        this.page = page;
        this.size = size;
    }

    // --- 6. Getter (값 조회) ---

    public int getCourseId() {
        return courseId;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }
    
    // public Integer getMinRating() {
    //     return minRating;
    // }

    // --- 7. Setter (값 설정) ---

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setSortBy(String sortBy) {
        // 정렬 기준을 안전하게 설정하기 위한 검증 로직 추가 가능 (예: 허용된 필드만 사용)
        this.sortBy = sortBy;
    }

    public void setSortDirection(String sortDirection) {
        // 정렬 방향을 대소문자 구분 없이 처리하기 위한 로직 추가 가능
        this.sortDirection = sortDirection;
    }

    public void setPage(Integer page) {
        // 페이지 번호가 음수가 되지 않도록 방어 코드 추가 가능
        this.page = page;
    }

    public void setSize(Integer size) {
        // 페이지 크기가 너무 크거나 작지 않도록 방어 코드 추가 가능
        this.size = size;
    }

    // --- 8. toString (객체 상태 확인) ---

    @Override
    public String toString() {
        return "ReviewSearchCondition{" +
               "courseId=" + courseId +
               ", sortBy='" + sortBy + '\'' +
               ", sortDirection='" + sortDirection + '\'' +
               ", page=" + page +
               ", size=" + size +
               '}';
    }

}
