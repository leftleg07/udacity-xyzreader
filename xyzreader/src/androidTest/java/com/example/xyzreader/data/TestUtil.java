package com.example.xyzreader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.xyzreader.data.generated.ItemDatabase;

import java.util.Map;
import java.util.Set;

import static com.google.common.truth.Truth.assertWithMessage;

/**
 * db test util class
 */
public abstract class TestUtil {
    public static void deleteDatabase(Context context) {
        context.deleteDatabase(ItemContract.DATABASE_NAME);
    }

    static ContentValues createItemsEntryValues() {
        ContentValues entryValues = new ContentValues();
        entryValues.put(ItemColumns.SERVER_ID, "11");
        entryValues.put(ItemColumns.PHOTO_URL, "https://dl.dropboxusercontent.com/u/231329/xyzreader_data/images/p004.jpg");
        entryValues.put(ItemColumns.THUMB_URL, "https://dl.dropboxusercontent.com/u/231329/xyzreader_data/thumbs/p004.jpg");
        entryValues.put(ItemColumns.ASPECT_RATIO, 1.49925);
        entryValues.put(ItemColumns.AUTHOR, "Carl Sagan");
        entryValues.put(ItemColumns.TITLE, "Mysteries of the Universe Solved");
        entryValues.put(ItemColumns.PUBLISHED_DATE, "2013-06-20T00:00:00.000Z");
        entryValues.put(ItemColumns.BODY, "Paroxysm of global economics <a href='http://www.google.com'>Google Search</a> event take root and flourish, realm of the galaxies");
        return entryValues;
    }

    public static long insertItemsEntryValues(Context context) {
        SQLiteDatabase db = ItemDatabase.getInstance(context).getWritableDatabase();
        ContentValues testValues = TestUtil.createItemsEntryValues();

        long entryRowId = db.insert(ItemContract.TABLE_NAME_ITEM, null, testValues);

        // Verify we got a row back.
        assertWithMessage("Error: Failure to insert Item Entry Values").that(entryRowId).isNotEqualTo(-1);

        db.close();
        return entryRowId;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);

            assertWithMessage("Column '%s' not found. %s", columnName, error).that(idx).isNotEqualTo(-1);
            String expectedValue = entry.getValue().toString();
            String currentValue = valueCursor.getString(idx);
            assertWithMessage("Value '%s' did not match the expected value '%s'. %s", currentValue, expectedValue, error).that(currentValue).isEqualTo(expectedValue);
        }
    }

    public static void deleteAllRecord(Context context) {

        context.getContentResolver().delete(
                ItemProvider.Item.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = context.getContentResolver().query(
                ItemProvider.Item.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertWithMessage("Error: Records not deleted from Item table during delete").that(cursor.getCount()).isEqualTo(0);
        cursor.close();

    }

}
