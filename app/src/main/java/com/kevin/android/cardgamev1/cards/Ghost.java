package com.kevin.android.cardgamev1.cards;

import com.kevin.android.cardgamev1.blueprintclasses.Card;
import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.tools.UriTools;

import static com.kevin.android.cardgamev1.Constants.CardTypes.CREATURE;


public class Ghost extends Card {
    public Ghost () {
        cardId = "002";
        cardName = "Ghost";
        cardText = "Sorta spooky.";
      //  cardType = CREATURE;
        attackValue = 1;
        lifeValue = 2;
        defenseValue = 0;
        imageId = UriTools.getResourceUri(R.drawable.ghost).toString();
        manaCost = 2;
        effectiveManaCost = manaCost;
    }
}
