package com.kevin.android.cardgamev1.decks;


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
import com.kevin.android.cardgamev1.database.DeckContract;


public class DeckAdapter extends CursorAdapter{

    Context mContext;

    public DeckAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
    }

    public static class ItemViewHolder {
       //view declarations go here

        ViewGroup deckContainer;
        ImageView championImage;
        TextView deckName;


        public ItemViewHolder(View view){
           // view initilization goes here (findviewbyid)
            deckContainer = (ViewGroup)view.findViewById(R.id.deck_wrapper);
            championImage = (ImageView)view.findViewById(R.id.picture_slot_deck_library);
            deckName = (TextView)view.findViewById(R.id.deck_name_deck_library);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.deck_list_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        view.setTag(itemViewHolder);
        view.isFocusable();
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) view.getTag();
        final long id = cursor.getInt(cursor.getColumnIndex(DeckContract.DeckEntry._ID));
        final String deckName = cursor.getString(cursor.getColumnIndex(DeckContract.DeckEntry.COLUMN_DECK_DECKNAME));
        final String imageUri = cursor.getString(cursor.getColumnIndex(DeckContract.DeckEntry.COLUMN_DECK_IMAGE));

        itemViewHolder.deckName.setText(deckName);
        itemViewHolder.championImage.setImageURI(Uri.parse(imageUri));
    }
}
