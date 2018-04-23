package com.kevin.android.cardgamev1.database;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class CardContract {

    public static final String CONTENT_AUTHORITY = "com.kevin.android.cardgamev1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CARD = "cards";

    private CardContract(){
    }

    public static final class CardEntry implements BaseColumns{
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARD;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARD;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CARD);

        public static final String TABLE_NAME = "cards";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CARD_NAME = "name";
        public static final String COLUMN_CARD_IMAGE = "image";
        public static final String COLUMN_CARD_TYPE = "type";
        public static final String COLUMN_CARD_TEXT = "text";
        public static final String COLUMN_CARD_MANACOST = "manacost";
        public static final String COLUMN_CARD_CHAMPION = "champion";
        public static final String COLUMN_CARD_LIFE = "life";
        public static final String COLUMN_CARD_ATTACK = "attack";
        public static final String COLUMN_CARD_DEFENSE = "defense";
        public static final String COLUMN_CARD_QUICK = "quick";
        public static final String COLUMN_CARD_OVERPOWER = "overpower";
        public static final String COLUMN_CARD_DEFENDER = "defender";
        public static final String COLUMN_CARD_RANGED = "ranged";
        public static final String COLUMN_CARD_AREAATTACK = "areaattack";
        public static final String COLUMN_CARD_LIGHTNINGREFLEXES = "lightningreflexes";
        public static final String COLUMN_CARD_CONCEALED = "concealed";
        public static final String COLUMN_CARD_PILLAGE = "pillage";
        public static final String COLUMN_CARD_BATTLEREADY = "battleready";
        public static final String COLUMN_CARD_RALLY = "rally";
        public static final String COLUMN_CARD_BLUFF = "bluff";
        public static final String COLUMN_CARD_UNSTABLE = "unstable";
        public static final String COLUMN_CARD_FIREDUP = "firedup";
        public static final String COLUMN_CARD_DRAWCARD = "drawcard";
        public static final String COLUMN_CARD_WARMBLOODED = "warmblooded";
        public static final String COLUMN_CARD_LASTWORD = "lastword";
        public static final String COLUMN_CARD_SETFIRE = "setfire";
        public static final String COLUMN_CARD_ARMORBREAK = "armorbreak";


        public static Uri getContentUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
