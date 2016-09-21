package com.example.xyzreader.job;

/**
 * job event
 */

public class FetchJobEvent {
    public final boolean isUpdated;

    public FetchJobEvent(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }
}
