package com.kevin.android.cardgamev1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardDBHelper extends SQLiteOpenHelper{


    private static CardDBHelper mInstance = null;

    public final static String DATABASE_NAME = "card.db";
    public final static int DATABASE_VERSION = 2;

    public static CardDBHelper getInstance (Context context){
        if (mInstance == null) {
            mInstance = new CardDBHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public CardDBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    String SQL_CREATE_CARD_TABLE = "CREATE TABLE " + CardContract.CardEntry.TABLE_NAME + " ("
            + CardContract.CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CardContract.CardEntry.COLUMN_CARD_NAME + " TEXT NOT NULL, "
            + CardContract.CardEntry.COLUMN_CARD_TYPE + " TEXT NOT NULL CHECK (" + CardContract.CardEntry.COLUMN_CARD_TYPE + " IN (\"CREATURE\", \"SPELL\")), "
            + CardContract.CardEntry.COLUMN_CARD_IMAGE + " BLOB NOT NULL, "
            + CardContract.CardEntry.COLUMN_CARD_LIFE + " INTEGER DEFAULT 0, "
            + CardContract.CardEntry.COLUMN_CARD_ATTACK + " INTEGER DEFAULT 0, "
            + CardContract.CardEntry.COLUMN_CARD_DEFENSE + " INTEGER DEFAULT 0, "
            + CardContract.CardEntry.COLUMN_CARD_MANACOST + " INTEGER DEFAULT 0, "
            + CardContract.CardEntry.COLUMN_CARD_CHAMPION + " TEXT DEFAULT \"NONE\", "
            + CardContract.CardEntry.COLUMN_CARD_QUICK + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_QUICK + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_OVERPOWER + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_OVERPOWER + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_DEFENDER + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_DEFENDER + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_RANGED + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_RANGED + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_AREAATTACK + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_AREAATTACK + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_LIGHTNINGREFLEXES + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_LIGHTNINGREFLEXES + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_CONCEALED + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_CONCEALED + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_PILLAGE + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_PILLAGE + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_BATTLEREADY + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_BATTLEREADY + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_RALLY + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_RALLY + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_BLUFF + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_BLUFF + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_UNSTABLE + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_UNSTABLE + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_FIREDUP + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_FIREDUP + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_DRAWCARD + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_DRAWCARD + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_WARMBLOODED + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_WARMBLOODED + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_LASTWORD + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_LASTWORD + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_SETFIRE + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_SETFIRE + " IN (0,1)), "
            + CardContract.CardEntry.COLUMN_CARD_ARMORBREAK + " NOT NULL DEFAULT 0 CHECK (" + CardContract.CardEntry.COLUMN_CARD_ARMORBREAK + " IN (0,1))"
            + ");";
    String SQL_CREATE_DECK_TABLE = "CREATE TABLE " + DeckContract.DeckEntry.TABLE_NAME + " ("
            + DeckContract.DeckEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DeckContract.DeckEntry.COLUMN_DECK_DECKNAME + " STRING NOT NULL, "
            + DeckContract.DeckEntry.COLUMN_DECK_CHAMPION + " STRING NOT NULL, "
            + DeckContract.DeckEntry.COLUMN_DECK_IMAGE + " BLOB NOT NULL, "
            + DeckContract.DeckEntry.COLUMN_DECK_USES + " INTEGER NOT NULL DEFAULT 0, "
            + DeckContract.DeckEntry.COLUMN_DECK_CARDLIST + " STRING NOT NULL"
            + ");";
    String SQL_CREATE_CHAMPION_TABLE = "CREATE TABLE " + ChampionContract.ChampionEntry.TABLE_NAME + " ("
            + ChampionContract.ChampionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME + " STRING NOT NULL, "
            + ChampionContract.ChampionEntry.COLUMN_CHAMPION_IMAGE + " BLOB NOT NULL, "
            + ChampionContract.ChampionEntry.COLUMN_CHAMPION_TEXT + " STRING, "
            + ChampionContract.ChampionEntry.COLUMN_CHAMPION_ABILITY + " STRING NOT NULL"
            + ");";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CARD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DECK_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHAMPION_TABLE);
    }


    public void deleteDatabase (Context context){
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CardContract.CardEntry.TABLE_NAME );
    onCreate(sqLiteDatabase);
    }
}
