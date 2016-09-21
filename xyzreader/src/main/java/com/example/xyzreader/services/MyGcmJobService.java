package com.example.xyzreader.services;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.example.xyzreader.ArticleApplication;

import javax.inject.Inject;

/**
 * gcm task service
 */
public class MyGcmJobService extends GcmJobSchedulerService {
    @Inject
    JobManager jobManager;

    public MyGcmJobService() {
        ArticleApplication.getInstance().getComponent().inject(this);
    }

    @NonNull
    @Override
    protected JobManager getJobManager() {
        return jobManager;
    }
}
