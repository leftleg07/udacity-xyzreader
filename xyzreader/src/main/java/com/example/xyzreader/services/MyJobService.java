package com.example.xyzreader.services;

import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.example.xyzreader.ArticleApplication;

import javax.inject.Inject;

public class MyJobService extends FrameworkJobSchedulerService {
    @Inject JobManager jobManager;

    public MyJobService() {
        ((ArticleApplication) getApplication()).getComponent().inject(this);
    }

    @NonNull
    @Override
    protected JobManager getJobManager() {
        return jobManager;
    }
}
