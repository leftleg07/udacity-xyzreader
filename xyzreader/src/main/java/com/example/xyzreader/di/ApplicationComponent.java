package com.example.xyzreader.di;

import com.example.xyzreader.job.FetchJob;
import com.example.xyzreader.services.MyGcmJobService;
import com.example.xyzreader.services.MyJobService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * google dagger component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(FetchJob updateJob);

    void inject(MyJobService myJobService);

    void inject(MyGcmJobService myGcmJobService);
}
