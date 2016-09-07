package com.example.xyzreader.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

import static com.example.xyzreader.data.ItemContract.TABLE_NAME_ITEM;

/**
 * Created by gsshop on 2016. 9. 7..
 */
@Database(fileName = ItemContract.DATABASE_NAME, version = ItemContract.DATABASE_VERSION)
public class ItemDatabase {
    private ItemDatabase() {}

    @Table(ItemColumns.class) public static final String ITEM = TABLE_NAME_ITEM;
}
