package com.example.xyzreader.job;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.birbit.android.jobqueue.JobManager;
import com.example.xyzreader.MockApplication;
import com.example.xyzreader.data.ItemProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by gsshop on 2016. 9. 5..
 */
@RunWith(AndroidJUnit4.class)
public class FetchJobTest {
    @Inject
    JobManager mJobManager;

    @Inject
    ContentResolver mContentResolver;

    private CountDownLatch mSignal;
    @Before
    public void setUp() {
        MockApplication application = (MockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.getMockComponent().inject(this);
        mSignal = new CountDownLatch(1);
        EventBus.getDefault().register(this);
    }


    @After
    public void tearDown() throws Exception {
        EventBus.getDefault().unregister(this);
        mSignal.countDown();


    }

    @Test
    public void testUpdateJob() throws Exception {
        mJobManager.addJob(new FetchJob());
        mSignal.await();
        Cursor cursor = mContentResolver.query(ItemProvider.Item.CONTENT_URI, null, null, null, null);
        assertThat(cursor.getCount()).isGreaterThan(0);
    }

    @Subscribe
    public void onEvent(FetchJobEvent event) {
        mSignal.countDown();
    }

}
