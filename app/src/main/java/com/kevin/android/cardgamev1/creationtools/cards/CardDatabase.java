package com.kevin.android.cardgamev1.creationtools.cards;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Loader;
import android.database.Cursor;
import android.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.design.widget.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kevin.android.cardgamev1.CardToSave;
import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.database.CardContract;

import java.util.ArrayList;
// when working with SQlite database
//implements LoaderManager.LoaderCallbacks<Cursor>

public class CardDatabase extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<CardToSave> cardList;
    GridView gridView;

    // switched to new adapter -CardFirebaseAdapter
  //  CardCursorAdapter mAdapter;

    CardFirebaseAdapter cardFirebaseAdapter;
    private static final int URI_LOADER = 0;
    String TAG = "LOG ! ! ! ! ! ! !";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_database);
        gridView = (GridView)findViewById(R.id.card_database_grid);
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.card_database_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardDatabase.this, CardCreator.class);
                startActivity(intent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference("cards");
        // old adapter
      //  mAdapter = new CardCursorAdapter(this, null);
        //  getLoaderManager().initLoader(URI_LOADER, null, this);

        cardList = new ArrayList<>();
        cardFirebaseAdapter = new CardFirebaseAdapter(this, cardList);
        gridView.setAdapter(cardFirebaseAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CardDatabase.this, CardCreator.class);
                intent.putExtra("card", cardFirebaseAdapter.getItem(position).getCardName());

                // for SQlite
              //  Uri itemUri = ContentUris.withAppendedId(CardContract.CardEntry.CONTENT_URI, id);
              //  intent.setData(itemUri);
                startActivity(intent);
            }
        });

        ValueEventListener cardListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot cardSnapshot : dataSnapshot.getChildren()){
                    CardToSave cardToSave = cardSnapshot.getValue(CardToSave.class);
                    Log.v(TAG, "CARD LOADED NAME = " + cardToSave.getCardName());
                    if (cardList.contains(cardToSave)){
                        return;
                    } else {
                        cardList.add(cardToSave);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "loadCard:onCancelled: error = " + databaseError);
            }
        };
        mDatabase.addValueEventListener(cardListener);

    }



/*
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                CardContract.CardEntry.CONTENT_URI,
                null,
                null,
                null,
                CardContract.CardEntry.COLUMN_CARD_MANACOST
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    if (cursor != null) {
        mAdapter.swapCursor(cursor);
    }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.swapCursor(null);
    } */
}
