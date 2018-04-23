package com.kevin.android.cardgamev1;

import android.graphics.Bitmap;

public class CardToSave {


    protected String cardName;
    protected String cardText;
    protected String championName;
    protected String imageId;
    protected String cardType;
    protected int manaCost;

    protected int attack;
    protected int life;
    protected int defense;

    protected boolean hasQuick = false;
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
    protected boolean hasSetFire = false;
    protected boolean hasArmorBreak = false;

    protected Bitmap cardImage;


    public CardToSave() {
    }

    public CardToSave(String cardName, String cardText, String championName, String imageId, int manaCost, int attack, int life, int defense, boolean hasQuick, boolean hasOverPower, boolean hasDefender, boolean hasRanged, boolean hasAreaAttack, boolean hasLightningReflexes, boolean hasConcealed, boolean hasPillage, boolean hasBattleReady, boolean hasRally, boolean hasBluff, boolean hasUnstable, boolean hasFiredUp, boolean hasDrawCard, boolean hasWarmBlooded, boolean hasLastWord, boolean hasSetFire, boolean hasArmorBreak, Bitmap cardImage, String cardType) {
        this.cardName = cardName;
        this.cardText = cardText;
        this.championName = championName;
        this.imageId = imageId;
        this.manaCost = manaCost;
        this.attack = attack;
        this.life = life;
        this.defense = defense;
        this.hasQuick = hasQuick;
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
        this.hasSetFire = hasSetFire;
        this.hasArmorBreak = hasArmorBreak;
        this.cardImage = cardImage;
        this.cardType = cardType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
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

    public Bitmap getCardImage() {
        return cardImage;
    }

    public void setCardImage(Bitmap cardImage) {
        this.cardImage = cardImage;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
