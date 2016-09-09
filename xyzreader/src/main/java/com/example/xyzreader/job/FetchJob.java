package com.example.xyzreader.job;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.example.xyzreader.data.ItemColumns;
import com.example.xyzreader.data.ItemProvider;
import com.example.xyzreader.di.ApplicationComponent;
import com.example.xyzreader.network.ReaderData;
import com.example.xyzreader.network.XYZReaderService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import dagger.internal.Preconditions;

/**
 * Created by gsshop on 2016. 9. 5..
 */

public class FetchJob extends Job {

    private static final String LOG_TAG = FetchJob.class.getSimpleName();
    @Inject
    XYZReaderService mService;

    @Inject
    ContentResolver mContentResolver;



    public FetchJob() {
        // This job requires network connectivity,
        // and should be persisted in case the application exits before job is completed.
        super(new Params(Priority.MID).requireNetwork().singleInstanceBy("update-data"));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        String jsonStr = mService.getReaderData().toBlocking().single();
        ReaderData[] data = new Gson().fromJson(jsonStr, ReaderData[].class);
        try {
            ArrayList batch = buildReaderDataBatchOperation(data);
            if (batch != null && batch.size() > 0) {
                mContentResolver.applyBatch(ItemProvider.AUTHORITY, batch);
                mContentResolver.notifyChange(
                        ItemProvider.Item.CONTENT_URI, // URI where data was modified
                        null,                           // No local observer
                        false);                         // IMPORTANT: Do not sync to network
            }
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    public void inject(ApplicationComponent component) {
        component.inject(this);
    }

    public ArrayList buildReaderDataBatchOperation(ReaderData[] data) {

        final ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();

        // Build hash table of incoming entries
        final HashMap<String, ReaderData> entryMap = new HashMap<>();
        for (ReaderData e : data) {
            entryMap.put(e.mId, e);
        }

        Cursor cursor = mContentResolver.query(ItemProvider.Item.CONTENT_URI, null, null, null, null);
        Preconditions.checkNotNull(cursor);

        Log.i(LOG_TAG, "Found " + cursor.getCount() + " local entries. Computing merge solution...");

        while (cursor.moveToNext()) {
            String serverId = cursor.getString(cursor.getColumnIndex(ItemColumns.SERVER_ID));
            ReaderData match = entryMap.get(serverId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(serverId);
                // Check to see if the entry needs to be updated
                String title = cursor.getString(cursor.getColumnIndex(ItemColumns.TITLE));
                String author = cursor.getString(cursor.getColumnIndex(ItemColumns.AUTHOR));
                String body = cursor.getString(cursor.getColumnIndex(ItemColumns.BODY));
                String thumbUrl = cursor.getString(cursor.getColumnIndex(ItemColumns.THUMB_URL));
                String photoUrl = cursor.getString(cursor.getColumnIndex(ItemColumns.PHOTO_URL));
                double ratio = cursor.getDouble(cursor.getColumnIndex(ItemColumns.ASPECT_RATIO));
                Uri existingUri = ItemProvider.Item.withServerId(serverId);
                if (!match.mTitle.equals(title) || !match.mAuthor.equals(author) || !match.mBody.equals(body) || !match.mThumb.equals(thumbUrl)|| !match.mPhoto.equals(photoUrl) || match.mAspectRatio != ratio) {
                    // Update existing record

                    Log.i(LOG_TAG, "Scheduling update: " + existingUri);
                    batchOperations.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ItemColumns.TITLE, match.mTitle)
                            .withValue(ItemColumns.AUTHOR, match.mAuthor)
                            .withValue(ItemColumns.BODY, match.mBody)
                            .withValue(ItemColumns.THUMB_URL, match.mThumb)
                            .withValue(ItemColumns.PHOTO_URL, match.mPhoto)
                            .withValue(ItemColumns.ASPECT_RATIO, match.mAspectRatio)
                            .withValue(ItemColumns.PUBLISHED_DATE, match.mPublishedDate)
                            .build());
                } else {
                    Log.i(LOG_TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = ItemProvider.Item.withServerId(serverId);
                Log.i(LOG_TAG, "Scheduling delete: " + deleteUri);
                batchOperations.add(ContentProviderOperation.newDelete(deleteUri).build());
            }
        }
        cursor.close();

        for (ReaderData entry : entryMap.values()) {
            Log.i(LOG_TAG, "Scheduling insert: entry_id=" + entry.mId);
            batchOperations.add(ContentProviderOperation.newInsert(ItemProvider.Item.CONTENT_URI)
                    .withValue(ItemColumns.SERVER_ID, entry.mId)
                    .withValue(ItemColumns.TITLE, entry.mTitle)
                    .withValue(ItemColumns.AUTHOR, entry.mAuthor)
                    .withValue(ItemColumns.BODY, entry.mBody)
                    .withValue(ItemColumns.THUMB_URL, entry.mThumb)
                    .withValue(ItemColumns.PHOTO_URL, entry.mPhoto)
                    .withValue(ItemColumns.ASPECT_RATIO, entry.mAspectRatio)
                    .withValue(ItemColumns.PUBLISHED_DATE, entry.mPublishedDate)
                    .build());
        }

        Log.i(LOG_TAG, "Merge solution ready. Applying batch update");

        return batchOperations;
    }

}
