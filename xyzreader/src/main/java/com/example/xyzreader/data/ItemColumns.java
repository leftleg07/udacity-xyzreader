package com.example.xyzreader.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Item table columns
 */
public class ItemColumns {
    /** Type: INTEGER PRIMARY KEY AUTOINCREMENT */
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";
    /** Type: TEXT */
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SERVER_ID = "server_id";
    /** Type: TEXT NOT NULL */
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String TITLE = "title";
    /** Type: TEXT NOT NULL */
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String AUTHOR = "author";
    /** Type: TEXT NOT NULL */
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String BODY = "body";
    /** Type: TEXT NOT NULL */
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String THUMB_URL = "thumb_url";
    /** Type: TEXT NOT NULL */
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String PHOTO_URL = "photo_url";
    /** Type: REAL NOT NULL DEFAULT 1.5 */
    @DataType(DataType.Type.REAL) @NotNull @DefaultValue("1.5")
    public static final String ASPECT_RATIO = "aspect_ratio";
    /** Type: INTEGER NOT NULL DEFAULT 0 */
    @DataType(DataType.Type.INTEGER) @NotNull @DefaultValue("0")
    public static final String PUBLISHED_DATE = "published_date";
}
