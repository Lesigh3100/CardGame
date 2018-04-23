package com.kevin.android.cardgamev1.blueprintclasses;

import android.graphics.Bitmap;
import android.net.Uri;

import com.kevin.android.cardgamev1.Constants;

import java.io.Serializable;


// implements serialable
public class Card implements Serializable{

    // change into abstract class to require implementation of methods IMO

    protected String cardId;
    protected String cardName;
    protected String cardText;
    protected String championName;
    protected String imageId;

    protected int manaCost = 0;

  //  protected Bitmap cardImage;

   /* public enum CardType {CREATURE, SPELL;
    CardType(){}
        String value;

        public String getValue(){
            return this.name();
        }

        public static CardType fromValue(String cardTypeString){
            for (CardType cardType1 : CardType.values()){
                if (cardType1.name().equals(cardTypeString)){
                    return cardType1;
                }
            }
            throw new IllegalArgumentException("Invalid Card Type: " + cardTypeString);
        }


    } */


    // protected int cardType;
  //  protected CardType cardType;
    protected int attackValue = 0;
    protected int lifeValue = 0;
    protected int defenseValue = 0;
    protected int attackNumber = 0;
    protected int defenseNumber = 0;
    protected int takenDamageNumber = 0;
    protected int effectiveManaCost;
    protected int effectiveAttackValue = 0;
    protected int effectiveDefenseValue = 0;
    protected int effectiveLifeValue = 0;
    protected int creaturesKilled = 0;
    protected boolean playedThisTurn = false;
    protected boolean attackedThisTurn = false;


    protected boolean hasOverPower = false;
    protected boolean hasDefender = false;
    protected boolean hasRanged = false;
    protected boolean hasAreaAttack = false;
    protected boolean hasLightningReflexes = false;
    protected boolean hasConcealed = false;
    protected boolean hasPillage = false;
    protected boolean hasBattleReady = false;
    protected boolean hasRally = false;
    protected boolean hasBluff = false;
    protected boolean hasUnstable = false;
    protected boolean hasFiredUp = false;
    protected boolean hasDrawCard = false;
    protected boolean hasWarmBlooded = false;
    protected boolean hasLastWord = false;
    protected boolean hasQuick = false;
    protected boolean hasArmorBreak = false;
    protected boolean hasSetFire = false;


    protected boolean isSelected = false;
    protected boolean isInHeroHand = false;
    public boolean isOnHeroBoard = false;
    public boolean isOnEnemyBoard = false;

    // empty constructor - necessary for firebase db object loading
    public Card() {
    }

    // constructor with all attributes
    public Card(String cardId, String cardName, String cardText, String imageId, int manaCost, int attackValue, int lifeValue, int defenseValue, boolean hasOverPower, boolean hasDefender, boolean hasRanged, boolean hasAreaAttack, boolean hasLightningReflexes, boolean hasConcealed, boolean hasPillage, boolean hasBattleReady, boolean hasRally, boolean hasBluff, boolean hasUnstable, boolean hasFiredUp, boolean hasDrawCard, boolean hasWarmBlooded, boolean hasLastWord, boolean hasQuick, boolean hasArmorBreak, boolean hasSetFire) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.cardText = cardText;
        this.imageId = imageId;
        this.manaCost = manaCost;
        this.attackValue = attackValue;
        this.lifeValue = lifeValue;
        this.defenseValue = defenseValue;
        this.hasOverPower = hasOverPower;
        this.hasDefender = hasDefender;
        this.hasRanged = hasRanged;
        this.hasAreaAttack = hasAreaAttack;
        this.hasLightningReflexes = hasLightningReflexes;
        this.hasConcealed = hasConcealed;
        this.hasPillage = hasPillage;
        this.hasBattleReady = hasBattleReady;
        this.hasRally = hasRally;
        this.hasBluff = hasBluff;
        this.hasUnstable = hasUnstable;
        this.hasFiredUp = hasFiredUp;
        this.hasDrawCard = hasDrawCard;
        this.hasWarmBlooded = hasWarmBlooded;
        this.hasLastWord = hasLastWord;
        this.hasQuick = hasQuick;
        this.hasArmorBreak = hasArmorBreak;
        this.hasSetFire = hasSetFire;
    }

    // enum if it ends up used
    /*
 public CardType getCardType(){
     return cardType;
 } */

    public boolean isAttackedThisTurn() {
        return attackedThisTurn;
    }

    public void setAttackedThisTurn(boolean attackedThisTurn) {
        this.attackedThisTurn = attackedThisTurn;
    }
/*
    public Bitmap getCardImage() {
        return cardImage;
    }

    public void setCardImage(Bitmap cardImage) {
        this.cardImage = cardImage;
    } */

    public boolean isPlayedThisTurn() {
        return playedThisTurn;
    }

    public void setPlayedThisTurn(boolean playedThisTurn) {
        this.playedThisTurn = playedThisTurn;
    }

    public boolean hasAbility() {
        if (hasArmorBreak || hasAreaAttack || hasBattleReady || hasBluff || hasConcealed || hasDefender || hasDrawCard || hasFiredUp || hasLastWord || hasLightningReflexes || hasOverPower
                || hasPillage || hasRanged || hasRally || hasUnstable || hasWarmBlooded || hasQuick || hasSetFire) {
        return true;
        }
        return false;
    }
    /*
    public void playSpell(Card heroCard, Card enemyCard, Deck heroBoard, Deck enemyBoard) {
// add methods yo
    } */

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getEffectiveManaCost() {
        return effectiveManaCost;
    }

    public void setEffectiveManaCost(int effectiveManaCost) {
        this.effectiveManaCost = effectiveManaCost;
    }

    public boolean isInHeroHand() {
        return isInHeroHand;
    }

    public void setInHeroHand(boolean inHeroHand) {
        isInHeroHand = inHeroHand;
    }

    public boolean isOnHeroBoard() {
        return isOnHeroBoard;
    }

    public void setOnHeroBoard(boolean onHeroBoard) {
        isOnHeroBoard = onHeroBoard;
    }

    public boolean isOnEnemyBoard() {
        return isOnEnemyBoard;
    }

    public void setOnEnemyBoard(boolean onEnemyBoard) {
        isOnEnemyBoard = onEnemyBoard;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCardId() {
        return cardId;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }


    public int getLifeValue() {
        return lifeValue;
    }

    public void setLifeValue(int lifeValue) {
        this.lifeValue = lifeValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    public String getCardName() {
        return cardName;
    }


    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

    public int getAttackNumber() {
        return attackNumber;
    }

    public void setAttackNumber(int attackNumber) {
        this.attackNumber = attackNumber;
    }

    public int getDefenseNumber() {
        return defenseNumber;
    }

    public void setDefenseNumber(int defenseNumber) {
        this.defenseNumber = defenseNumber;
    }

    public int getTakenDamageNumber() {
        return takenDamageNumber;
    }

    public void setTakenDamageNumber(int takenDamageNumber) {
        this.takenDamageNumber = takenDamageNumber;
    }

    public boolean isHasOverPower() {
        return hasOverPower;
    }

    public void setHasOverPower(boolean hasOverPower) {
        this.hasOverPower = hasOverPower;
    }

    public boolean isHasDefender() {
        return hasDefender;
    }

    public void setHasDefender(boolean hasDefender) {
        this.hasDefender = hasDefender;
    }

    public boolean isHasRanged() {
        return hasRanged;
    }

    public void setHasRanged(boolean hasRanged) {
        this.hasRanged = hasRanged;
    }

    public boolean isHasAreaAttack() {
        return hasAreaAttack;
    }

    public void setHasAreaAttack(boolean hasAreaAttack) {
        this.hasAreaAttack = hasAreaAttack;
    }

    public boolean isHasLightningReflexes() {
        return hasLightningReflexes;
    }

    public void setHasLightningReflexes(boolean hasLightningReflexes) {
        this.hasLightningReflexes = hasLightningReflexes;
    }

    public boolean isHasConcealed() {
        return hasConcealed;
    }

    public void setHasConcealed(boolean hasConcealed) {
        this.hasConcealed = hasConcealed;
    }

    public boolean isHasPillage() {
        return hasPillage;
    }

    public void setHasPillage(boolean hasPillage) {
        this.hasPillage = hasPillage;
    }

    public boolean isHasBattleReady() {
        return hasBattleReady;
    }

    public void setHasBattleReady(boolean hasBattleReady) {
        this.hasBattleReady = hasBattleReady;
    }

    public boolean isHasRally() {
        return hasRally;
    }

    public void setHasRally(boolean hasRally) {
        this.hasRally = hasRally;
    }

    public boolean isHasBluff() {
        return hasBluff;
    }

    public void setHasBluff(boolean hasBluff) {
        this.hasBluff = hasBluff;
    }

    public boolean isHasUnstable() {
        return hasUnstable;
    }

    public void setHasUnstable(boolean hasUnstable) {
        this.hasUnstable = hasUnstable;
    }

    public boolean isHasFiredUp() {
        return hasFiredUp;
    }

    public void setHasFiredUp(boolean hasFiredUp) {
        this.hasFiredUp = hasFiredUp;
    }

    public boolean isHasDrawCard() {
        return hasDrawCard;
    }

    public void setHasDrawCard(boolean hasDrawCard) {
        this.hasDrawCard = hasDrawCard;
    }

    public boolean isHasWarmBlooded() {
        return hasWarmBlooded;
    }

    public void setHasWarmBlooded(boolean hasWarmBlooded) {
        this.hasWarmBlooded = hasWarmBlooded;
    }

    public boolean isHasLastWord() {
        return hasLastWord;
    }

    public void setHasLastWord(boolean hasLastWord) {
        this.hasLastWord = hasLastWord;
    }

    public boolean isHasQuick() {
        return hasQuick;
    }

    public void setHasQuick(boolean hasQuick) {
        this.hasQuick = hasQuick;
    }

    public boolean isHasArmorBreak() {
        return hasArmorBreak;
    }

    public void setHasArmorBreak(boolean hasArmorBreak) {
        this.hasArmorBreak = hasArmorBreak;
    }

    public boolean isHasSetFire() {
        return hasSetFire;
    }

    public void setHasSetFire(boolean hasSetFire) {
        this.hasSetFire = hasSetFire;
    }

}
