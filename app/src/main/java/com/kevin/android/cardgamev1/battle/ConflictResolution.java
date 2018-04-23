package com.kevin.android.cardgamev1.battle;

import android.util.Log;

import com.kevin.android.cardgamev1.blueprintclasses.Card;
import com.kevin.android.cardgamev1.Constants;
import com.kevin.android.cardgamev1.blueprintclasses.Player;

import java.util.Iterator;

public class ConflictResolution {



    private Player hero;
    private Player opponent;

    private Card cardPlayed;
    private Card opponentCard;
    private Boolean attackingChampion = false;
  private Constants.GameStatus gameStatus;

    public ConflictResolution(Player hero, Player opponent) {
        setGameStatus(Constants.GameStatus.CONTINUE);
        this.hero = hero;
        this.opponent = opponent;
    }

    /*
    public ConflictResolution(Player hero, Player opponent, Card cardPlayed, Card opponentCard) {
        this.hero = hero;
        this.opponent = opponent;
        this.cardPlayed = cardPlayed;
        this.opponentCard = opponentCard;
        attackingChampion = false;
        cardPlayed.setAttackedThisTurn(true);
    }
    */

    // to attack champion
    /*
    public ConflictResolution(Player hero, Player opponent, Card cardPlayed) {
        this.hero = hero;
        this.opponent = opponent;
        this.cardPlayed = cardPlayed;
        attackingChampion = true;
        cardPlayed.setAttackedThisTurn(true);
    } */

    public void attackingCard(Player hero, Player opponent, Card cardPlayed, Card opponentCard){
        setHero(hero);
        setOpponent(opponent);
        setCardPlayed(cardPlayed);
        setOpponentCard(opponentCard);
        getCardPlayed().setAttackedThisTurn(true);
        setAttackingChampion(false);
        playSequence();
    }
    public void attackingChampion(Player hero, Player opponent, Card cardPlayed){
        setHero(hero);
        setOpponent(opponent);
        setCardPlayed(cardPlayed);
        setOpponentCard(null);
        getCardPlayed().setAttackedThisTurn(true);
        setAttackingChampion(true);
        playSequence();
    }


    public void playSequence() {
        //   cardSpell();
        //    checkBeforeAttackAbility();
        if (getAttackingChampion()) {
            attackChampion();
        } else {
            attack();
        }

        //     checkAfterAttackAbility();
        endPlaySequenceCleanup();
    }

    private void attack() {
        Log.v("OPPONENT CARD=", getOpponentCard().getCardName());
        Log.v("HERO CARD=", getCardPlayed().getCardName());

        int attackLife = getCardPlayed().getLifeValue();
        int defendLife = getOpponentCard().getLifeValue();
        int attackAttackValue = getCardPlayed().getAttackValue();
        int defendAttackValue = getOpponentCard().getAttackValue();
        int attackDefense = getCardPlayed().getDefenseValue();
        int defendDefense = getOpponentCard().getDefenseNumber();

        int attackerDamage = (attackAttackValue - defendDefense);
        int defendDamage = (defendAttackValue - attackDefense);

        if (attackerDamage <= 0) {
            attackerDamage = 0;
        }
        if (defendDamage <= 0) {
            defendDamage = 0;
        }
        attackLife = attackLife - defendDamage;
        defendLife = defendLife - attackerDamage;

        getCardPlayed().setLifeValue(attackLife);
        getOpponentCard().setLifeValue(defendLife);
        // if one of the cards has special ability that effects damage outcomes, separate method?
    }

    // pre constructor change

    private void attackChampion() {
        int attackerAttackValue = getCardPlayed().getAttackValue();
        int championLife = getOpponent().getLife();
        championLife = championLife - attackerAttackValue;
        getOpponent().setLife(championLife);
    }


    /*
    private void cardSpell () {
        if (cardPlayed.getCardType() != SPELL) {
        return;
        }
        cardPlayed.playSpell(cardPlayed, opponentCard, heroCardsOnBoard, enemyCardsOnBoard);
    } */

  /*  private void checkBeforeAttackAbility () {
    if (!cardPlayed.hasAbility()) {
        return;
    }
        boolean offense = true;
        cardPlayed.beforeAttackAbility(offense);
        opponentCard.beforeAttackAbility(!offense);
    } */
  /*
    private void checkAfterAttackAbility () {
    if (!cardPlayed.hasAbility()){
        return;
    }
        boolean offense = true;
        cardPlayed.afterAttackAbility(offense);
        opponentCard.afterAttackAbility(!offense);
    } */

    private void endPlaySequenceCleanup() {

        checkEndGame();

        for (Iterator<Card> iterator = hero.getDeckBoard().getCards().iterator(); iterator.hasNext();){
            Card card = iterator.next();
            if (card.getLifeValue() <= 0){
                iterator.remove();
                hero.getDeckDiscard().getCards().add(card);
            }
        }

        for (Iterator<Card> iterator = opponent.getDeckBoard().getCards().iterator(); iterator.hasNext();){
            Card card = iterator.next();
            if (card.getLifeValue() <= 0){
                iterator.remove();
                opponent.getDeckDiscard().getCards().add(card);
            }
        }

// NEEDS REFRACTOR TO STOP java.util.ConcurrentModificationException
        /*
        for (Card check : hero.getDeckBoard().getCards()) {
            if (check.getLifeValue() <= 0) {
                check.setOnHeroBoard(false);
                hero.getDeckBoard().moveFromDeckToDeck(hero.getDeckDiscard(), hero.getDeckBoard(), check);
            }
        }
        for (Card check : opponent.getDeckBoard().getCards()) {
            if (check.getLifeValue() <= 0) {
                check.setOnHeroBoard(false);
                opponent.getDeckBoard().moveFromDeckToDeck(opponent.getDeckDiscard(), opponent.getDeckBoard(),check);
            }
        }
        */
    }

    public void checkEndGame() {

        if (heroDead() && enemyDead()) {
            setGameStatus(Constants.GameStatus.DRAW);
        } else if (heroDead() && !enemyDead()) {
            setGameStatus(Constants.GameStatus.ENEMYWIN);
        } else if (!heroDead() && enemyDead()) {
            setGameStatus(Constants.GameStatus.HEROWIN);
        }
        else  {setGameStatus(Constants.GameStatus.CONTINUE);}
    }

    private boolean heroDead() {
        if (getHero().getLife() <= 0)
            return true;
        else return false;
    }

    private boolean enemyDead() {
        if (getOpponent().getLife() <= 0)
            return true;
        else return false;
    }

    public Constants.GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Constants.GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Player getHero() {
        return hero;
    }

    public void setHero(Player hero) {
        this.hero = hero;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(Card cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    public Card getOpponentCard() {
        return opponentCard;
    }

    public void setOpponentCard(Card opponentCard) {
        this.opponentCard = opponentCard;
    }

    public Boolean getAttackingChampion() {
        return attackingChampion;
    }

    public void setAttackingChampion(Boolean attackingChampion) {
        this.attackingChampion = attackingChampion;
    }
}
