package com.example.xyzreader.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static com.example.xyzreader.data.ItemColumns.SERVER_ID;
import static com.example.xyzreader.data.ItemContract.TABLE_NAME_ITEM;

/**
 * Created by gsshop on 2016. 9. 7..
 */
@ContentProvider(authority = ItemProvider.AUTHORITY, database = ItemDatabase.class)
public class ItemProvider {
    public static final String AUTHORITY = "com.example.xyzreader.data";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String Item = TABLE_NAME_ITEM;
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table =TABLE_NAME_ITEM)
    public static class Item {
        @ContentUri(
                path = Path.Item,
                type = "vnd.android.cursor.dir/item"
        )
        public static final Uri CONTENT_URI = buildUri(Path.Item);

        @InexactContentUri(
                name = SERVER_ID,
                path = Path.Item + "/*",
                type = "vnd.android.cursor.item/item",
                whereColumn = SERVER_ID,
                pathSegment = 1
        )
        public static Uri withServerId(String serverId) {
            return buildUri(Path.Item, serverId);
        }

        public static String getServerIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
