package com.example.xyzreader;

import android.app.Application;

import com.example.xyzreader.di.ApplicationComponent;
import com.example.xyzreader.di.ApplicationModule;
import com.example.xyzreader.di.DaggerApplicationComponent;
import com.facebook.stetho.Stetho;

/**
 * application
 */
public class ArticleApplication extends Application {
    public ApplicationComponent getComponent() {
        return component;
    }

    private ApplicationComponent component;

    private static ArticleApplication instance;

    public ArticleApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        component = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public static ArticleApplication getInstance() {
        return instance;
    }
}
