package com.kevin.android.cardgamev1.decks;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.database.CardContract;
import com.kevin.android.cardgamev1.database.ChampionContract;
import com.kevin.android.cardgamev1.database.DeckContract;
import com.kevin.android.cardgamev1.tools.Converter;
import com.kevin.android.cardgamev1.tools.Toasts;

import java.util.ArrayList;

public class DeckBuilder extends AppCompatActivity {

    String championType;
    ImageView championImage;
    GridView cardGrid;
    ListView cardList;
    RadioGroup radioGroup;
    RadioButton radioChampionButton;
    RadioButton radioUniversalButton;
    Button saveButton;
    Button deleteButton;
    EditText deckNameEditText;
    CardsAvailableAdapter cardsAvailableAdapter;
    CardsInDeckAdapter cardsInDeckAdapter;
    ArrayList<CardNameAndCount> cardsInDeckList;
    int championInList = 1;
    Context mContext;
    String[] championList;
    String[] cardNameLookup;
    String[] cardListOfIds;
    String cardNameFiller;
    String championImageUri;
    Uri mCurrentDeckUri;
    private final int DECK_LOADER = 7;
    private final int CARD_LOADER = 8;
    private final int CARD_IN_PREMADE_DECK_LOADER = 123;
    private final int CHAMPION_IMAGE_LOADER = 151;
    private final int MAXIMUM_CARD_COUNT = 2;
    private final int MAXIMUM_DECK_SIZE = 30;
    private int DECK_USES = 0;
    int query_count = 0;
    private boolean mItemChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_builder);
        mContext = this;
        setUI();
        Intent intent = getIntent();
        mCurrentDeckUri = intent.getData();
        setCardsInDeckAdapter();
        if (mCurrentDeckUri != null) {
            getLoaderManager().initLoader(DECK_LOADER, null, new DeckLoaderCallbacks());
        } else {

            championType = intent.getStringExtra("Name");
            championList = new String[]{"Universal", championType};
            getLoaderManager().initLoader(CHAMPION_IMAGE_LOADER, null, new ChampionImageCallBacks());
            getLoaderManager().initLoader(CARD_LOADER, null, new CardLoaderCallBacks());
        }
        cardNameLookup = new String[]{"idgoeshere"};
        cardsAvailableAdapter = new CardsAvailableAdapter(this, null);
        cardGrid.setAdapter(cardsAvailableAdapter);
        cardGrid.setOnItemClickListener(onItemClickListener);
        cardList.setOnItemClickListener(onItemClickListener);


    }

    private void setCardsInDeckAdapter (){
        cardsInDeckList = new ArrayList<>();
        cardsInDeckAdapter = new CardsInDeckAdapter(this, cardsInDeckList);
        cardList.setAdapter(cardsInDeckAdapter);
    }


    private void saveDeck(){
        if (!deckNameEditText.getText().toString().equals("")  && cardsInDeckList.size() > 0){
            ContentValues values = new ContentValues();
            values.put(DeckContract.DeckEntry.COLUMN_DECK_DECKNAME, deckNameEditText.getText().toString());
            values.put(DeckContract.DeckEntry.COLUMN_DECK_IMAGE, championImageUri);
            values.put(DeckContract.DeckEntry.COLUMN_DECK_CARDLIST, Converter.exportDeckToJSON(cardsInDeckList));
            values.put(DeckContract.DeckEntry.COLUMN_DECK_CHAMPION, championType);
            values.put(DeckContract.DeckEntry.COLUMN_DECK_USES, DECK_USES);

            if (mCurrentDeckUri == null) {
                Uri uri = null;
                try{
                    uri = getContentResolver().insert(DeckContract.DeckEntry.CONTENT_URI, values);
                } catch (IllegalArgumentException e){
                    Toasts.toastFailure(this);
                }
                if (uri != null){
                    Toasts.toastSuccess(this);
                    finish();
                }
            } else {
                getContentResolver().update(mCurrentDeckUri, values, null, null);
                Toasts.toastSuccess(this);
                finish();
            }
        }
    }

    private boolean validateFields() {
        return cardsInDeckList.size() > 0 && !deckNameEditText.getText().toString().equals("");
    }


    // updated converter tool
    private ArrayList<String> retrieveIds(){
        ArrayList<String> cardList = new ArrayList<>();
        for (int i = 0; i < cardsInDeckList.size(); i++){
            cardList.add(Long.toString(cardsInDeckList.get(i).getCardId()));
            if (cardsInDeckList.get(i).getCardCount() > 1){
                for (int o = 1; o < cardsInDeckList.get(i).getCardCount(); o++){
                    cardList.add(Long.toString(cardsInDeckList.get(i).getCardId()));
                }
            }
        }
        return cardList;
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

            switch (parent.getId()) {
                case R.id.grid_card_selector:
                    moveToDeck();
                    break;
                case R.id.deck_creator_in_deck_listview:
                    removeFromDeck(position);
                    break;
            }
        }
    };

    private void removeFromDeck(int position) {
        CardNameAndCount card = cardsInDeckAdapter.getItem(position);
        if (card.getCardCount() == MAXIMUM_CARD_COUNT || card.getCardCount() > 1) {
            card.setCardCount(card.getCardCount() - 1);
            cardsInDeckAdapter.notifyDataSetChanged();
        } else if (card.getCardCount() == 1) {
            cardsInDeckList.remove(position);
            cardsInDeckAdapter.notifyDataSetChanged();
        }
    }


    // creates cardnameandcount object from card adapter & moves it to the in deck array if there are 0 of it, if there is 1 already there it adds a count
    private void moveToDeck() {
        CardNameAndCount card = new CardNameAndCount();
        Cursor cursor = cardsAvailableAdapter.getCursor();
        card.setCardName(cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_NAME)));
        card.setCardId(cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry._ID)));

        if (!checkDeck(card) && checkDeckSizeMax()) {
            card.setCardCount(1);
            cardsInDeckList.add(card);
            cardsInDeckAdapter.notifyDataSetChanged();
        } else if (checkMax(card) && checkDeckSizeMax()) {
            addExistingCard(card);
            cardsInDeckAdapter.notifyDataSetChanged();
        }
    }

    // checks the in deck array for an instance of the card, returns how many are already in the array, if # usable ever goes to 3, this needs to be changed to count cardCount
    private boolean checkDeck(CardNameAndCount cardNameAndCount) {
        for (int i = 0; i < cardsInDeckList.size(); i++) {
            if (cardNameAndCount.getCardId() == cardsInDeckList.get(i).getCardId()) {
             return true;
            }
        }
        return false;
    }

    // returns true if the # of cards in the deck list is less than the maximum allowed
    private boolean checkMax(CardNameAndCount cardNameAndCount) {
        for (int i = 0; i < cardsInDeckList.size(); i++) {
            if (cardNameAndCount.getCardId() == cardsInDeckList.get(i).getCardId() && cardsInDeckList.get(i).getCardCount() < MAXIMUM_CARD_COUNT) {
                return true;
            }
        }
        return false;
    }
    // true if there are less cards in the deck list than the maximum allowed
    private boolean checkDeckSizeMax(){
        return cardsInDeckList.size() < MAXIMUM_DECK_SIZE;
    }

    // adds +1 to card count for a card already in the in deck array
    private void addExistingCard(CardNameAndCount cardNameAndCount) {
        int count;
        for (int i = 0; i < cardsInDeckList.size(); i++) {
            if (cardNameAndCount.getCardId() == cardsInDeckList.get(i).getCardId()) {
                count = cardsInDeckList.get(i).getCardCount();
                cardsInDeckList.get(i).setCardCount(count + 1);
            }
        }
    }

    // link the UI ids
    private void setUI() {
        cardGrid = (GridView) findViewById(R.id.grid_card_selector);
        cardList = (ListView) findViewById(R.id.deck_creator_in_deck_listview);
        championImage = (ImageView)findViewById(R.id.deck_creator_champion_image);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group_deck_builder);
        radioChampionButton = (RadioButton) findViewById(R.id.radio_button_champion);
        radioUniversalButton = (RadioButton) findViewById(R.id.radio_button_universal);
        saveButton = (Button) findViewById(R.id.deck_creator_save_button);
        deckNameEditText = (EditText) findViewById(R.id.deck_creator_name);
        radioChampionButton.setChecked(true);
        deckNameEditText.setOnTouchListener(mTouchListener);
        cardGrid.setOnTouchListener(mTouchListener);
        cardList.setOnTouchListener(mTouchListener);
        deleteButton = (Button)findViewById(R.id.deck_creator_delete_button);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id) {
                    case R.id.radio_button_universal:
                        championInList = 0;
                        getLoaderManager().restartLoader(CARD_LOADER, null, new CardLoaderCallBacks());
                        break;
                    case R.id.radio_button_champion:
                        championInList = 1;
                        getLoaderManager().restartLoader(CARD_LOADER, null, new CardLoaderCallBacks());
                        break;
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()){
                    Log.v("VALIDATE", "SUCCESS");
                    saveDeck();
                } else {
                    Toasts.toastValidationFail(mContext);
                }
            }
        });
    }



    // gets the champion string[] for the SQLite query to get cards for the correct champion
    private String[] fetchChampion(int championType) {
        return new String[]{championList[championType]};
    }


// get cards of type universal && cards of type of selected champion


    private class CardLoaderCallBacks implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    mContext,
                    CardContract.CardEntry.CONTENT_URI,
                    null,
                    CardContract.CardEntry.COLUMN_CARD_CHAMPION + "=?",
                    fetchChampion(championInList),
                    CardContract.CardEntry.COLUMN_CARD_MANACOST
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null) {
                cardsAvailableAdapter.swapCursor(cursor);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            cardsAvailableAdapter.swapCursor(null);
        }
    }

    private class DeckLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    mContext,
                    mCurrentDeckUri,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
                String deckName = cursor.getString(cursor.getColumnIndex(DeckContract.DeckEntry.COLUMN_DECK_DECKNAME));
                championType = cursor.getString(cursor.getColumnIndex(DeckContract.DeckEntry.COLUMN_DECK_CHAMPION));
                championList = new String[]{"Universal", championType};
                getLoaderManager().initLoader(CARD_LOADER, null, new CardLoaderCallBacks());
                deckNameEditText.setText(deckName);
                String cardIds = cursor.getString(cursor.getColumnIndex(DeckContract.DeckEntry.COLUMN_DECK_CARDLIST));
                DECK_USES = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DeckContract.DeckEntry.COLUMN_DECK_USES)));
                championImageUri = cursor.getString(cursor.getColumnIndex(DeckContract.DeckEntry.COLUMN_DECK_IMAGE));
                championImage.setImageURI(Uri.parse(championImageUri));
           //     String[] projection = new String[]{CardContract.CardEntry.COLUMN_CARD_NAME};

                ArrayList<CardNameAndCount> cards = Converter.importCards(cardIds);

                for (int j = 0; j < cards.size(); j++){
                    cardsInDeckList.add(cards.get(j));
                }
                // cardsInDeckList = cards;
                cardsInDeckAdapter.notifyDataSetChanged();

                for (int z = 0; z < cardsInDeckList.size(); z++){
                    Log.v("CARDNAMES", cardsInDeckList.get(z).getCardName());
                    Log.v("CARDID", Long.toString(cardsInDeckList.get(z).getCardId()));
                }


                // retrieve card names with loader
                /*
                String[] cards = new String[cardListFromJson.size()];
                for (int g = 0; g < cardListFromJson.size(); g++){
                    cards[g] = Long.toString(cardListFromJson.get(g).getCardId());
                }
                 cardListOfIds = cards;
                query_count = cardListOfIds.length;
                getLoaderManager().initLoader(CARD_IN_PREMADE_DECK_LOADER, null, new CardNameLoaderCallbacks());
*/

             //   ArrayList<CardNameAndCount> fillList = new ArrayList<>();


/*
                for (int i = 0; i < cardListFromJson.size(); i++) {
                    int cardId = Integer.parseInt(cardListFromJson.get(i));
                    CardNameAndCount card = new CardNameAndCount();
                    card.setCardId(cardId);
                    if (checkImportDeck(card, fillList) >= 1) {
                        fillList.get(i).setCardCount(fillList.get(i).getCardCount() + 1);
                    } else {
                        cardNameLookup[0] = cardListFromJson.get(i);
                        Cursor cursor1 = getContentResolver().query(
                                CardContract.CardEntry.CONTENT_URI,
                                projection,
                                CardContract.CardEntry._ID + "=?",
                                cardNameLookup,
                                null,
                                null
                        );
                        if (cursor1.moveToFirst()) {
                            card.setCardName(cursor1.getString(cursor1.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_NAME)));
                            card.setCardCount(1);
                            cursor1.close();
                        }
                    }
                }
                cardsInDeckList = new ArrayList<>(fillList);
                cardsInDeckAdapter = new CardsInDeckAdapter(mContext, cardsInDeckList);
                cardList.setAdapter(cardsInDeckAdapter);
                cardsInDeckAdapter.notifyDataSetChanged();
                */
            }


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            loader.reset();
        }
    }

    private int checkImportDeck(CardNameAndCount cardNameAndCount, ArrayList<CardNameAndCount> cards) {
        int instances = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cardNameAndCount.getCardId() == cards.get(i).getCardId()) {
                instances++;
            }
        }
        return instances;
    }

    private class ChampionImageCallBacks implements LoaderManager.LoaderCallbacks<Cursor>{
        String[] projection1 = new String[]{ChampionContract.ChampionEntry.COLUMN_CHAMPION_IMAGE};
        String[] selectionArgs = new String[]{championType};
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
               mContext,
                    ChampionContract.ChampionEntry.CONTENT_URI,
                    projection1,
                    ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME + "=?",
                    selectionArgs,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
if (cursor.moveToFirst()){
    championImageUri = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_IMAGE));
    championImage.setImageURI(Uri.parse(championImageUri));
}
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
loader.reset();
        }
    }

    // get card names from DB using a query with their ID numbers
    private class CardNameLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = new String[]{CardContract.CardEntry.COLUMN_CARD_NAME, CardContract.CardEntry._ID};
            StringBuilder stringBuilder = new StringBuilder();
            for (int g = 0; g < (query_count - 1); g++ ){
                stringBuilder.append(",?");
            }
            String finalQuery = stringBuilder.toString();

            return new CursorLoader(
                    mContext,
                    CardContract.CardEntry.CONTENT_URI,
                    projection,
                    CardContract.CardEntry._ID + " IN(?" + finalQuery + ")", // HERE__!_#@$)
                    cardListOfIds,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
          if (cursor.moveToFirst()){
              do {
                  String nameOfCard = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_NAME));
                  long cardId = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry._ID));
                  CardNameAndCount card = new CardNameAndCount(nameOfCard, cardId);
                  if (!checkDeck(card) && checkDeckSizeMax()) {
                      card.setCardCount(1);
                      cardsInDeckList.add(card);
                  } else if (checkMax(card) && checkDeckSizeMax()) {
                      addExistingCard(card);
                  }
              } while (cursor.moveToNext());
              cardsInDeckAdapter.notifyDataSetChanged();
          }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            loader.reset();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mItemChanged) {
            super.onBackPressed();
            return;
        }
        showUnsavedChangesDialog();
    }

    private void showUnsavedChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_and_quit);
        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    finish();
                }
            }
        });
        builder.setNegativeButton(R.string.continue_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };

    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (mCurrentDeckUri != null) {
            builder.setMessage(R.string.delete_message);
        } else {
            builder.setMessage(R.string.delete_progress);
        }
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    if (mCurrentDeckUri != null) {
                        ContentResolver contentResolver = getContentResolver();
                        contentResolver.delete(mCurrentDeckUri, null, null);
                        contentResolver.notifyChange(mCurrentDeckUri, null);
                        finish();
                    } else {
                        deckNameEditText.setText(null);
                        cardsInDeckList.clear();
                    }
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.continue_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemChanged = true;
            return false;
        }
    };
}
