package com.kevin.android.cardgamev1.creationtools.champions;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.database.ChampionContract;

public class ChampionAdapter extends CursorAdapter {
    private Context mContext;

    public ChampionAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mContext = context;
    }


    public String getName(int position) {
        Cursor cursor = getCursor();
        String name = null;
        if (cursor.moveToPosition(position)){
            name = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME));
        }
     return name;
    }

    private static class ChampionViewHolder {
        ViewGroup viewGroup;
        TextView name;
        ImageView image;

        public ChampionViewHolder (View view){
            viewGroup = (ViewGroup)view.findViewById(R.id.champion_wrapper);
            name = (TextView)view.findViewById(R.id.champion_list_name);
            image = (ImageView)view.findViewById(R.id.champion_list_image);
        }

    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.champion_list_item, parent, false);
        ChampionViewHolder itemViewHolder = new ChampionViewHolder(view);
        view.setTag(itemViewHolder);
        view.isFocusable();
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ChampionViewHolder championViewHolder = (ChampionViewHolder)view.getTag();
        final long id = cursor.getInt(cursor.getColumnIndex(ChampionContract.ChampionEntry._ID));
        final String championName = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME));
        final String imageUri = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_IMAGE));
        championViewHolder.image.setImageURI(Uri.parse(imageUri));
        championViewHolder.name.setText(championName);
    }
}
