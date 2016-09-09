package com.example.xyzreader.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.example.xyzreader.ArticleApplication;

import javax.inject.Inject;

/**
 * Created by yboyar on 3/20/16.
 */
public class MyGcmJobService extends GcmJobSchedulerService {
    @Inject
    JobManager jobManager;

    public MyGcmJobService() {
        Context context = getApplicationContext();
        ((ArticleApplication)context).getComponent().inject(this);
    }

    @NonNull
    @Override
    protected JobManager getJobManager() {
        return jobManager;
    }
}
