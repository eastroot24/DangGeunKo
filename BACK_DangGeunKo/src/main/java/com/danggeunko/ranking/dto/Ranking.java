package com.danggeunko.ranking.dto;

public class Ranking {

    private Integer rankingId;
    private Integer reviewId;

    private String region;
    private Float score;
    private Integer rankOrder;

    /* ===============================
       생성자
    =============================== */

    public Ranking() {
    }

    public Ranking(Integer rankingId, Integer reviewId, String region,
                      Float score, Integer rankOrder) {

        this.rankingId = rankingId;
        this.reviewId = reviewId;
        this.region = region;
        this.score = score;
        this.rankOrder = rankOrder;
    }


    /* ===============================
       Getter / Setter
    =============================== */

    public Integer getRankingId() {
        return rankingId;
    }

    public void setRankingId(Integer rankingId) {
        this.rankingId = rankingId;
    }

    public Integer getreviewId() {
        return reviewId;
    }

    public void setreviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Float getscore() {
        return score;
    }

    public void setscore(Float score) {
        this.score = score;
    }

    public Integer getRankOrder() {
        return rankOrder;
    }

    public void setRankOrder(Integer rankOrder) {
        this.rankOrder = rankOrder;
    }

}

