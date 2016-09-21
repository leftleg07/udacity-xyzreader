package com.example.xyzreader.network;

import retrofit2.http.GET;
import rx.Observable;

/**
 * api service
 */

public interface XYZReaderService {
    String BASE_URL = "https://dl.dropboxusercontent.com";

    @GET("u/231329/xyzreader_data/data.json")
    Observable<String> getReaderData();
}
