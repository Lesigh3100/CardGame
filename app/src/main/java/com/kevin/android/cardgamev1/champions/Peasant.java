package com.kevin.android.cardgamev1.champions;


import com.kevin.android.cardgamev1.blueprintclasses.Champion;
import com.kevin.android.cardgamev1.R;

public class Peasant extends Champion {

    public Peasant() {
        super(20, "Peasant");
        imageId = R.drawable.peasants;
    }

    @Override
    public void championAbility() {
    }
}
