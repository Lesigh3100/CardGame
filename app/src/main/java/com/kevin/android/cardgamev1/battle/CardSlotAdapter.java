package com.kevin.android.cardgamev1.battle;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.android.cardgamev1.Constants;
import com.kevin.android.cardgamev1.blueprintclasses.Player;
import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.blueprintclasses.Card;

import java.util.ArrayList;

public class CardSlotAdapter extends ArrayAdapter<Card>{

    private Context mContext;
    private Player player;
    private Card itemCard;
    private ViewGroup cardContainer;
    ArrayList<Card> cards;


    // add card array? activeDeck? to constructor
    public CardSlotAdapter(@NonNull Context context, ArrayList<Card> deck, Player player) {
        super(context, R.layout.card_list_item, deck);
        this.cards = deck;
        mContext = context;
        this.player = player;

    }

    public void updateData(ArrayList<Card> list){
        cards=list;
        notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        itemCard = getItem(position);
        if (player.getCurrentMana() >= itemCard.getEffectiveManaCost() && !itemCard.isPlayedThisTurn() && !itemCard.isAttackedThisTurn()){
            cardContainer.setBackground(getContext().getResources().getDrawable(R.drawable.black_border));
            return true;
        }
        else if (itemCard.isOnHeroBoard && !itemCard.isPlayedThisTurn() && !itemCard.isAttackedThisTurn()){
            cardContainer.setBackground(getContext().getResources().getDrawable(R.drawable.black_border));
            return true;
        }
      else {
            cardContainer.setBackground(getContext().getResources().getDrawable(R.drawable.red_border));
            return false;
        }

    }

    @Nullable
    @Override
    public Card getItem(int position) {
        return super.getItem(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_list_item, parent, false);
        }


        final Card currentCard = getItem(position);


        cardContainer = (ViewGroup)listItemView.findViewById(R.id.card_slot_layout_container);
        ImageView imageButton = (ImageView) listItemView.findViewById(R.id.picture_slot);
        TextView onCardAttack = (TextView)listItemView.findViewById(R.id.on_card_attack);
        TextView onCardHealth = (TextView)listItemView.findViewById(R.id.on_card_hp);
        TextView onCardMana = (TextView)listItemView.findViewById(R.id.on_card_mana);
        ImageView onCardHeart = (ImageView)listItemView.findViewById(R.id.heart);
        ImageView onCardSword = (ImageView)listItemView.findViewById(R.id.sword);
        ImageView onCardManaBottle = (ImageView)listItemView.findViewById(R.id.mana_bottle);

        onCardMana.setText(Integer.toString(currentCard.getEffectiveManaCost()));
        onCardAttack.setText(Integer.toString(currentCard.getAttackValue()));
        onCardHealth.setText(Integer.toString(currentCard.getLifeValue()));
        imageButton.setImageURI(null);
        imageButton.setImageURI(Uri.parse(currentCard.getImageId()));
        imageButton.invalidate();

      //  if (currentCard.getCardType() == Constants.CardTypes.CREATURE){
            onCardHeart.setVisibility(View.VISIBLE);
            onCardSword.setVisibility(View.VISIBLE);
            onCardHealth.setVisibility(View.VISIBLE);
            onCardAttack.setVisibility(View.VISIBLE);
    //    }
     //   else if (currentCard.getCardType() == Constants.CardTypes.SPELL){
            onCardHeart.setVisibility(View.INVISIBLE);
            onCardSword.setVisibility(View.INVISIBLE);
            onCardHealth.setVisibility(View.INVISIBLE);
            onCardAttack.setVisibility(View.INVISIBLE);
     //   }
        if (currentCard.isOnHeroBoard){
            onCardMana.setVisibility(View.INVISIBLE);
            onCardManaBottle.setVisibility(View.INVISIBLE);
        }

        else {cardContainer.setBackground(getContext().getResources().getDrawable(R.drawable.black_border));
        }
        return listItemView;
    }
}
