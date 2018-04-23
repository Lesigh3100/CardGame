package com.kevin.android.cardgamev1.decks;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.database.DeckContract;

public class DeckSelector extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    GridView gridView;
    DeckAdapter mDeckAdapter;
    private static final int URI_LOADER = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_selector);
        gridView = (GridView)findViewById(R.id.grid_deck_selector);
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.deck_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeckSelector.this, DeckChampionChooser.class);
                startActivity(intent);
            }
        });
        mDeckAdapter = new DeckAdapter(this, null);
        getLoaderManager().initLoader(URI_LOADER, null, this);
        gridView.setAdapter(mDeckAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(DeckSelector.this, DeckBuilder.class);
                Uri itemUri = ContentUris.withAppendedId(DeckContract.DeckEntry.CONTENT_URI, id);
                intent.setData(itemUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                DeckContract.DeckEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null){
            mDeckAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDeckAdapter.swapCursor(null);
    }
}
