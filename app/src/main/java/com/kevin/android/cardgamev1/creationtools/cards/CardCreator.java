package com.kevin.android.cardgamev1.creationtools.cards;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;

import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kevin.android.cardgamev1.Constants;
import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.blueprintclasses.Card;
import com.kevin.android.cardgamev1.CardToSave;
import com.kevin.android.cardgamev1.database.CardContract;
import com.kevin.android.cardgamev1.database.ChampionContract;
import com.kevin.android.cardgamev1.tools.Toasts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// implements LoaderManager.LoaderCallbacks<Cursor>
public class CardCreator extends AppCompatActivity {

    String TAG = "LOG ! ! ! ! ! ! !";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    // inserts objects into cards array in the firebase database
    // cardRef.setValue("Hello, World")
    DatabaseReference cardRef = database.getReference("cards");

    EditText cardNameEditText;
    EditText cardDescriptionEditText;
    TextView cardManaCost;
    TextView cardLife;
    TextView cardAttack;
    TextView cardDefense;

    Spinner championSpinner;

    Button saveButton;
    Button deleteButton;
    Button imageButton;
    ImageView imageView;
    ImageView championImage;
    Button lifePlus;
    Button lifeMinus;
    Button attackPlus;
    Button attackMinus;
    Button defensePlus;
    Button defenseMinus;


    Switch quickSwitch;
    Switch overpowerSwitch;
    Switch defenderSwitch;
    Switch rangedSwitch;
    Switch areaattackSwitch;
    Switch lightningreflexesSwitch;
    Switch concealedSwitch;
    Switch pillageSwitch;
    Switch battlereadySwitch;
    Switch rallySwitch;
    Switch bluffSwitch;
    Switch unstableSwitch;
    Switch firedupSwitch;
    Switch drawcardSwitch;
    Switch warmbloodedSwitch;
    Switch lastwordSwitch;
    Switch setfireSwitch;
    Switch armorbreakSwitch;

    protected int attackValue;
    protected int lifeValue;
    protected int manaValue;
    protected int defenseValue;

    ArrayAdapter<String> championAdapter;

    ScrollView creatureScrollView;
    ScrollView spellScrollView;

    ArrayList<Boolean> powerList;
    ArrayList<String> championList;

    protected boolean quick = false;
    protected boolean overpower = false;
    protected boolean defender = false;
    protected boolean ranged = false;
    protected boolean areaattack = false;
    protected boolean lightningreflexes = false;
    protected boolean concealed = false;
    protected boolean pillage = false;
    protected boolean battleready = false;
    protected boolean rally = false;
    protected boolean bluff = false;
    protected boolean unstable = false;
    protected boolean firedup = false;
    protected boolean drawcard = false;
    protected boolean warmblooded = false;
    protected boolean lastword = false;
    protected boolean setfire = false;
    protected boolean armorbreak = false;

    private boolean mItemChanged = false;
    private Uri mCurrentCardUri;
    private Uri mImageUri;
    private Uri mChampionUri;
    private boolean imageSet = false;
    private boolean changeMade = false;
    private ContentResolver mContentResolver;
    private final int EDIT_LOADER = 3;
    private final int CHAMPION_LOADER = 19;
    private final int ID_LOADER = 123;
    private long cardId;
    private String champion;
    private Bitmap cardBitmap;


    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int CHOOSE_IMAGE_REQUEST = 2;
    Context mContext;

    private CardToSave incomingCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card__creator);
        mContentResolver = this.getContentResolver();
        powerList = new ArrayList<>();
        // SQL method
        // getCardStatsOrNewCard();
        checkIfNewCardOrEdit();
        championList = new ArrayList<>();
        championList.add(getString(R.string.universal));
        setUi();
        setButtons();
        mContext = this;

        //SQL method
        //TODO change this to FB
        getLoaderManager().initLoader(CHAMPION_LOADER, null, new ChampionLoaderCallbacks());
    }

    // determines mana cost, needs a lot of work!
    private int initiateBlackBox() {
        int attack = getAttackValue();
        int life = getLifeValue();
        int defense = getDefenseValue();
        double powerLevel;
        double powerAdder;
        double powerMultiplier;

        powerAdder = ((defense * 3) + attack + life) / 2;

        int countOfPowers = 0;

        for (boolean boo : powerList) {
            if (boo) {
                countOfPowers++;
            }
        }
        if (countOfPowers == 0) {
            powerMultiplier = 1;
        } else {
            powerMultiplier = 1.2 * countOfPowers;
        }
        Log.v("Power Count", Double.toString(countOfPowers));

        powerLevel = powerAdder * powerMultiplier;

        return (int) powerLevel;
    }

    private void checkIfNewCardOrEdit(){
        Intent intent = getIntent();
        if (intent == null){
            initilizePlaceHolderStats();
            createBooleanArrayList();
        } else {
            String cardName = intent.getStringExtra("card");
            DatabaseReference singleCardRef = database.getReference("cards").child(cardName);
            singleCardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    incomingCard = (CardToSave)dataSnapshot.getValue();
                    addCardFromFirebase(incomingCard);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.v(TAG, "DatabaseError = " + databaseError);
                }
            });
        }

    }

    private void addCardFromFirebase(CardToSave cardToSave){
        cardNameEditText.setText(cardToSave.getCardName());
        imageView.setImageBitmap(cardToSave.getCardImage());
        setLifeValue(cardToSave.getLife());
        setAttackValue(cardToSave.getAttack());
        setDefenseValue(cardToSave.getDefense());

        powerList.add(cardToSave.isHasQuick());
        powerList.add(cardToSave.isHasOverPower());
        powerList.add(cardToSave.isHasDefender());
        powerList.add(cardToSave.isHasRanged());
        powerList.add(cardToSave.isHasAreaAttack());
        powerList.add(cardToSave.isHasLightningReflexes());
        powerList.add(cardToSave.isHasConcealed());
        powerList.add(cardToSave.isHasPillage());
        powerList.add(cardToSave.isHasBattleReady());
        powerList.add(cardToSave.isHasRally());
        powerList.add(cardToSave.isHasBluff());
        powerList.add(cardToSave.isHasUnstable());
        powerList.add(cardToSave.isHasFiredUp());
        powerList.add(cardToSave.isHasDrawCard());
        powerList.add(cardToSave.isHasWarmBlooded());
        powerList.add(cardToSave.isHasLastWord());
        powerList.add(cardToSave.isHasSetFire());
        powerList.add(cardToSave.isHasArmorBreak());

        quickSwitch.setChecked(powerList.get(0));
        overpowerSwitch.setChecked(powerList.get(1));
        defenderSwitch.setChecked(powerList.get(2));
        rangedSwitch.setChecked(powerList.get(3));
        areaattackSwitch.setChecked(powerList.get(4));
        lightningreflexesSwitch.setChecked(powerList.get(5));
        concealedSwitch.setChecked(powerList.get(6));
        pillageSwitch.setChecked(powerList.get(7));
        battlereadySwitch.setChecked(powerList.get(8));
        rallySwitch.setChecked(powerList.get(9));
        bluffSwitch.setChecked(powerList.get(10));
        unstableSwitch.setChecked(powerList.get(11));
        firedupSwitch.setChecked(powerList.get(12));
        drawcardSwitch.setChecked(powerList.get(13));
        warmbloodedSwitch.setChecked(powerList.get(14));
        lastwordSwitch.setChecked(powerList.get(15));
        setfireSwitch.setChecked(powerList.get(16));
        armorbreakSwitch.setChecked(powerList.get(17));
        
        imageSet = true;
        mImageUri = Uri.parse(cardToSave.getImageId());
        setManaValue(initiateBlackBox());
        updateStats();
    }

    // checks if making a new card or editing old card
    // SQlite method
    /*
    private void getCardStatsOrNewCard() {
        Intent intent = getIntent();
        mCurrentCardUri = intent.getData();
        if (mCurrentCardUri == null) {
            initilizePlaceHolderStats();
            createBooleanArrayList();
        } else {
            getLoaderManager().initLoader(EDIT_LOADER, null, this);
        }
    } */

    // loader for SQlite
/*
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                mCurrentCardUri,
                null,
                null,
                null,
                null
        );
    } */

    // for SQLite
    /*
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {

            String name = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_NAME));
            String image = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_IMAGE));
            final Uri imageUri = Uri.parse(image);
            champion = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_CHAMPION));
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

            cardId = cursor.getLong(cursor.getColumnIndex(CardContract.CardEntry._ID));
            cardNameEditText.setText(name);
            imageView.setImageURI(imageUri);
            setLifeValue(life);
            setAttackValue(attack);
            setDefenseValue(defense);

            powerList.add(getBooleanFromInt(quickInt));
            powerList.add(getBooleanFromInt(overpowerInt));
            powerList.add(getBooleanFromInt(defenderInt));
            powerList.add(getBooleanFromInt(rangedInt));
            powerList.add(getBooleanFromInt(areaattackInt));
            powerList.add(getBooleanFromInt(lightningReflexesInt));
            powerList.add(getBooleanFromInt(concealedInt));
            powerList.add(getBooleanFromInt(pillageInt));
            powerList.add(getBooleanFromInt(battlereadyInt));
            powerList.add(getBooleanFromInt(rallyInt));
            powerList.add(getBooleanFromInt(bluffInt));
            powerList.add(getBooleanFromInt(unstableInt));
            powerList.add(getBooleanFromInt(firedupInt));
            powerList.add(getBooleanFromInt(drawcardInt));
            powerList.add(getBooleanFromInt(warmbloodedInt));
            powerList.add(getBooleanFromInt(lastwordInt));
            powerList.add(getBooleanFromInt(setfireInt));
            powerList.add(getBooleanFromInt(armorbreakInt));

            quickSwitch.setChecked(powerList.get(0));
            overpowerSwitch.setChecked(powerList.get(1));
            defenderSwitch.setChecked(powerList.get(2));
            rangedSwitch.setChecked(powerList.get(3));
            areaattackSwitch.setChecked(powerList.get(4));
            lightningreflexesSwitch.setChecked(powerList.get(5));
            concealedSwitch.setChecked(powerList.get(6));
            pillageSwitch.setChecked(powerList.get(7));
            battlereadySwitch.setChecked(powerList.get(8));
            rallySwitch.setChecked(powerList.get(9));
            bluffSwitch.setChecked(powerList.get(10));
            unstableSwitch.setChecked(powerList.get(11));
            firedupSwitch.setChecked(powerList.get(12));
            drawcardSwitch.setChecked(powerList.get(13));
            warmbloodedSwitch.setChecked(powerList.get(14));
            lastwordSwitch.setChecked(powerList.get(15));
            setfireSwitch.setChecked(powerList.get(16));
            armorbreakSwitch.setChecked(powerList.get(17));


            imageSet = true;
            mImageUri = imageUri;
            setManaValue(initiateBlackBox());
            updateStats();
        }
    } */

    private boolean getBooleanFromInt(int i) {
        if (i == 0) {
            return false;
        }
        return true;
    }

    // get index for item in spinner to set it manually
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    // LOADER - for SQLite
/*
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    } */

    // checks to make sure minimum requirements to submit a card are there
    private boolean validateFields() {
        if (cardNameEditText.getText().toString().length() != 0 && imageSet && championSpinner.toString() != null) {
            return true;
        }
        return false;
    }

    // only use for new card
    private void initilizePlaceHolderStats() {
        setManaValue(0);
        setLifeValue(0);
        setManaValue(0);
        setDefenseValue(0);
    }

    // i think only use for new? otherwise populate from values
    private void createBooleanArrayList() {
        powerList.add(quick);
        powerList.add(overpower);
        powerList.add(defender);
        powerList.add(ranged);
        powerList.add(areaattack);
        powerList.add(lightningreflexes);
        powerList.add(concealed);
        powerList.add(pillage);
        powerList.add(battleready);
        powerList.add(rally);
        powerList.add(bluff);
        powerList.add(unstable);
        powerList.add(firedup);
        powerList.add(drawcard);
        powerList.add(warmblooded);
        powerList.add(lastword);
        powerList.add(setfire);
        powerList.add(armorbreak);
    }

    private void setSpinner() {
        championSpinner = (Spinner) findViewById(R.id.card_creator_spinner_1_champion);
        championAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, championList);
        championAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        championSpinner.setAdapter(championAdapter);
    }

    private void setUi() {
        quickSwitch = (Switch) findViewById(R.id.card_creator_switch_1_quick);
        overpowerSwitch = (Switch) findViewById(R.id.card_creator_switch_2_overpower);
        defenderSwitch = (Switch) findViewById(R.id.card_creator_switch_3_defender);
        rangedSwitch = (Switch) findViewById(R.id.card_creator_switch_4_ranged);
        areaattackSwitch = (Switch) findViewById(R.id.card_creator_switch_5_area_attack);
        lightningreflexesSwitch = (Switch) findViewById(R.id.card_creator_switch_6_lightning_reflexes);
        concealedSwitch = (Switch) findViewById(R.id.card_creator_switch_7_concealed);
        pillageSwitch = (Switch) findViewById(R.id.card_creator_switch_8_pillage);
        battlereadySwitch = (Switch) findViewById(R.id.card_creator_switch_9_battle_ready);
        rallySwitch = (Switch) findViewById(R.id.card_creator_switch_10_rally);
        bluffSwitch = (Switch) findViewById(R.id.card_creator_switch_11_bluff);
        unstableSwitch = (Switch) findViewById(R.id.card_creator_switch_12_unstable);
        firedupSwitch = (Switch) findViewById(R.id.card_creator_switch_13_fired_up);
        drawcardSwitch = (Switch) findViewById(R.id.card_creator_switch_14_draw_card);
        warmbloodedSwitch = (Switch) findViewById(R.id.card_creator_switch_15_warm_blooded);
        lastwordSwitch = (Switch) findViewById(R.id.card_creator_switch_16_last_word);
        setfireSwitch = (Switch) findViewById(R.id.card_creator_switch_17_set_fire);
        armorbreakSwitch = (Switch) findViewById(R.id.card_creator_switch_18_armor_break);
        creatureScrollView = (ScrollView) findViewById(R.id.card_creator_scroll_view_creature);
        spellScrollView = (ScrollView) findViewById(R.id.card_creator_scroll_view_spell);
        cardNameEditText = (EditText) findViewById(R.id.card_creator_name);
        cardDescriptionEditText = findViewById(R.id.card_creator_card_description_edit_text);
        cardManaCost = (TextView) findViewById(R.id.card_creator_mana_cost);
        cardLife = (TextView) findViewById(R.id.card_creator_life_textview);
        cardAttack = (TextView) findViewById(R.id.card_creator_attack_textview);
        cardDefense = (TextView) findViewById(R.id.card_creator_defense_textview);


        quickSwitch.setOnTouchListener(mTouchListener);
        overpowerSwitch.setOnTouchListener(mTouchListener);
        defenderSwitch.setOnTouchListener(mTouchListener);
        rangedSwitch.setOnTouchListener(mTouchListener);
        areaattackSwitch.setOnTouchListener(mTouchListener);
        lightningreflexesSwitch.setOnTouchListener(mTouchListener);
        concealedSwitch.setOnTouchListener(mTouchListener);
        pillageSwitch.setOnTouchListener(mTouchListener);
        battlereadySwitch.setOnTouchListener(mTouchListener);
        rallySwitch.setOnTouchListener(mTouchListener);
        bluffSwitch.setOnTouchListener(mTouchListener);
        firedupSwitch.setOnTouchListener(mTouchListener);
        drawcardSwitch.setOnTouchListener(mTouchListener);
        warmbloodedSwitch.setOnTouchListener(mTouchListener);
        lastwordSwitch.setOnTouchListener(mTouchListener);
        setfireSwitch.setOnTouchListener(mTouchListener);
        armorbreakSwitch.setOnTouchListener(mTouchListener);

        quickSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        overpowerSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        defenderSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        rangedSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        areaattackSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        lightningreflexesSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        concealedSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        pillageSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        battlereadySwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        rallySwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        bluffSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        firedupSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        drawcardSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        warmbloodedSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        lastwordSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        setfireSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
        armorbreakSwitch.setOnCheckedChangeListener(onCheckedChangeListenerCreature);
    }

    private void setButtons() {
        attackPlus = (Button) findViewById(R.id.card_creator_attack_plus_button);
        attackMinus = (Button) findViewById(R.id.card_creator_attack_minus_button);
        lifePlus = (Button) findViewById(R.id.card_creator_life_plus_button);
        lifeMinus = (Button) findViewById(R.id.card_creator_life_minus_button);
        defensePlus = (Button) findViewById(R.id.card_creator_defense_plus_button);
        defenseMinus = (Button) findViewById(R.id.card_creator_defense_minus_button);
        saveButton = (Button) findViewById(R.id.card_creator_save_button);
        deleteButton = (Button) findViewById(R.id.card_creator_delete_button);
        imageButton = (Button) findViewById(R.id.card_creator_image_button);
        imageView = (ImageView) findViewById(R.id.card_creator_image_view);


        attackMinus.setOnTouchListener(mTouchListener);
        attackPlus.setOnTouchListener(mTouchListener);
        lifePlus.setOnTouchListener(mTouchListener);
        lifeMinus.setOnTouchListener(mTouchListener);
        defensePlus.setOnTouchListener(mTouchListener);
        defenseMinus.setOnTouchListener(mTouchListener);
        imageButton.setOnTouchListener(mTouchListener);

        deleteButton.setOnClickListener(onClickListener);
        attackPlus.setOnClickListener(onClickListener);
        attackMinus.setOnClickListener(onClickListener);
        lifePlus.setOnClickListener(onClickListener);
        lifeMinus.setOnClickListener(onClickListener);
        defensePlus.setOnClickListener(onClickListener);
        defenseMinus.setOnClickListener(onClickListener);
        saveButton.setOnClickListener(onClickListener);
        imageButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.card_creator_image_button:
                    openImage();
                    break;
                case R.id.card_creator_attack_plus_button:
                    if (getAttackValue() <= 9 && initiateBlackBox() <= 10) {
                        setAttackValue(getAttackValue() + 1);
                        setManaValue(initiateBlackBox());
                    } else {
                        errorToast();
                    }
                    updateStats();
                    break;
                case R.id.card_creator_attack_minus_button:
                    if (getAttackValue() >= 1) {
                        setAttackValue(getAttackValue() - 1);
                        setManaValue(initiateBlackBox());
                    }
                    updateStats();
                    break;
                case R.id.card_creator_life_plus_button:
                    if (getLifeValue() <= 9 && initiateBlackBox() <= 10) {
                        setLifeValue(getLifeValue() + 1);
                        setManaValue(initiateBlackBox());
                    } else {
                        errorToast();
                    }
                    updateStats();
                    break;
                case R.id.card_creator_life_minus_button:
                    if (getLifeValue() >= 1) {
                        setLifeValue(getLifeValue() - 1);
                        setManaValue(initiateBlackBox());
                    }
                    updateStats();
                    break;
                case R.id.card_creator_defense_plus_button:
                    if (getDefenseValue() <= 2 && initiateBlackBox() <= 10) {
                        setDefenseValue(getDefenseValue() + 1);
                        setManaValue(initiateBlackBox());
                    } else {
                        errorToast();
                    }
                    updateStats();
                    break;
                case R.id.card_creator_defense_minus_button:
                    if (getDefenseValue() >= 1) {
                        setDefenseValue(getDefenseValue() - 1);
                        setManaValue(initiateBlackBox());
                    }
                    updateStats();
                    break;
                case R.id.card_creator_save_button:
                    if (validateFields()) {
                        saveButton();
                    } else {
                        Toasts.toastFailure(mContext);
                        Log.v("save button", "validate field failure");
                    }
                    break;
                case R.id.card_creator_delete_button:
                    deleteDialog();
                    break;
            }
        }
    };


    private CardToSave createCardToSave() {
//TODO CHANGE FROM CREATURE ONLY WHEN SPELLS ARE IMPLEMENTED
        if (cardBitmap != null && !cardNameEditText.getText().toString().trim().equals("")) {
            return new CardToSave(cardNameEditText.getText().toString().trim(), cardDescriptionEditText.getText().toString().trim(), champion, mImageUri.toString(), getManaValue(), getAttackValue(), getLifeValue(), getDefenseValue(),
                    powerList.get(0), powerList.get(1), powerList.get(2), powerList.get(3), powerList.get(4), powerList.get(5),
                    powerList.get(6), powerList.get(7), powerList.get(8), powerList.get(9), powerList.get(10), powerList.get(11),
                    powerList.get(12), powerList.get(13), powerList.get(14), powerList.get(15), powerList.get(16), powerList.get(17), cardBitmap, "CREATURE");
        } else {
            Toasts.toastFailure(this);
            return null;
        }
    }

    // takes reference to 'cards' from firebase db, adds a tier for the name of the card being created, then saves the card in its entirety with the image under the card name
    private void saveButton() {
        cardRef.child(cardNameEditText.getText().toString().trim()).setValue(createCardToSave() , new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.v(TAG, "Value Set! Error = " + databaseError);
                finish();
            }
        });
    }

    // method to save to SQLite database
    /*
    private void deprecatedSQLSave(){
        ContentValues values = new ContentValues();

        try{
            cardBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mImageUri);
        } catch (IOException io){
            Log.v(TAG, "IO EXCEPTION: " + io);
            Toasts.toastFailure(mContext);
        }

        String name = cardNameEditText.getText().toString().trim();
        String image = mImageUri.toString();
        String description = cardDescriptionEditText.toString().trim();
        String champion = championSpinner.getSelectedItem().toString();
        Log.v("CHAMPIONSTRINGSPINNER", champion);
        int life = getLifeValue();
        int manacost = initiateBlackBox();
        int attack = getAttackValue();
        int defense = getDefenseValue();

        int quickInt = getPowerBooleanFromArray(0);
        int overpowerInt = getPowerBooleanFromArray(1);
        int defenderInt = getPowerBooleanFromArray(2);
        int rangedInt = getPowerBooleanFromArray(3);
        int areaattackInt = getPowerBooleanFromArray(4);
        int lightningReflexesInt = getPowerBooleanFromArray(5);
        int concealedInt = getPowerBooleanFromArray(6);
        int pillageInt = getPowerBooleanFromArray(7);
        int battlereadyInt = getPowerBooleanFromArray(8);
        int rallyInt = getPowerBooleanFromArray(9);
        int bluffInt = getPowerBooleanFromArray(10);
        int unstableInt = getPowerBooleanFromArray(11);
        int firedupInt = getPowerBooleanFromArray(12);
        int drawcardInt = getPowerBooleanFromArray(13);
        int warmbloodedInt = getPowerBooleanFromArray(14);
        int lastwordInt = getPowerBooleanFromArray(15);
        int setfireInt = getPowerBooleanFromArray(16);
        int armorbreakInt = getPowerBooleanFromArray(17);

        // TODO CHANGE THIS WHEN SPELLS ARE IMPLEMENTED
        String cardType = "CREATURE";

        values.put(CardContract.CardEntry.COLUMN_CARD_NAME, name);
        values.put(CardContract.CardEntry.COLUMN_CARD_IMAGE, image);
        values.put(CardContract.CardEntry.COLUMN_CARD_CHAMPION, champion);
        values.put(CardContract.CardEntry.COLUMN_CARD_LIFE, life);
        values.put(CardContract.CardEntry.COLUMN_CARD_MANACOST, manacost);
        values.put(CardContract.CardEntry.COLUMN_CARD_ATTACK, attack);
        values.put(CardContract.CardEntry.COLUMN_CARD_DEFENSE, defense);
        values.put(CardContract.CardEntry.COLUMN_CARD_QUICK, quickInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_OVERPOWER, overpowerInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_DEFENDER, defenderInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_RANGED, rangedInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_AREAATTACK, areaattackInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_LIGHTNINGREFLEXES, lightningReflexesInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_CONCEALED, concealedInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_PILLAGE, pillageInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_BATTLEREADY, battlereadyInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_RALLY, rallyInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_BLUFF, bluffInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_UNSTABLE, unstableInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_FIREDUP, firedupInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_DRAWCARD, drawcardInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_WARMBLOODED, warmbloodedInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_LASTWORD, lastwordInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_SETFIRE, setfireInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_ARMORBREAK, armorbreakInt);
        values.put(CardContract.CardEntry.COLUMN_CARD_TYPE, cardType);


        // insert into SQL DB

        if (mCurrentCardUri == null) {
            Uri uri = null;
            try {
                uri = getContentResolver().insert(CardContract.CardEntry.CONTENT_URI, values);
                mCurrentCardUri = uri;
            } catch (IllegalArgumentException e) {
                Log.v(TAG, "Illegal Argument exception = " + e);
                Toasts.toastFailure(this);
            }
            if (uri != null) {
                getLoaderManager().initLoader(ID_LOADER, null, new IdLoader());
            }
        } else {
            getContentResolver().update(mCurrentCardUri, values, null, null);
            getLoaderManager().initLoader(ID_LOADER, null, new IdLoader());
        }
    } */

    private int getPowerBooleanFromArray(int locationInArray) {

        if (powerList.get(locationInArray)) {
            return 1;
        } else return 0;
    }

    private void updateStats() {
        cardAttack.setText(Integer.toString(getAttackValue()));
        cardLife.setText(Integer.toString(getLifeValue()));
        cardDefense.setText(Integer.toString(getDefenseValue()));
        cardManaCost.setText(Integer.toString(getManaValue()));
    }

    private void errorToast() {
        Toast toast = Toast.makeText(this, "Mana cost too high, card has reached power limit!", Toast.LENGTH_SHORT);
        toast.show();
    }


    CompoundButton.OnCheckedChangeListener onCheckedChangeListenerCreature = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.card_creator_switch_1_quick:
                    switchValueChanger(0);
                    break;
                case R.id.card_creator_switch_2_overpower:
                    switchValueChanger(1);
                    break;
                case R.id.card_creator_switch_3_defender:
                    switchValueChanger(2);
                    break;
                case R.id.card_creator_switch_4_ranged:
                    switchValueChanger(3);
                    break;
                case R.id.card_creator_switch_5_area_attack:
                    switchValueChanger(4);
                    break;
                case R.id.card_creator_switch_6_lightning_reflexes:
                    switchValueChanger(5);
                    break;
                case R.id.card_creator_switch_7_concealed:
                    switchValueChanger(6);
                    break;
                case R.id.card_creator_switch_8_pillage:
                    switchValueChanger(7);
                    break;
                case R.id.card_creator_switch_9_battle_ready:
                    switchValueChanger(8);
                    break;
                case R.id.card_creator_switch_10_rally:
                    switchValueChanger(9);
                    break;
                case R.id.card_creator_switch_11_bluff:
                    switchValueChanger(10);
                    break;
                case R.id.card_creator_switch_12_unstable:
                    switchValueChanger(11);
                    break;
                case R.id.card_creator_switch_13_fired_up:
                    switchValueChanger(12);
                    break;
                case R.id.card_creator_switch_14_draw_card:
                    switchValueChanger(13);
                    break;
                case R.id.card_creator_switch_15_warm_blooded:
                    switchValueChanger(14);
                    break;
                case R.id.card_creator_switch_16_last_word:
                    switchValueChanger(15);
                    break;
                case R.id.card_creator_switch_17_set_fire:
                    switchValueChanger(16);
                    break;
                case R.id.card_creator_switch_18_armor_break:
                    switchValueChanger(17);
                    break;
            }

        }
    };

    // Switches value of booleans in array of powers & updates mana cost
    private void switchValueChanger(int numberInArray) {

        if (powerList.get(numberInArray)) {
            powerList.set(numberInArray, false);
            setManaValue(initiateBlackBox());
            updateStats();
        } else {
            powerList.set(numberInArray, true);
            setManaValue(initiateBlackBox());
            updateStats();
        }
    }


    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getLifeValue() {
        return lifeValue;
    }

    public void setLifeValue(int lifeValue) {
        this.lifeValue = lifeValue;
    }

    public int getManaValue() {
        return manaValue;
    }

    public void setManaValue(int manaValue) {
        this.manaValue = manaValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    private void openImage() {
        checkWritePermission();
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                mImageUri = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    mImageUri = data.getData();

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mImageUri = data.getData();
                    int takeFlags = data.getFlags();
                    takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    ContentResolver contentResolver = mContext.getContentResolver();
                    contentResolver.takePersistableUriPermission(mImageUri, takeFlags);
                }

                imageView.setImageURI(mImageUri);
                imageView.invalidate();
                // take image uri and write to bitmap to be uploaded to firebase
                try{
                    cardBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mImageUri);
                } catch (IOException io){
                    Log.v(TAG, "IO EXCEPTION: " + io);
                    Toasts.toastFailure(mContext);
                }
               /*
                File imageFile = new File(mImageUri.toString());
                if (imageFile.exists()){
                    cardBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                } else {
                    Log.v(TAG, "CARD BITMAP FAILED! ! ! ! ! !");
                    Log.v(TAG, "IMAGE URI: " + mImageUri.toString());
                } */

                imageSet = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }
        }
    }

    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private class ChampionLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    mContext,
                    ChampionContract.ChampionEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
                do {
                    String championName = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME));
                    championList.add(championName);
                }
                while (cursor.moveToNext());
                setSpinner();
                if (mCurrentCardUri != null) {
                    championSpinner.setSelection(getIndex(championSpinner, champion));
                }
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
        if (mCurrentCardUri != null) {
            builder.setMessage(R.string.delete_message);
        } else {
            builder.setMessage(R.string.delete_progress);
        }
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    if (mCurrentCardUri != null) {
                        ContentResolver contentResolver = getContentResolver();
                        contentResolver.delete(mCurrentCardUri, null, null);
                        contentResolver.notifyChange(mCurrentCardUri, null);

                        finish();
                    } else {
                        cardNameEditText.setText(null);
                        setAttackValue(0);
                        setManaValue(0);
                        setLifeValue(0);
                        setDefenseValue(0);
                        imageSet = false;
                        mImageUri = null;
                        imageView.setImageURI(mImageUri);
                        imageView.invalidate();
                        quickSwitch.setChecked(false);
                        overpowerSwitch.setChecked(false);
                        defenderSwitch.setChecked(false);
                        rangedSwitch.setChecked(false);
                        areaattackSwitch.setChecked(false);
                        lightningreflexesSwitch.setChecked(false);
                        concealedSwitch.setChecked(false);
                        pillageSwitch.setChecked(false);
                        battlereadySwitch.setChecked(false);
                        rallySwitch.setChecked(false);
                        bluffSwitch.setChecked(false);
                        firedupSwitch.setChecked(false);
                        drawcardSwitch.setChecked(false);
                        warmbloodedSwitch.setChecked(false);
                        lastwordSwitch.setChecked(false);
                        setfireSwitch.setChecked(false);
                        armorbreakSwitch.setChecked(false);
                        initiateBlackBox();
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

    // loader to get SQlite ID and then upload to firebase DB with ID
    /*
    private class IdLoader implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    mContext,
                    mCurrentCardUri,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
                cardId = cursor.getLong(cursor.getColumnIndex(CardContract.CardEntry._ID));
                cardRef.child(cardNameEditText.getText().toString().trim()).setValue(createCardToSave() , new DatabaseReference.CompletionListener() {
                   @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.v(TAG, "Value Set! Error = " + databaseError);
                       finish();
                    }
                });

            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            loader.reset();
        }
    } */

}
