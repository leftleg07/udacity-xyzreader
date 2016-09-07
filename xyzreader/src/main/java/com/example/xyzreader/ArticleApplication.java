package com.example.xyzreader;

import android.app.Application;

import com.example.xyzreader.di.ApplicationComponent;
import com.example.xyzreader.di.ApplicationModule;
import com.example.xyzreader.di.DaggerApplicationComponent;
import com.facebook.stetho.Stetho;

/**
 * Created by gsshop on 2016. 9. 5..
 */

public class ArticleApplication extends Application {
    public ApplicationComponent getComponent() {
        return component;
    }

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        component = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }
}
