package com.example.xyzreader.di;

import com.example.xyzreader.job.FetchJobTest;
import com.example.xyzreader.network.XYZReaderServiceTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * google dagger component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface MockApplicationComponent {
    void inject(XYZReaderServiceTest xyzReaderServiceTest);
    void inject(FetchJobTest updateJobTest);
}
