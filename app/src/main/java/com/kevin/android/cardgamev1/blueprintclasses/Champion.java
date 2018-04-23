package com.kevin.android.cardgamev1.blueprintclasses;


import java.io.Serializable;

public abstract class Champion implements Serializable {

   protected int life;
   protected int defense;

    public int getImageId() {
        return imageId;
    }

    protected int imageId;

    // add Weapon object

    String championType;

    public abstract void championAbility();

    public Champion(int life, String championType) {
        this.life = life;
        this.championType = championType;
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

    public String getChampionType() {
        return championType;
    }

    public void setChampionType(String championType) {
        this.championType = championType;
    }
}
