package com.kevin.android.cardgamev1.tools;


import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.kevin.android.cardgamev1.blueprintclasses.Card;
import com.kevin.android.cardgamev1.Constants;
import com.kevin.android.cardgamev1.database.CardContract;
import com.kevin.android.cardgamev1.database.DeckContract;
import com.kevin.android.cardgamev1.decks.CardNameAndCount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Converter {

    private static ContentResolver mContentResolver;
    private static Context mContext;


    public static ArrayList<CardNameAndCount> importCards(String jsonString) {
        ArrayList<CardNameAndCount> cardList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray;
            jsonArray = jsonObject.getJSONArray("cardlist");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                CardNameAndCount card = new CardNameAndCount();
                card.setCardId(Long.parseLong(jsonObject.getString("id")));
                card.setCardCount(Integer.parseInt(jsonObject.getString("count")));
                card.setCardName(jsonObject.getString("name"));
                cardList.add(card);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cardList;
    }

    public static String exportDeckToJSON(ArrayList<CardNameAndCount> cards) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < cards.size(); i++) {
            jsonObject = new JSONObject();
            try {
                jsonObject.put("id", cards.get(i).getCardId());
                jsonObject.put("count", Long.toString(cards.get(i).getCardCount()));
                jsonObject.put("name", cards.get(i).getCardName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        JSONObject finishedObject = new JSONObject();
        try {
            finishedObject.put("cardlist", jsonArray);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return finishedObject.toString();
    }

    /*
    public static void exportDeckToDb(String deckName, String championType, ArrayList<String> cardList, Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(exportDeckToJSON(cardList));
            values.put(DeckContract.DeckEntry.COLUMN_DECK_CARDLIST, jsonObject.toString());
            values.put(DeckContract.DeckEntry.COLUMN_DECK_DECKNAME, deckName);
            values.put(DeckContract.DeckEntry.COLUMN_DECK_CHAMPION, championType);

        } catch (JSONException e){
            e.printStackTrace();
        }



        Uri uri = null;
        try {
            uri = mContentResolver.insert(DeckContract.DeckEntry.CONTENT_URI, values);
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    } */


/*
    public Card importCardFromSQL(Uri cardId, Context context) {
        Card cardFromDb;
        mContext = context;
        ContentResolver contentResolver = mContext.getContentResolver();
        ContentValues = contentResolver.query(

                cardId)
        String cardName;
        int attackValue;
        int lifeValue;
        int manaCost;
        Constants.CardTypes cardType;
        int defenseValue;
        boolean hasOverPower;
        boolean hasDefender;
        boolean hasRanged;
        boolean hasAreaAttack;
        boolean hasLightningReflexes;
        boolean hasConcealed;
        boolean hasPillage;
        boolean hasBattleReady;
        boolean hasRally;
        boolean hasBluff;
        boolean hasUnstable;
        boolean hasFiredUp;
        boolean hasDrawCard;
        boolean hasWarmBlooded;
        boolean hasLastWord;
        boolean hasQuick;
        boolean hasAbsorb;
        boolean hasSetFire;


    }   */

    public class cardImporter implements LoaderManager.LoaderCallbacks<Cursor> {
        Uri mCurrentCardUri;
        Card cardToImport;
        Context mContext;
        String[] cards;
        ArrayList<String> cardIds = new ArrayList<>();
        ArrayList<Card> fillList = new ArrayList<>();
        private final int CARD_URI = 11;


        public cardImporter(Context context, ArrayList<String> cardList ) {
            mContext = context;
            cardIds = cardList;

        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    mContext,
                    null,
                    null,
                    CardContract.CardEntry._ID + "=?",
                    cards,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
            do {

                String name = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_NAME));
                String image = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_IMAGE));
                final Uri imageUri = Uri.parse(image);

                String champion = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_CHAMPION));

                int life = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_LIFE));
                int manacost = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_MANACOST));
                int attack = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_ATTACK));
                int defense = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_DEFENSE));

                int quickInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_QUICK));
                int overpowerInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_OVERPOWER));
                int defenderInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_DEFENDER));
                int rangedInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_RANGED));
                int areaattackInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_AREAATTACK));
                int lightningReflexesInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_LIGHTNINGREFLEXES));
                int concealedInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_CONCEALED));
                int pillageInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_PILLAGE));
                int battlereadyInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_BATTLEREADY));
                int rallyInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_RALLY));
                int bluffInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_BLUFF));
                int unstableInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_UNSTABLE));
                int firedupInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_FIREDUP));
                int drawcardInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_DRAWCARD));
                int warmbloodedInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_WARMBLOODED));
                int lastwordInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_LASTWORD));
                int setfireInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_SETFIRE));
                int armorbreakInt = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_ARMORBREAK));
                cardToImport.setCardName(name);
                cardToImport.setImageId(image);
                cardToImport.setChampionName(champion);
                cardToImport.setLifeValue(life);
                cardToImport.setManaCost(manacost);
                cardToImport.setAttackValue(attack);
                cardToImport.setDefenseValue(defense);
                cardToImport.setHasQuick(intToBoolean(quickInt));
                cardToImport.setHasOverPower(intToBoolean(overpowerInt));
                cardToImport.setHasDefender(intToBoolean(defenderInt));
                cardToImport.setHasRanged(intToBoolean(rangedInt));
                cardToImport.setHasAreaAttack(intToBoolean(areaattackInt));
                cardToImport.setHasLightningReflexes(intToBoolean(lightningReflexesInt));
                cardToImport.setHasConcealed(intToBoolean(concealedInt));
                cardToImport.setHasPillage(intToBoolean(pillageInt));
                cardToImport.setHasBattleReady(intToBoolean(battlereadyInt));
                cardToImport.setHasRally(intToBoolean(rallyInt));
                cardToImport.setHasBluff(intToBoolean(bluffInt));
                cardToImport.setHasUnstable(intToBoolean(unstableInt));
                cardToImport.setHasFiredUp(intToBoolean(firedupInt));
                cardToImport.setHasDrawCard(intToBoolean(drawcardInt));
                cardToImport.setHasWarmBlooded(intToBoolean(warmbloodedInt));
                cardToImport.setHasLastWord(intToBoolean(lastwordInt));
                cardToImport.setHasSetFire(intToBoolean(setfireInt));
                cardToImport.setHasArmorBreak(intToBoolean(armorbreakInt));

                fillList.add(cardToImport);
            } while (cursor.moveToNext());
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            loader.reset();
        }

        private Boolean intToBoolean(int value){
            return value == 1;
        }

    }


        public void exportCard(String cardName,
                               int cardId,
                               int attackValue,
                               int lifeValue,
                               int manaCost,
                               Constants.CardTypes cardType,
                               int defenseValue,
                               boolean hasOverPower,
                               boolean hasDefender,
                               boolean hasRanged,
                               boolean hasAreaAttack,
                               boolean hasLightningReflexes,
                               boolean hasConcealed,
                               boolean hasPillage,
                               boolean hasBattleReady,
                               boolean hasRally,
                               boolean hasBluff,
                               boolean hasUnstable,
                               boolean hasFiredUp,
                               boolean hasDrawCard,
                               boolean hasWarmBlooded,
                               boolean hasLastWord,
                               boolean hasQuick,
                               boolean hasAbsorb,
                               boolean hasSetFire) {


            JSONObject jsonObject = new JSONObject();
            JSONArray cardJsonArray = new JSONArray();


        }

    }

