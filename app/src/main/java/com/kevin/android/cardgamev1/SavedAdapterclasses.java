package com.kevin.android.cardgamev1;



public class SavedAdapterclasses {

    // methods that use arrayadapters to put cards into gridviews
    /*
    private void setHeroBoard(final Player hero, final Player opponent) {
        int size = hero.getDeckBoard().getCards().size();
        if (size > 6) {
            for (int i = 6; i < size; i++) {
                Card card = hero.getDeckBoard().getCards().get(0);
                card.setOnHeroBoard(false);
                hero.getDeckBoard().moveFromDeckToDeck(hero.getDeckDiscard(), hero.getDeckBoard(), card);
            }
        }

        heroBoardAdapter = new CardSlotAdapter(Battle.this, hero.getDeckBoard().getCards(), hero, opponent);
        heroBoard.setAdapter(heroBoardAdapter);

        // LONG CLICK LISTENER FOR DRAGGING / ATTACKING CARDS + CHAMPION
        heroBoard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.v("HEROBOARD LONG", "LOGGED"); // THIS WAS SUCCESSFUL BUT IT IMMEDIATELY GOES TO THE OTHER DRAGLIST5t6 ENER

                final Card selectedCard = heroBoardAdapter.getItem(position);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(null, shadowBuilder, view, 0);

                if (opponent.getDeckBoard().getCards().size() > 0 && hero.getDeckBoard().getCards().contains(selectedCard)) {
                    enemyBoardContainer.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent dragEvent) {
                            switch (dragEvent.getAction()) {
                                case DragEvent.ACTION_DRAG_STARTED:
                                    enemyBoardContainer.setBackground(getResources().getDrawable(R.drawable.orange_border));
                                    return true;
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    return true;
                                case DragEvent.ACTION_DRAG_EXITED:
                                    return true;
                                case DragEvent.ACTION_DROP:

                                    final float positionX = dragEvent.getX();
                                    final float positionY = dragEvent.getY();
                                    Card enemyCard;
                                    if (enemyBoard.pointToPosition((int) positionX, (int) positionY) != -1) {
                                        enemyCard = enemyBoardAdapter.getItem(enemyBoard.pointToPosition((int) positionX, (int) positionY));
                                        conflictResolution.attackingCard(hero, opponent, selectedCard, enemyCard);
                                    }
                                    heroHandAdapter.notifyDataSetChanged();
                                    heroBoardAdapter.notifyDataSetChanged();
                                    updatePlayersUi(hero, opponent);
                                    checkEndGame();
                                    return true;
                                case DragEvent.ACTION_DRAG_LOCATION:
                                    return true;
                                case DragEvent.ACTION_DRAG_ENDED:
                                    enemyBoardContainer.setBackground(null);
                                    enemyBoardContainer.setOnDragListener(null);

                                    return true;
                            }
                            return false;
                        }
                    });
                }
                if (hero.getDeckBoard().getCards().contains(selectedCard)) {
                    enemyChampion.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent dragEvent) {
                            switch (dragEvent.getAction()) {
                                case DragEvent.ACTION_DRAG_STARTED:
                                    enemyChampion.setBackground(getResources().getDrawable(R.drawable.red_border));
                                    return true;
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    return true;
                                case DragEvent.ACTION_DRAG_EXITED:
                                    return true;
                                case DragEvent.ACTION_DROP:
                                    conflictResolution.attackingChampion(hero, opponent, selectedCard);
                                    heroHandAdapter.notifyDataSetChanged();
                                    heroBoardAdapter.notifyDataSetChanged();
                                    updatePlayersUi(hero, opponent);
                                    checkEndGame();
                                    return true;
                                case DragEvent.ACTION_DRAG_LOCATION:
                                    return true;
                                case DragEvent.ACTION_DRAG_ENDED:
                                    enemyChampion.setBackground(null);
                                    enemyBoardContainer.setBackground(null);
                                    enemyChampion.setOnDragListener(null);
                                    return true;
                            }
                            return false;
                        }
                    });
                }
                return false;
            }
        });

        // ONCLICK LISTENER TO HIGHLIGHT CARD
        heroBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int position, long id) {
                final Card selectedCard = heroBoardAdapter.getItem(position);
                heroHand.setVisibility(View.INVISIBLE);
                heroBoard.setVisibility(View.INVISIBLE);
                highlightedLayout.setVisibility(View.VISIBLE);
                highlightedAttack.setText(Integer.toString(selectedCard.getAttackValue()));
                highlightedLife.setText(Integer.toString(selectedCard.getLifeValue()));
                if (selectedCard.getCardType().equals(Constants.CardTypes.CREATURE)) {
                    highlightedHeart.setVisibility(View.VISIBLE);
                    highlightedSword.setVisibility(View.VISIBLE);
                }
                highlightedButton.setImageResource(selectedCard.getImageId());
                highlightedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        highlightedLayout.setVisibility(View.INVISIBLE);
                        heroHand.setVisibility(View.VISIBLE);
                        heroBoard.setVisibility(View.VISIBLE);
                        highlightedAttack.setText(null);
                        highlightedLife.setText(null);
                        highlightedButton.setImageDrawable(null);
                    }
                });
            }
        });

    }
    */
    /*private void setHeroHand(final Player hero, final Player opponent) {
        int size = hero.getDeckHand().getCards().size();
        if (size > 8) {
            for (int i = 8; i < size; i++) {
                Card card = hero.getDeckHand().getCards().get(randomCardGen(hero.getDeckHand().getCards().size()));
                card.setInHeroHand(false);
                hero.getDeckHand().moveFromDeckToDeck(hero.getDeckDiscard(), hero.getDeckHand(), card);
            }
        }
        heroHandAdapter = new CardSlotAdapter(Battle.this, hero.getDeckHand().getCards(), hero, opponent);
        heroHand.setAdapter(heroHandAdapter);




        heroHand.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                final Card selectedCard = heroHandAdapter.getItem(i);

                view.startDrag(null, shadowBuilder, view, 0);
                    heroBoardContainer.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View view, DragEvent dragEvent) {
                            switch (dragEvent.getAction()) {
                                case DragEvent.ACTION_DRAG_STARTED:
                                    heroBoardContainer.setBackground(getResources().getDrawable(R.drawable.green_border));
                                    return true;
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    return true;
                                case DragEvent.ACTION_DRAG_EXITED:
                                    return true;
                                case DragEvent.ACTION_DROP:
                                        selectedCard.setPlayedThisTurn(true);
                                        selectedCard.setOnHeroBoard(true);
                                        hero.getDeckHand().moveFromDeckToDeck(hero.getDeckBoard(), hero.getDeckHand(), selectedCard);
                                        hero.setCurrentMana(hero.getCurrentMana() - selectedCard.getEffectiveManaCost());
                                        heroHandAdapter.notifyDataSetChanged();
                                        heroBoardAdapter.notifyDataSetChanged();
                                        updatePlayersUi(hero, opponent);
                                        checkEndGame();
                                    return true;
                                case DragEvent.ACTION_DRAG_LOCATION:
                                    return true;
                                case DragEvent.ACTION_DRAG_ENDED:
                                    heroBoardContainer.setBackground(null);
                                    heroBoardContainer.setOnDragListener(null);
                                    return true;
                            }
                            return false;
                        }
                    });

                return false;
            }
        });
        heroHand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int position, long id) {
                final Card selectedCard = heroHandAdapter.getItem(position);
                heroHand.setVisibility(View.INVISIBLE);
                highlightedLayoutForHand.setVisibility(View.VISIBLE);
                highlightedAttackForHand.setText(Integer.toString(selectedCard.getAttackValue()));
                highlightedLifeForHand.setText(Integer.toString(selectedCard.getLifeValue()));
                if (selectedCard.getCardType().equals(Constants.CardTypes.CREATURE)) {
                    highlightedHeartForHand.setVisibility(View.VISIBLE);
                    highlightedSwordForHand.setVisibility(View.VISIBLE);
                }

                highlightedButtonForHand.setImageResource(selectedCard.getImageId());



                // ONCLICK LISTENER - WORKS BUT BETTER TO USE TOUCH LISTENER TO HANDLE MOVEMENT

                highlightedButtonForHand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        highlightedLayoutForHand.setVisibility(View.INVISIBLE);
                        heroHand.setVisibility(View.VISIBLE);
                        highlightedAttackForHand.setText(null);
                        highlightedLifeForHand.setText(null);
                        highlightedButtonForHand.setImageDrawable(null);
                    }
                });


                // ONLONGCLICKLISTENER FOR DRAGGING CARDS
                highlightedButtonForHand.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);


                        //TODO CHECK RETURN VALUES IF THEY END ONDRAG

                        v.startDrag(null, shadowBuilder, view, 0);
                        heroBoardContainer.setOnDragListener(new View.OnDragListener() {
                            @Override
                            public boolean onDrag(View view, DragEvent dragEvent) {
                                switch (dragEvent.getAction()) {
                                    case DragEvent.ACTION_DRAG_STARTED:
                                        heroBoardContainer.setBackground(getResources().getDrawable(R.drawable.green_border));
                                        return true;
                                    case DragEvent.ACTION_DRAG_ENTERED:
                                        return true;
                                    case DragEvent.ACTION_DRAG_EXITED:
                                        return true;
                                    case DragEvent.ACTION_DROP:

                                        selectedCard.setPlayedThisTurn(true);
                                        selectedCard.setOnHeroBoard(true);
                                        hero.getDeckHand().moveFromDeckToDeck(hero.getDeckBoard(), hero.getDeckHand(), selectedCard);
                                        hero.setCurrentMana(hero.getCurrentMana() - selectedCard.getEffectiveManaCost());
                                        heroHandAdapter.notifyDataSetChanged();
                                        heroBoardAdapter.notifyDataSetChanged();
                                        return true;
                                    case DragEvent.ACTION_DRAG_LOCATION:
                                        return true;
                                    case DragEvent.ACTION_DRAG_ENDED:
                                        highlightedLayoutForHand.setVisibility(View.INVISIBLE);
                                        heroHand.setVisibility(View.VISIBLE);
                                        highlightedAttackForHand.setText(null);
                                        highlightedLifeForHand.setText(null);
                                        highlightedButtonForHand.setImageDrawable(null);
                                        updatePlayersUi(hero, opponent);
                                        heroBoardContainer.setBackground(null);
                                        heroBoardContainer.setOnDragListener(null);
                                        return true;
                                }
                                return false;
                            }
                        });
                        return false;
                    }

                });

            }
        });
    }


    private void setEnemyBoard(Player hero, Player opponent) {
        enemyBoardAdapter = new CardSlotAdapter(Battle.this, opponent.getDeckBoard().getCards(), hero, opponent);
        enemyBoard.setAdapter(enemyBoardAdapter);
    } */


}
