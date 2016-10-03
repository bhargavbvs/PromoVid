package com.promovid.android.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bvs on 2/10/16.
 */
public class YoutubeChannelDetailsEntity {

    @SerializedName("items")
    @Expose
    private List<YoutubeChannelDetailsItemsEntity> items;

    public List<YoutubeChannelDetailsItemsEntity> getItems() {
        return items;
    }

    public void setItems(List<YoutubeChannelDetailsItemsEntity> items) {
        this.items = items;
    }
}
