package com.kevin.android.cardgamev1.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kevin.android.cardgamev1.R;

import static com.kevin.android.cardgamev1.database.CardContract.CONTENT_AUTHORITY;
import static com.kevin.android.cardgamev1.database.CardContract.PATH_CARD;
import static com.kevin.android.cardgamev1.database.ChampionContract.PATH_CHAMPION;
import static com.kevin.android.cardgamev1.database.DeckContract.PATH_DECK;


public class GameProvider extends ContentProvider{

    private CardDBHelper cardDBHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CARD_ALL = 100;
    private static final int CARD_ID = 101;
    private static final int DECK_ALL = 102;
    private static final int DECK_ID = 103;
    private static final int CHAMPION_ALL = 104;
    private static final int CHAMPION_ID = 105;

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_CARD, CARD_ALL);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_CARD + "/#", CARD_ID);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_DECK, DECK_ALL);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_DECK + "/#", DECK_ID);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_CHAMPION, CHAMPION_ALL);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_CHAMPION + "/#", CHAMPION_ID);
    }
    @Override
    public boolean onCreate() {
        cardDBHelper = new CardDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = cardDBHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case CARD_ALL:
                // MIGHT ADD CHAMPION TO SECOND NULL HERE
                cursor = database.query(CardContract.CardEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case CARD_ID:
                selection = CardContract.CardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // MIGHT ADD CHAMPION TO SECOND NULL HERE
                cursor = database.query(CardContract.CardEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case DECK_ALL:
                cursor = database.query(DeckContract.DeckEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case DECK_ID:
                selection = DeckContract.DeckEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DeckContract.DeckEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            case CHAMPION_ALL:
                cursor = database.query(ChampionContract.ChampionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case CHAMPION_ID:
                selection = ChampionContract.ChampionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ChampionContract.ChampionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.illegal_argument));

        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CARD_ALL:
                return CardContract.CardEntry.CONTENT_LIST_TYPE;
            case CARD_ID:
                return CardContract.CardEntry.CONTENT_ITEM_TYPE;
            case DECK_ALL:
                return DeckContract.DeckEntry.CONTENT_LIST_TYPE;
            case DECK_ID:
                return DeckContract.DeckEntry.CONTENT_ITEM_TYPE;
            case CHAMPION_ALL:
                return ChampionContract.ChampionEntry.CONTENT_LIST_TYPE;
            case CHAMPION_ID:
                return ChampionContract.ChampionEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException(getContext().getString(R.string.unknown_uri) + uri + getContext().getString(R.string.with_match) + match);
                    }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CARD_ALL:
                return insertCard(uri, contentValues);
            case DECK_ALL:
                return insertDeck(uri, contentValues);
            case CHAMPION_ALL:
                return insertChampion(uri, contentValues);
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.illegal_argument));
        }
    }

    private Uri insertChampion(Uri uri, ContentValues contentValues){
        SQLiteDatabase database = cardDBHelper.getWritableDatabase();
        String championName = contentValues.getAsString(ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME);
        String championImage = contentValues.getAsString(ChampionContract.ChampionEntry.COLUMN_CHAMPION_IMAGE);
        String championText = contentValues.getAsString(ChampionContract.ChampionEntry.COLUMN_CHAMPION_TEXT);
        String championAbility = contentValues.getAsString(ChampionContract.ChampionEntry.COLUMN_CHAMPION_ABILITY);

        if (championName == null || championImage == null || championAbility == null){
            throw new IllegalArgumentException(getContext().getString(R.string.illegal_argument));
        }
        long id = database.insert(ChampionContract.ChampionEntry.TABLE_NAME, null, contentValues);
        if (id == -1){
            Toast.makeText(getContext(), R.string.insert_failed, Toast.LENGTH_SHORT).show();
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertDeck (Uri uri, ContentValues contentValues){
        SQLiteDatabase database = cardDBHelper.getWritableDatabase();
        String name = contentValues.getAsString(DeckContract.DeckEntry.COLUMN_DECK_DECKNAME);
        String championType = contentValues.getAsString(DeckContract.DeckEntry.COLUMN_DECK_CHAMPION);
        String image = contentValues.getAsString(DeckContract.DeckEntry.COLUMN_DECK_IMAGE);
        String deckUses = contentValues.getAsString(DeckContract.DeckEntry.COLUMN_DECK_USES);
        String cardList = contentValues.getAsString(DeckContract.DeckEntry.COLUMN_DECK_CARDLIST);

        if (name == null || championType == null || image == null || deckUses == null || cardList == null){
            throw new IllegalArgumentException(getContext().getString(R.string.illegal_argument));
        }
        long id = database.insert(DeckContract.DeckEntry.TABLE_NAME, null, contentValues);
        if (id == -1){
            Toast.makeText(getContext(), R.string.insert_failed, Toast.LENGTH_SHORT).show();
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertCard (Uri uri, ContentValues contentValues){
        SQLiteDatabase database = cardDBHelper.getWritableDatabase();
        String name = contentValues.getAsString(CardContract.CardEntry.COLUMN_CARD_NAME);
        Integer life = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_LIFE);
        Integer manaCost = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_MANACOST);
        Integer attack = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_ATTACK);
        Integer defense = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_DEFENSE);
        String image = contentValues.getAsString(CardContract.CardEntry.COLUMN_CARD_IMAGE);

        Integer quick = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_QUICK);
        Integer  overpower = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_OVERPOWER);
        Integer  defender = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_DEFENDER);
        Integer  ranged = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_RANGED);
        Integer areaattack = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_AREAATTACK);
        Integer  lightningreflexes = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_LIGHTNINGREFLEXES);
        Integer  concealed = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_CONCEALED);
        Integer  pillage = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_PILLAGE);
        Integer  battleready = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_BATTLEREADY);
        Integer  rally = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_RALLY);
        Integer  bluff = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_BLUFF);
        Integer  unstable = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_UNSTABLE);
        Integer  firedup = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_FIREDUP);
        Integer  drawcard = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_DRAWCARD);
        Integer  warmblooded = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_WARMBLOODED);
        Integer  lastword = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_LASTWORD);
        Integer  setfire = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_SETFIRE);
        Integer armorbreak = contentValues.getAsInteger(CardContract.CardEntry.COLUMN_CARD_ARMORBREAK);

        if (name == null || manaCost == null || attack == null || life == null || defense == null || image == null || quick == null
                || overpower == null || defender == null || ranged == null || areaattack == null || lightningreflexes == null || concealed
                == null || pillage == null || battleready == null || rally == null || bluff == null || unstable == null || firedup == null || drawcard
                == null || warmblooded == null || lastword == null || setfire == null || armorbreak == null){
            throw new IllegalArgumentException(getContext().getString(R.string.illegal_argument));
        }
        long id = database.insert(CardContract.CardEntry.TABLE_NAME, null, contentValues);
        if (id == -1){
            Toast.makeText(getContext(), R.string.insert_failed, Toast.LENGTH_SHORT).show();
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = cardDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CARD_ALL:
                return database.delete(CardContract.CardEntry.TABLE_NAME, selection, selectionArgs);
            case CARD_ID:
                selection = CardContract.CardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(CardContract.CardEntry.TABLE_NAME, selection, selectionArgs);
            case DECK_ALL:
                return database.delete(DeckContract.DeckEntry.TABLE_NAME, selection, selectionArgs);
            case DECK_ID:
                selection = DeckContract.DeckEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(DeckContract.DeckEntry.TABLE_NAME, selection, selectionArgs);
            case CHAMPION_ALL:
                return database.delete(ChampionContract.ChampionEntry.TABLE_NAME, selection, selectionArgs);
            case CHAMPION_ID:
                selection = ChampionContract.ChampionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(ChampionContract.ChampionEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.deletion_failure) + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CARD_ALL:
                return updateCardItem(uri, contentValues, selection, selectionArgs);
            case CARD_ID:
                selection = CardContract.CardEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCardItem(uri, contentValues, selection, selectionArgs);
            case DECK_ALL:
                return updateDeckItem(uri, contentValues, selection, selectionArgs);
            case DECK_ID:
                selection = DeckContract.DeckEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateDeckItem(uri, contentValues, selection, selectionArgs);
            case CHAMPION_ALL:
                return updateChampionItem(uri, contentValues, selection, selectionArgs);
            case CHAMPION_ID:
                selection = ChampionContract.ChampionEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateChampionItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.update_failed));
        }

    }
    private int updateChampionItem(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = cardDBHelper.getWritableDatabase();
        if (values.size() == 0){
            return 0;
        }
        int rows = database.update(ChampionContract.ChampionEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    private int updateCardItem(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = cardDBHelper.getWritableDatabase();
        if (values.size() == 0){
            return 0;
        }
        int rows = database.update(CardContract.CardEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }
    private int updateDeckItem(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = cardDBHelper.getWritableDatabase();
        if (values.size() == 0){
            return 0;
        }
        int rows = database.update(DeckContract.DeckEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }
}
