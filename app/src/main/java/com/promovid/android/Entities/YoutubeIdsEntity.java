package com.promovid.android.Entities;

import java.util.List;

/**
 * Created by bvs on 2/10/16.
 */
public class YoutubeIdsEntity {

    private List<String> data;
    private StatusEntity status;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }
}
