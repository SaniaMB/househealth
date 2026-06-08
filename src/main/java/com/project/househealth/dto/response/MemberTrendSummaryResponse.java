package com.project.househealth.dto.response;

public class MemberTrendSummaryResponse {

    private Long userId;
    private String userName;

    private String bloodPressureTrend;
    private String fastingSugarTrend;
    private String postMealSugarTrend;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBloodPressureTrend() {
        return bloodPressureTrend;
    }

    public void setBloodPressureTrend(
            String bloodPressureTrend
    ) {
        this.bloodPressureTrend =
                bloodPressureTrend;
    }

    public String getFastingSugarTrend() {
        return fastingSugarTrend;
    }

    public void setFastingSugarTrend(
            String fastingSugarTrend
    ) {
        this.fastingSugarTrend =
                fastingSugarTrend;
    }

    public String getPostMealSugarTrend() {
        return postMealSugarTrend;
    }

    public void setPostMealSugarTrend(
            String postMealSugarTrend
    ) {
        this.postMealSugarTrend =
                postMealSugarTrend;
    }
}