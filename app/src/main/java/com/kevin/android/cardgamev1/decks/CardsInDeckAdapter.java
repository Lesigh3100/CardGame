package com.kevin.android.cardgamev1.decks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kevin.android.cardgamev1.R;


import java.util.ArrayList;


public class CardsInDeckAdapter extends ArrayAdapter<CardNameAndCount> {
    private Context mContext;

    public CardsInDeckAdapter(@NonNull Context context, ArrayList<CardNameAndCount> cards) {
        super(context, R.layout.deck_card_in_deck_slot_list_item, cards);
        this.mContext = context;

    }


    private static class ItemViewHolder {
        ViewGroup layout;
        TextView numberCards;
        TextView nameCard;

        private ItemViewHolder (View view){
            layout = (ViewGroup)view.findViewById(R.id.deck_creator_card_in_deck_slot_layout);
            numberCards = (TextView)view.findViewById(R.id.deck_creator_text_number_of_cards);
            nameCard = (TextView)view.findViewById(R.id.deck_creator_text_slot_card_name);
        }
    }

    @Nullable
    @Override
    public CardNameAndCount getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView1 = convertView;
        ItemViewHolder itemViewHolder;

        if (listItemView1 == null){
            listItemView1 = LayoutInflater.from(getContext()).inflate(R.layout.deck_card_in_deck_slot_list_item, parent, false);
            itemViewHolder = new ItemViewHolder(listItemView1);
            listItemView1.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) listItemView1.getTag();
        }
        final CardNameAndCount cardNameAndCount = getItem(position);

        String cardName = cardNameAndCount.getCardName();
        long cardCount = cardNameAndCount.getCardCount();
        itemViewHolder.nameCard.setText(cardName);
        itemViewHolder.numberCards.setText(Long.toString(cardCount));
        return listItemView1;
    }

}
