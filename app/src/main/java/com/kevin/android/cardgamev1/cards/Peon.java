package com.kevin.android.cardgamev1.cards;


import com.kevin.android.cardgamev1.blueprintclasses.Card;
import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.tools.UriTools;

import static com.kevin.android.cardgamev1.Constants.CardTypes.CREATURE;

public class Peon extends Card {

   public Peon () {
        cardId = "001";
        cardName = "Peon";
        cardText = "It's just a peon.";
      //  cardType = CREATURE;
        attackValue = 1;
        lifeValue = 1;
        defenseValue = 0;
        imageId = UriTools.getResourceUri(R.drawable.peon).toString();
        manaCost = 1;
        effectiveManaCost = manaCost;
   }

}
