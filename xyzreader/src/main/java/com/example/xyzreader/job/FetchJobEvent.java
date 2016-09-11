package com.example.xyzreader.job;

/**
 * Created by gsshop on 2016. 9. 6..
 */

public class FetchJobEvent {
    public final boolean isUpdated;

    public FetchJobEvent(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }
}
