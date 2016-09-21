package com.example.xyzreader.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/**
 * Test for content provider
 */
@RunWith(AndroidJUnit4.class)
public class TestProvider {
    private ContentResolver mContentResolver;
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        mContentResolver = mContext.getContentResolver();
        TestUtil.deleteAllRecord(mContext);
    }

    @Test
    public void testProviderRegistry() throws Exception {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // MovieProvider class.
        String pkg = mContext.getPackageName();

        String cls = com.example.xyzreader.data.generated.ItemProvider.class.getName();
        ComponentName componentName = new ComponentName(pkg, cls);
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertWithMessage("Error: ItemProvider registered with authority: %s instead of authority: %s", providerInfo.authority, ItemProvider.AUTHORITY).that(providerInfo.authority).isEqualTo(ItemProvider.AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.

            assertWithMessage("Error: ItemProvider not registered at " + mContext.getPackageName()).that(false).isTrue();
        }

    }

    @Test
    public void testQuotesTable() throws Exception {
        // insert

        ContentValues testValues = TestUtil.createItemsEntryValues();
        ContentValues updateValues = new ContentValues(testValues);
        updateValues.put(ItemColumns.AUTHOR, "Charls Sun");


        Uri uri = mContentResolver.insert(ItemProvider.Item.CONTENT_URI, testValues);
        long itemId = ContentUris.parseId(uri);

        assertWithMessage("Error: Item Query Validation Failed").that(itemId).isGreaterThan((long) 0);

        // update
        uri = ItemProvider.Item.withServerId(updateValues.getAsString(ItemColumns.SERVER_ID));
        int count = mContentResolver.update(uri, updateValues, null, null);
        assertThat(count).isEqualTo(1);

        Cursor cursor = mContentResolver.query(uri, null, null, null, null, null);

        assertWithMessage("Error: No Records returned from Quotes query").that(cursor.moveToFirst()).isTrue();
        TestUtil.validateCurrentRecord("Error: Quotes Query Validation Failed", cursor, updateValues);
        cursor.close();

    }
}
