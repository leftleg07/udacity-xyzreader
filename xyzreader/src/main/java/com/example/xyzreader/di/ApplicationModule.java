package com.example.xyzreader.di;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.example.xyzreader.ArticleApplication;
import com.example.xyzreader.job.FetchJob;
import com.example.xyzreader.network.XYZReaderService;
import com.example.xyzreader.services.MyGcmJobService;
import com.example.xyzreader.services.MyJobService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * dagger application module
 */
@Module
public class ApplicationModule {
    private final Context mApplicationContext;

    public ApplicationModule(Context applicationContext) {
        this.mApplicationContext = applicationContext;
    }

    @Provides
    @Singleton
    public Context proviceApplicationContext() {
        return mApplicationContext;
    }

    @Provides
    @Singleton // Application reference must come from AppModule.class
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplicationContext);
    }

    @Provides
    @Singleton
    public XYZReaderService provideTheMovieDBApiService() {

        OkHttpClient client = buildOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(XYZReaderService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();


        return retrofit.create(XYZReaderService.class);
    }

    private OkHttpClient buildOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        return httpClient.build();

    }

    /**
     * JobManager
     *
     * @return
     */
    @Singleton
    @Provides
    public JobManager provideJobManager() {
        Configuration.Builder builder = new Configuration.Builder(mApplicationContext)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {

                        ApplicationComponent component = ((ArticleApplication) mApplicationContext).getComponent();
                        ((FetchJob) job).inject(component);
                    }
                })
                .consumerKeepAlive(120);//wait 2 minute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(mApplicationContext,
                    MyJobService.class), true);
        } else {
            int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mApplicationContext);
            if (enableGcm == ConnectionResult.SUCCESS) {
                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(mApplicationContext,
                        MyGcmJobService.class), true);
            }
        }
        return new JobManager(builder.build());
    }

    @Singleton
    @Provides
    public ContentResolver getContentResolver() {
        return mApplicationContext.getContentResolver();
    }

}
