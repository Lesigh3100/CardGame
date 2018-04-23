package com.kevin.android.cardgamev1.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DeckContract {
    public static final String CONTENT_AUTHORITY = "com.kevin.android.cardgamev1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DECK = "decks";

    public static final class DeckEntry implements BaseColumns{

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DECK;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DECK;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DECK);

        public static final String TABLE_NAME = "decks";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DECK_DECKNAME = "deckname";
        public static final String COLUMN_DECK_IMAGE = "championimage";
        public static final String COLUMN_DECK_CHAMPION = "championtype";
        public static final String COLUMN_DECK_CARDLIST = "cardlist";
        public static final String COLUMN_DECK_USES = "deckuses";



        public static Uri getContentUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
