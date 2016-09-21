package com.example.xyzreader.network;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.xyzreader.MockApplication;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertThat;

/**
 * API Service
 */
@RunWith(AndroidJUnit4.class)
public class XYZReaderServiceTest {
    @Inject
    XYZReaderService mService;

    @Before
    public void setUp() {
        MockApplication application = (MockApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        application.getMockComponent().inject(this);

    }

    @Test
    public void testService() throws Exception {
        String jsonStr = mService.getReaderData().toBlocking().single();
        ReaderData[] data = new Gson().fromJson(jsonStr, ReaderData[].class);
        assertThat(data.length).isGreaterThan(0);

    }
}
