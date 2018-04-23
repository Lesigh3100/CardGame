package com.kevin.android.cardgamev1.creationtools.champions;

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
import com.kevin.android.cardgamev1.database.ChampionContract;


public class ChampionDatabase extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    GridView gridView;
    ChampionAdapter mAdapter;
    private static final int URI_LOADER = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_database);
        gridView = (GridView)findViewById(R.id.champion_database_grid);
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.champion_database_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChampionDatabase.this, ChampionCreator.class);
                startActivity(intent);
            }
        });
        mAdapter = new ChampionAdapter(this, null);
        getLoaderManager().initLoader(URI_LOADER, null, this);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(ChampionDatabase.this, ChampionCreator.class);
                Uri itemUri = ContentUris.withAppendedId(ChampionContract.ChampionEntry.CONTENT_URI, id);
                intent.setData(itemUri);
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                ChampionContract.ChampionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null){
            mAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}

