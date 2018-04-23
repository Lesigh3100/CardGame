package com.kevin.android.cardgamev1.decks;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.creationtools.champions.ChampionAdapter;
import com.kevin.android.cardgamev1.database.ChampionContract;


public class DeckChampionChooser extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    ChampionAdapter championAdapter;
    GridView gridView;
    private final int CHAMPION_LOADER = 141;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_champion_chooser);
        gridView = (GridView)findViewById(R.id.grid_deck_champion_chooser);
        championAdapter = new ChampionAdapter(this, null);
        getLoaderManager().initLoader(CHAMPION_LOADER, null, this);
        gridView.setAdapter(championAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(DeckChampionChooser.this, DeckBuilder.class);
                String championName = championAdapter.getName(position);
                intent.putExtra("Name", championName);
                startActivity(intent);
                finish();
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
            championAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    loader.reset();
    }
}
