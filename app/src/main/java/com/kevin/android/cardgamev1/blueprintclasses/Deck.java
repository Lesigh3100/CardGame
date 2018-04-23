package com.kevin.android.cardgamev1.blueprintclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Deck implements Serializable {

    ArrayList<Card> cards = new ArrayList<>();

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card){
        cards.add(card);
    }
    public void removeCard(Card card){
        cards.remove(card);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void moveFromDeckToDeck (Deck deckToAdd, Deck deckToRemove, Card card) {
       deckToAdd.addCard(card);
       deckToRemove.removeCard(card);
   }
}
