package com.kevin.android.cardgamev1.database;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ChampionContract {

    public static final String CONTENT_AUTHORITY = "com.kevin.android.cardgamev1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CHAMPION = "champion";

    public static final class ChampionEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAMPION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAMPION;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CHAMPION);

        public static final String TABLE_NAME = "champions";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CHAMPION_NAME = "name";
        public static final String COLUMN_CHAMPION_IMAGE = "image";
        public static final String COLUMN_CHAMPION_ABILITY = "ability";
        public static final String COLUMN_CHAMPION_TEXT = "text";


        public static Uri getContentUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    
}
