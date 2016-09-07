package com.example.xyzreader.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.xyzreader.data.generated.ItemDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/**
 * Tests for database
 */
@RunWith(AndroidJUnit4.class)
public class TestDb {
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getTargetContext();
        TestUtil.deleteDatabase(mContext);
    }

    @Test
    public void testCreateDb() throws Exception {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(ItemContract.TABLE_NAME_ITEM);


        SQLiteDatabase db = ItemDatabase.getInstance(mContext).getWritableDatabase();
        assertThat(db.isOpen()).isTrue();

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertWithMessage("Error: This means that the database has not been created correctly").that(c.moveToFirst()).isTrue();

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertWithMessage("Error: Your database was created without the tables").that(tableNameHashSet.size()).isEqualTo(0);
        c.close();

        /**
         * quotes table
         */
        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + ItemContract.TABLE_NAME_ITEM + ")",
                null);

        assertWithMessage("Error: This means that we were unable to query the database for table information.").that(c.moveToFirst()).isTrue();


        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> entryColumnHashSet = new HashSet<String>();
        entryColumnHashSet.add(ItemColumns._ID);
        entryColumnHashSet.add(ItemColumns.SERVER_ID);
        entryColumnHashSet.add(ItemColumns.TITLE);
        entryColumnHashSet.add(ItemColumns.AUTHOR);
        entryColumnHashSet.add(ItemColumns.BODY);
        entryColumnHashSet.add(ItemColumns.THUMB_URL);
        entryColumnHashSet.add(ItemColumns.PHOTO_URL);
        entryColumnHashSet.add(ItemColumns.ASPECT_RATIO);
        entryColumnHashSet.add(ItemColumns.PUBLISHED_DATE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            entryColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertWithMessage("Error: The database doesn't contain all of the required item entry columns").that(entryColumnHashSet.isEmpty()).isTrue();
        c.close();

        db.close();
    }

    @Test
    public void testItemTable() throws Exception {
        ContentValues testValues = TestUtil.createItemsEntryValues();

        // insert data
        TestUtil.insertItemsEntryValues(mContext);

        SQLiteDatabase db = ItemDatabase.getInstance(mContext).getWritableDatabase();
        assertThat(db.isOpen()).isTrue();

        Cursor cursor = db.query(
                ItemContract.TABLE_NAME_ITEM,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        assertWithMessage("Error: No Records returned from quotes query").that(cursor.moveToFirst()).isTrue();

        TestUtil.validateCurrentRecord("Error: Item Query Validation Failed", cursor, testValues);

        cursor.close();
        db.close();

    }


}
