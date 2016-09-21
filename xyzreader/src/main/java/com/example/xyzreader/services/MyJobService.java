package com.example.xyzreader.services;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.example.xyzreader.ArticleApplication;

import javax.inject.Inject;

/**
 * job service
 */
public class MyJobService extends FrameworkJobSchedulerService {
    @Inject JobManager jobManager;

    public MyJobService() {
        ArticleApplication.getInstance().getComponent().inject(this);
    }

    @NonNull
    @Override
    protected JobManager getJobManager() {
        return jobManager;
    }
}
