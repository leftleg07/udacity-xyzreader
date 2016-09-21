package com.example.xyzreader.network;

import com.google.gson.annotations.SerializedName;

/**
 * data model
 */

public class ReaderData {

    @SerializedName("id")
    public String mId;

    @SerializedName("photo")
    public String mPhoto;

    @SerializedName("thumb")
    public String mThumb;

    @SerializedName("aspect_ratio")
    public double mAspectRatio;

    @SerializedName("author")
    public String mAuthor;

    @SerializedName("title")
    public String mTitle;

    @SerializedName("published_date")
    public String mPublishedDate;

    @SerializedName("body")
    public String mBody;
}
