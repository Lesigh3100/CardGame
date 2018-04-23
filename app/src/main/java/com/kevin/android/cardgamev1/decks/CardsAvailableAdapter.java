package com.kevin.android.cardgamev1.decks;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.database.CardContract;

import java.util.ArrayList;


// clone of card cursor adapter but implements onclick with an arraylist
public class CardsAvailableAdapter extends CursorAdapter    {
        private Context mContext;

    public CardsAvailableAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
        mContext = context;
    }
        private static class ItemViewHolder {
            ViewGroup cardContainer;
            TextView nameText;
            ImageView imageButton;
            TextView onCardAttack;
            TextView onCardLife;
            TextView onCardMana;
            ImageView onCardHeart;
            ImageView onCardSword;
            ImageView onCardManaBottle;

            private ItemViewHolder(View view){
                nameText = (TextView)view.findViewById(R.id.card_name_card_library);
                cardContainer =(ViewGroup)view.findViewById(R.id.card_slot_layout_container_card_library);
                imageButton = (ImageView) view.findViewById(R.id.picture_slot_card_library);
                onCardAttack = (TextView) view.findViewById(R.id.on_card_attack_card_library);
                onCardLife = (TextView) view.findViewById(R.id.on_card_hp_card_library);
                onCardMana = (TextView) view.findViewById(R.id.on_card_mana_card_library);
                onCardHeart = (ImageView) view.findViewById(R.id.heart_card_library);
                onCardSword = (ImageView) view.findViewById(R.id.sword_card_library);
                onCardManaBottle = (ImageView) view.findViewById(R.id.mana_bottle_card_library);
            }
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_list_item_library, parent, false);
        CardsAvailableAdapter.ItemViewHolder itemViewHolder = new CardsAvailableAdapter.ItemViewHolder(view);
        view.setTag(itemViewHolder);
        view.isFocusable();
        return view;
    }

    @Override
        public void bindView(View view, Context context, Cursor cursor) {
        CardsAvailableAdapter.ItemViewHolder itemViewHolder = (CardsAvailableAdapter.ItemViewHolder) view.getTag();
        final long id = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry._ID));
        final String cardName = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_NAME));
        final String imageUri = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_IMAGE));
        final int cardAttack = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_ATTACK));
        final int cardLife = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_LIFE));
        final int cardMana = cursor.getInt(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_MANACOST));
        final String cardType = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_TYPE));
        final String cardChampion = cursor.getString(cursor.getColumnIndex(CardContract.CardEntry.COLUMN_CARD_CHAMPION));

        final CardNameAndCount cardNameAndCount = new CardNameAndCount(cardName, id);

        // can be used to allow changes to card through here, probably wont though
        final Uri uri = CardContract.CardEntry.getContentUri(id);

        itemViewHolder.nameText.setText(cardName);

        itemViewHolder.imageButton.setImageURI(Uri.parse(imageUri));
        itemViewHolder.onCardAttack.setText(Integer.toString(cardAttack));
        itemViewHolder.onCardLife.setText(Integer.toString(cardLife));
        itemViewHolder.onCardMana.setText(Integer.toString(cardMana));

        //TODO ADD DEFENSE
        //   itemViewHolder.onCardDefense.setText(Integer.toString(cardAttack));
        itemViewHolder.onCardAttack.setText(Integer.toString(cardAttack));

    }
}