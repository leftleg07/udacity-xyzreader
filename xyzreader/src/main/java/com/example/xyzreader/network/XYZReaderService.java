package com.example.xyzreader.network;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by gsshop on 2016. 9. 5..
 */

public interface XYZReaderService {
    String BASE_URL = "https://dl.dropboxusercontent.com";

    @GET("u/231329/xyzreader_data/data.json")
    Observable<String> getReaderData();
}
