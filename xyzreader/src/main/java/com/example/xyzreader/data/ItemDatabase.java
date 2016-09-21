package com.example.xyzreader.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

import static com.example.xyzreader.data.ItemContract.TABLE_NAME_ITEM;

/**
 * Item database
 */
@Database(fileName = ItemContract.DATABASE_NAME, version = ItemContract.DATABASE_VERSION)
public class ItemDatabase {
    private ItemDatabase() {}

    @Table(ItemColumns.class) public static final String ITEM = TABLE_NAME_ITEM;
}
