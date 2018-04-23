package com.kevin.android.cardgamev1.creationtools.cards;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.android.cardgamev1.CardToSave;
import com.kevin.android.cardgamev1.R;

import java.util.ArrayList;

public class CardFirebaseAdapter extends ArrayAdapter<CardToSave>{
    private Context mContext;
    CardToSave card;
    ArrayList<CardToSave> cardsToSave;

    public CardFirebaseAdapter(@NonNull Context context, ArrayList<CardToSave> cards) {
        super(context, R.layout.card_list_item_library);
        this.cardsToSave = cards;
    }

    @Nullable
    @Override
    public CardToSave getItem(int position) {
        return super.getItem(position);
    }
    private static class ItemViewHolder1 {
        ViewGroup cardContainer;
        TextView nameText;
        ImageView imageButton;
        TextView onCardAttack;
        TextView onCardLife;
        TextView onCardMana;
        ImageView onCardHeart;
        ImageView onCardSword;
        ImageView onCardManaBottle;

        private ItemViewHolder1(View view){
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
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_list_item_library, parent, false);
        }
        final CardToSave currentCard = getItem(position);
        ItemViewHolder1 itemViewHolder = new ItemViewHolder1(listItemView);



        itemViewHolder.imageButton.setImageBitmap(currentCard.getCardImage());

        //TODO NOT WORKING
        //  itemViewHolder.imageButton.postInvalidate();

        itemViewHolder.onCardAttack.setText(currentCard.getAttack());
        itemViewHolder.onCardLife.setText(currentCard.getLife());
        itemViewHolder.onCardMana.setText(currentCard.getManaCost());

        //TODO ADD DEFENSE
        //   itemViewHolder.onCardDefense.setText(Integer.toString(cardAttack));

        return listItemView;
    }




}
