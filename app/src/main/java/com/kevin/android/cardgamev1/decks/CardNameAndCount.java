package com.kevin.android.cardgamev1.decks;


public class CardNameAndCount {

    private String cardName;
    private int cardCount = -1;
    private long cardId;

    public CardNameAndCount() {
    }

    public CardNameAndCount(String cardName, long cardId) {
        this.cardName = cardName;
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }
}
