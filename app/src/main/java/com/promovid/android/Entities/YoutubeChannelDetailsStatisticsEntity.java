package com.promovid.android.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bvs on 2/10/16.
 */
public class YoutubeChannelDetailsStatisticsEntity {

    @SerializedName("viewCount")
    @Expose
    private String viewCount;
    @SerializedName("likeCount")
    @Expose
    private String likeCount;
    @SerializedName("commentCount")
    @Expose
    private String commentCount;

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
