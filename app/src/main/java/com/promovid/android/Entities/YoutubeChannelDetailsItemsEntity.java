package com.promovid.android.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bvs on 2/10/16.
 */
public class YoutubeChannelDetailsItemsEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("snippet")
    @Expose
    private YoutubeChannelDetailsSnippetEntity snippet;
    @SerializedName("statistics")
    @Expose
    private YoutubeChannelDetailsStatisticsEntity statistics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public YoutubeChannelDetailsSnippetEntity getSnippet() {
        return snippet;
    }

    public void setSnippet(YoutubeChannelDetailsSnippetEntity snippet) {
        this.snippet = snippet;
    }

    public YoutubeChannelDetailsStatisticsEntity getStatistics() {
        return statistics;
    }

    public void setStatistics(YoutubeChannelDetailsStatisticsEntity statistics) {
        this.statistics = statistics;
    }
}
