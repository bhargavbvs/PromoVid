package com.promovid.android.Events;

/**
 * Created by bvs on 3/10/16.
 */
public class OnVideoIdsDownloadedEvent {
    private final String message;

    public OnVideoIdsDownloadedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
