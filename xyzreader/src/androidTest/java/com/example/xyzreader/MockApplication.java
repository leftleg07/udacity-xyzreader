package com.example.xyzreader;


import com.example.xyzreader.di.ApplicationModule;
import com.example.xyzreader.di.DaggerMockApplicationComponent;
import com.example.xyzreader.di.MockApplicationComponent;

/**
 * Mock Application
 */
public class MockApplication extends ArticleApplication {
    public MockApplicationComponent getMockComponent() {
        return mMockComponent;
    }

    private MockApplicationComponent mMockComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mMockComponent = DaggerMockApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }
}
