package com.kevin.android.cardgamev1.creationtools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.creationtools.cards.CardDatabase;
import com.kevin.android.cardgamev1.creationtools.champions.ChampionDatabase;

public class CreatorMain extends AppCompatActivity {

    Button cardButton;
    Button championButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_main);

        cardButton = (Button)findViewById(R.id.card_creator_button);
        championButton = (Button)findViewById(R.id.champion_creator_button);

        cardButton.setOnClickListener(onClickListener);
        championButton.setOnClickListener(onClickListener);



    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.card_creator_button:
                    Intent cardCreation = new Intent(CreatorMain.this, CardDatabase.class);
                    startActivity(cardCreation);
                    break;
                case R.id.champion_creator_button:
                    Intent championCreation = new Intent(CreatorMain.this, ChampionDatabase.class);
                    startActivity(championCreation);
                    break;
            }
        }
    };

}
