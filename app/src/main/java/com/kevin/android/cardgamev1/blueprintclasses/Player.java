package com.kevin.android.cardgamev1.blueprintclasses;

import java.io.Serializable;

public class Player implements Serializable {

    private String playerName;
    private Champion champion;
    // MAKE THIS FINAL WHEN POSSIBLE
    private Deck startingDeck;
    //
    private Deck activeDeck;
    private Deck deckHand;
    private Deck deckBoard;
    private Deck deckDiscard;

   private int mana;
   private int currentMana;
   private int life;

   private int turnNumber;

    public Player(String playerName, Champion champion, Deck startingDeck, int mana, int life) {
        this.playerName = playerName;
        this.champion = champion;
        this.startingDeck = startingDeck;
        this.mana = mana;
        this.life = life;

    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public Deck getStartingDeck () {
        return startingDeck;
    }

    public Deck getDeckHand() {
        return deckHand;
    }

    public void setDeckHand(Deck deckHand) {
        this.deckHand = deckHand;
    }

    public Deck getDeckBoard() {
        return deckBoard;
    }

    public void setDeckBoard(Deck deckBoard) {
        this.deckBoard = deckBoard;
    }

    public Deck getDeckDiscard() {
        return deckDiscard;
    }

    public void setDeckDiscard(Deck deckDiscard) {
        this.deckDiscard = deckDiscard;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Champion getChampion() {
        return champion;
    }

    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
