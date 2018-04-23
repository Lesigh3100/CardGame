package com.kevin.android.cardgamev1.battle;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.DragEvent;

import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;


import android.widget.ImageView;


import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.kevin.android.cardgamev1.blueprintclasses.Card;
import com.kevin.android.cardgamev1.Constants;
import com.kevin.android.cardgamev1.blueprintclasses.Deck;
import com.kevin.android.cardgamev1.blueprintclasses.Player;
import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.blueprintclasses.PlayerContainer;
import com.kevin.android.cardgamev1.cards.Ghost;
import com.kevin.android.cardgamev1.cards.Peon;
import com.kevin.android.cardgamev1.champions.Peasant;
import com.kevin.android.cardgamev1.tools.ScreenTools;
import com.kevin.android.cardgamev1.tools.Serializer;
import com.kevin.android.cardgamev1.tools.Toasts;


import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import java.io.IOException;


// Next Steps
// Populate the Cards in Hand with first 5-6 cards of Deck
// Be able to move card in Hand to Board
// Be able to attack Champion
// Be able to attack another card
// Be able to end turn
// Test timer
// Be able to play full first dummy game
//
// Populate Library
// Create Decks in Lib
// Play game with Lib Deck
// End of prototype
// get game online
// PARTY PARTY PARTY

public class Battle extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RoomStatusUpdateListener, RoomUpdateListener, RealTimeMessageReceivedListener {

    private GoogleApiClient mGoogleApiClient;
    private boolean mResolvingConnectionFailure = false;
    public final static int RC_SELECT_PLAYERS = 9005;
    private final static int RC_SIGN_IN = 9001;


    private final char[] SENDING_CODES = new char[]{'G', 'T', 'D'};
    private final int TURN_ONE = 1;
    private final int TURN_TWO = 2;

    private final int MAX_CARDS_HAND = 8;
    private final int MAX_CARDS_BOARD = 6;
    private boolean roomLeader = false;
    private boolean firstTurn = true;
    private boolean iSetupGame = false;
    private boolean myTurn = false;
    private boolean didSetup = false;
    private Deck myDeck;
    private int mCurrentScreen;
    private Room myRoom;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[2];
    private String TAG = "LOG !!!!!!!";
    private int turnCount = 0;
    private int currentPlayerTurn;
    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;



    Context mContext;
    TextView countDownTimer;
    Button endTurnButton;
    TextView enemyPlayerName;
    TextView enemyChampionType;
    TextView enemyLife;
    TextView enemyMana;
    TextView enemyCardsInHand;
    TextView enemyCardsInDeck;
    TextView enemyCardsInBurn;
    TextView enemyChampionPower;
    TextView enemyChampionWeapon;
    ImageView enemyChampion;

    // hero board views
    TextView heroPlayerName;
    TextView heroChampionType;
    TextView heroLife;
    TextView heroMana;
    TextView heroCardsInHand;
    TextView heroCardsInDeck;
    TextView heroCardsInBurn;
    TextView heroChampionPower;
    TextView heroChampionWeapon;

    GridView heroHand;
    GridView heroBoard;
    GridView enemyBoard;

    ViewGroup highlightedLayout;
    ImageView highlightedButton;
    TextView highlightedLife;
    TextView highlightedAttack;
    ImageView highlightedSword;
    ImageView highlightedHeart;

    ViewGroup highlightedLayoutForHand;
    ImageView highlightedButtonForHand;
    TextView highlightedLifeForHand;
    TextView highlightedAttackForHand;
    ImageView highlightedSwordForHand;
    ImageView highlightedHeartForHand;

    ConflictResolution conflictResolution;

    CardSlotAdapter heroBoardAdapter;
    CardSlotAdapter heroHandAdapter;
    CardSlotAdapter enemyBoardAdapter;

    // not sure about this one - change to viewgroup if used
    ViewGroup heroHandContainer;
    ViewGroup heroBoardContainer;
    ViewGroup enemyBoardContainer;

    private CountDownTimer turnTimer;

    Player heroPlayer;
    Player enemyPlayer;

    Player emptyPlayer;

    int playerNumber = -1;

    PlayerContainer playerContainer;


    // arbitrary request code for the waiting room UI.
// This can be any integer that's unique in your Activity.
    final static int RC_WAITING_ROOM = 10002;
    final static int CUSTOM_WAITING_ROOM = 11005;
    String mRoomId = null;

    // loading screen views
    TextView loadingScreenHeroText;
    TextView loadingScreenEnemyText;
    ImageView loadingScreenHeroImage;
    ImageView loadingScreenEnemyImage;

    final static int[] SCREENS = {
            R.id.battle_main, R.id.waiting_screen
    };


    // START OF DUMMY DATA / FUNCTIONS
    Deck dumbDeck1;
    Deck dumbDeck2;
    private Peasant peasant = new Peasant();
    ArrayList<Card> dummyList = new ArrayList<>();
    ArrayList<Card> dummyList2 = new ArrayList<>();
//    Player heroPlayer = new Player("Bob", peasant, player1Deck, 0, 20);
    //   Player enemyPlayer = new Player("BobsFriend", peasant, player2Deck, 0, 20);

    private void populateGrid() {
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());
        dummyList.add(new Peon());

        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
        dummyList2.add(new Ghost());
    }
    // END OF DUMMY DUMB STUFF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_battle);
        setupUi();
        mContext = this;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other apis needed here
                .build();

        // myDeck = new Deck(getIntent().getExtras ... etc )

        //DUMMY STUFF is ILAN
        populateGrid();


        myDeck = new Deck(dummyList);
        dumbDeck2 = new Deck(dummyList2);


        heroPlayer = new Player("bob", peasant, myDeck, 0, 20);
        enemyPlayer = new Player("bobs friend", peasant, dumbDeck2, 0, 20);

        // these are not actually used
        heroPlayer.setActiveDeck(heroPlayer.getStartingDeck());
        enemyPlayer.setActiveDeck(enemyPlayer.getStartingDeck());

        // stand in for when enemyPlayer is set to nothing before receiving enemies info
        //   emptyPlayer = new Player("emptyplayer", peasant, dumbDeck2, 0, 1);
        //   emptyPlayer.setActiveDeck(enemyPlayer.getStartingDeck());
        //        initilizeEmptyDecks(emptyPlayer);

        initilizeEmptyDecks(heroPlayer);
        initilizeEmptyDecks(enemyPlayer);

        //END OF STUFF

        // initilize grids with their adapters before game starts

        initilizeGrids();

        // changed conflictresolution to take in hero & enemy players, might need to modify it so methods take them in instead
        conflictResolution = new ConflictResolution(heroPlayer, enemyPlayer);

        if (mGoogleApiClient.isConnected()) {
            createRoom();
        } else {
            mGoogleApiClient.connect();
        }
    }


    private void createLoadingScreenUi() {
        loadingScreenHeroText = (TextView) findViewById(R.id.loading_screen_hero_name);
        loadingScreenHeroImage = (ImageView) findViewById(R.id.loading_screen_hero_image);
        loadingScreenEnemyText = (TextView) findViewById(R.id.loading_screen_enemy_name);
        loadingScreenEnemyImage = (ImageView) findViewById(R.id.loading_screen_enemy_image);
    }



    // add this to begin multiplayer query
    private void createRoom() {
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        // third long here is type selection, can pass in a long
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        roomConfigBuilder.setRoomStatusUpdateListener(this);
        roomConfigBuilder.setMessageReceivedListener(this);
        RoomConfig roomConfig = roomConfigBuilder.build();
        ScreenTools.keepScreenOn(this);

        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

        // go to game screen here
    }

    @Override
    public void onActivityResult(int request, int response, Intent data) {
        switch (request) {
            case RC_SELECT_PLAYERS:
                if (response != Activity.RESULT_OK) {

                    return;
                }

                // get the invitee list
                Bundle extras = data.getExtras();
                final ArrayList<String> invitees =
                        data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

                // get auto-match criteria
                Bundle autoMatchCriteria = null;
                int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
                int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

                if (minAutoMatchPlayers > 0) {
                    autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                            minAutoMatchPlayers, maxAutoMatchPlayers, 0);
                } else {
                    autoMatchCriteria = null;
                }

                // create the room and specify a variant if appropriate
                RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
                roomConfigBuilder.addPlayersToInvite(invitees);
                if (autoMatchCriteria != null) {
                    roomConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
                }
                RoomConfig roomConfig = roomConfigBuilder.build();
                Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);

                // prevent screen from sleeping during handshake
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                break;
            case RC_WAITING_ROOM:
                if (response == Activity.RESULT_OK) {
                    // (start game)
                    if (!iSetupGame) {
                        sendMyDeck();
                    }
                    // switch to main battle UI
                    switchToScreen(R.id.battle_main);
                } else if (response == Activity.RESULT_CANCELED) {
                    // Waiting room was dismissed with the back button. The meaning of this
                    // action is up to the game. You may choose to leave the room and cancel the
                    // match, or do something else like minimize the waiting room and
                    // continue to connect in the background.

                    // in this example, we take the simple approach and just leave the room:
                    Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (response == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player wants to leave the room.
                    Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                break;
        }
    }


    // create a RoomConfigBuilder that's appropriate for your implementation
    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder(this)
                .setRoomStatusUpdateListener(this);
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // show error message, return to main screen.
        }

        mRoomId = room.getRoomId();
        // get waiting room intent
      //  Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
     //   startActivityForResult(intent, RC_WAITING_ROOM);


    }

    public void createWaitingRoom(){


    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            //TODO
            // get room code info
            myRoom = room;

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // show error message, return to main screen.
        }
        mRoomId = room.getRoomId();
        // get waiting room intent
   //     Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
   //     startActivityForResult(i, RC_WAITING_ROOM);


    }

    @Override
    public void onRoomConnected(int statusCode, Room room) {
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            // let screen go to sleep
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            // show error message, return to main screen.
        }
        if (shouldStartGame(room)) {
       //     iSetupGame = shouldSetup(room);
        }
    }

    // are we already playing?
    boolean mPlaying = false;

    // at least 2 players required for our game
    final static int MIN_PLAYERS = 2;

    // returns whether there are enough players to start the game
    boolean shouldStartGame(Room room) {
        int connectedPlayers = 0;
        for (Participant p : room.getParticipants()) {
            if (p.isConnectedToRoom()) ++connectedPlayers;
        }
        return connectedPlayers >= MIN_PLAYERS;
    }

    // Returns whether the room is in a state where the game should be canceled.
    boolean shouldCancelGame(Room room) {
        return room.getParticipants().size() < 2;
        // TODO: Your game-specific cancellation logic here. For example, you might decide to
        // cancel the game if enough people have declined the invitation or left the room.
        // You can check a participant's status with Participant.getStatus().
        // (Also, your UI should have a Cancel button that cancels the game too)
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        Log.v(TAG, "onPeersConnected Called");

        // potentially re issue game info here for player reconnecting

        /*   if (mPlaying) {
            // add new player to an ongoing game
        } else if (shouldStartGame(room)) {

            } */
        iSetupGame = shouldSetup(room);
        if (!iSetupGame) {
            sendMyDeck();
        }
        // switch to main battle UI
        switchToScreen(R.id.battle_main);
    }

    private boolean shouldSetup(Room room) {
        Log.v(TAG, "SHOULD SETUP CALLED");
        // gets your ID + opponents ID, sorts them, then has whoever sorted to ids[0] run setup to choose who goes first, then handles broadcasting decks
        String myId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));
        ArrayList<String> ids = room.getParticipantIds();
        mParticipants = room.getParticipants();

        Collections.sort(ids, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        if (myId.equals(ids.get(0))) {
            playerNumber = 1;
            Log.v(TAG, "I AM ROOM LEADER");
        } else {
            playerNumber = 2;
            Log.v(TAG, "I AM NOT ROOM LEADER");
        }
        return myId.equals(ids.get(0));
    }


    private void deckSetup(Player enemyPlayer) {
        myDeck = new Deck(dummyList);
        //   dumbDeck2 = new Deck(dummyList2);
        heroPlayer = new Player("bob", peasant, myDeck, 0, 20);
        // enemyPlayer = new Player("bobs friend", peasant, dumbDeck2, 0, 20);
        heroPlayer.setActiveDeck(heroPlayer.getStartingDeck());
        // enemyPlayer.setActiveDeck(enemyPlayer.getStartingDeck());

        initilizeEmptyDecks(heroPlayer);
        initilizeEmptyDecks(enemyPlayer);
    }

    private void updatePlayerContainer() {

        playerContainer.setPlayer1(heroPlayer);
        playerContainer.setPlayer2(enemyPlayer);

    }

    private void sendMyDeck() {


        playerContainer = new PlayerContainer(heroPlayer);
        Log.v(TAG, "SEND MY DECK CALLED");
        try {
            byte[] sendDeck = Serializer.convertToBytes(playerContainer);
            // serialization currently working correctly
       //     checkSerialization(sendDeck);
            for (Participant p : mParticipants) {
                if (!p.getParticipantId().equals(mMyId)) {
                    Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, sendDeck, mRoomId, p.getParticipantId());
                }
            }
        } catch (IOException io) {
            Log.v(TAG, "IO EXCEPTION, " + io.toString());
        }
    }

    private void sendMyInfo() {
        updatePlayerContainer();
        try {
            byte[] sendHero = Serializer.convertToBytes(playerContainer);
            for (Participant p : mParticipants) {
                if (!p.getParticipantId().equals(mMyId)) {
                    Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, sendHero, mRoomId, p.getParticipantId());
                }
            }
            //     checkSerialization(sendHero);
        } catch (IOException io) {
            Log.v(TAG, "IO EXCEPTION, " + io.toString());
        }
    }

    // Tests that the serializer is properly encoding & decoding the playercontainer object
    private void checkSerialization(byte[] bytes) {
        PlayerContainer playerUnitTest;
        try {
            playerUnitTest = (PlayerContainer) Serializer.convertFromBytes(bytes);
            Log.v(TAG, "PLAYER UNIT TEST!");
            Log.v(TAG, "PLAYER NAME = " + playerUnitTest.getPlayer1().getPlayerName());
            Log.v(TAG, "PLAYER'S CHAMPION= " + playerUnitTest.getPlayer1().getChampion().getChampionType());
            Log.v(TAG, "PLAYER FIRST CARD NAME = " + playerUnitTest.getPlayer1().getActiveDeck().getCards().get(0).getCardName());
        } catch (ClassNotFoundException ce) {
            Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA CLASSNOTFOUND EXCEPTION");
        } catch (IOException io) {
            Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA IO EXCEPTION");
        }
    }

    private void checkSerializationSecondPlayer(byte[] bytes) {
        PlayerContainer playerUnitTest;
        try {
            playerUnitTest = (PlayerContainer) Serializer.convertFromBytes(bytes);
            Log.v(TAG, "PLAYER UNIT TEST FOR PLAYER 2!");
            Log.v(TAG, "PLAYER NAME = " + playerUnitTest.getPlayer2().getPlayerName());
            Log.v(TAG, "PLAYER'S CHAMPION= " + playerUnitTest.getPlayer2().getChampion().getChampionType());
            Log.v(TAG, "PLAYER FIRST CARD NAME = " + playerUnitTest.getPlayer2().getActiveDeck().getCards().get(0).getCardName());
            if (playerUnitTest.getPlayer2().getDeckBoard().getCards().size() > 0) {
                Log.v(TAG, "CARDS ON BOARD ARRAY = " + playerUnitTest.getPlayer2().getDeckBoard().getCards().get(0).getCardName());
            } else Log.v(TAG, "NO CARDS IN OPPONENT BOARD ARRAY :(");
        } catch (ClassNotFoundException ce) {
            Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA CLASSNOTFOUND EXCEPTION");
        } catch (IOException io) {
            Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA IO EXCEPTION");
        }
    }

    // the room leader broadcasts to the other player who will go first
    private void broadCastFirstTurn(int currentPlayerTurn) {
        if (currentPlayerTurn == 1) {
            mMsgBuf[0] = 'T';
            mMsgBuf[1] = 'M';
            turn();
        } else if (currentPlayerTurn == 2) {
            mMsgBuf[0] = 'T';
            mMsgBuf[1] = 'U';
        }
        for (Participant p : mParticipants) {
            if (!p.getParticipantId().equals(mMyId)) {
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, mMsgBuf, mRoomId, p.getParticipantId());
            }
        }

    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        if (shouldCancelGame(room)) {
            // cancel the game
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            finish();
        }
    }

    @Override
    public void onPeerLeft(Room room, List<String> peers) {
        // peer left -- see if game should be canceled
        if (shouldCancelGame(room)) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            finish();
        }
    }

    @Override
    public void onPeerDeclined(Room room, List<String> peers) {
        // peer declined invitation -- see if game should be canceled
        if (shouldCancelGame(room)) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            finish();
        }
    }

    //TODO finish this with incoming codes
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
// get bytes and update game pieces here
        byte[] buf = realTimeMessage.getMessageData();
        String sender = realTimeMessage.getSenderParticipantId();
        Log.d(TAG, "Message received: " + (char) buf[0] + "/" + (int) buf[1]);
        PlayerContainer incomingPlayerContainer;
        //TODO fill in code to receieve messages

        // T is for turn - start your turn
        // U = your turn, M = my turn

        // F = remove first turn flag

        // default is to receive player container object
        switch (buf[0]) {
            case 'T':
                if (buf[1] == 'U') {
                    Log.v(TAG, "MY TURN RECEIVED");
                    turn();
                } else if (buf[1] == 'M') {
                    // code here for waiting for your turn
                    Log.v(TAG, "NOT MY TURN RECEIVED");
                    firstTurn = false;
                }
                break;
            default:
                try {
                    incomingPlayerContainer = (PlayerContainer) Serializer.convertFromBytes(buf);
                    // if it doesn't include second player's info - create player container with both players & send it to other player, then run setup
                    if (firstTurn && iSetupGame) {
                        firstTurn = false;

                        enemyPlayer = incomingPlayerContainer.getPlayer1();
                        playerContainer = new PlayerContainer(heroPlayer, enemyPlayer);
                        //   Log.v(TAG, enemyPlayer.getPlayerName());
                        //   Log.v(TAG, playerContainer.getPlayer1().getPlayerName());

                        if (iSetupGame && !didSetup) {
                            didSetup = true;
                            sendMyInfo();
                            setup();
                        }
                    } else {
                        PlayerContainer container = new PlayerContainer(incomingPlayerContainer.getPlayer2(), incomingPlayerContainer.getPlayer1());
                        if (container.getPlayer2().getDeckBoard().getCards().size() > 0){
                            Log.v(TAG, "CARDS ON ENEMY BOARD = " + container.getPlayer2().getDeckBoard().getCards().get(0).getCardName());
                        } else  {
                            Log.v(TAG, "NO CARDS FOUND IN ENEMY BOARD CONTAINER");
                        }

                        updatePlayers(container);
                        updatePlayersUi();
                    }
                } catch (ClassNotFoundException ce) {
                    Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA CLASSNOTFOUND EXCEPTION");
                } catch (IOException io) {
                    Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA IO EXCEPTION");
                }

                //TODO fix this to be useful
                /*
                if (firstTurn){
                    firstTurn = false;
                    byte[] bytes7 = new byte[2];
                    bytes7[0] = 'F';
                    // not actually using this flag yet
                    bytes7[1] = 'S';
                    for (Participant p : mParticipants) {
                        if (!p.getParticipantId().equals(mMyId)) {
                            Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, bytes7, mRoomId, p.getParticipantId());
                        }
                    }
                    break;
                } */

                break;
            /*
            case 'D':
                // receive opponents deck & make a playerContainer
                try{
                    enemyPlayer = (Player)Serializer.deserialize(buf);
                    Log.v(TAG, "SERIALIZED ENEMY PLAYER!");
                }catch (ClassNotFoundException ce){
                    Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA CLASSNOTFOUND EXCEPTION");
                } catch (IOException io){
                    Log.v(TAG, "FAILED TO DESERIALIZE GAME DATA IO EXCEPTION");
                }
                playerContainer = new PlayerContainer(heroPlayer, enemyPlayer);
                sendGameData(playerContainer, sendingCodes('G'));
                if (iSetupGame){
                    setup();
                }
               break; */

        }


    }

    private void updatePlayers(PlayerContainer container) {
        heroPlayer.setLife(container.getPlayer1().getLife());
        enemyPlayer.setLife(container.getPlayer2().getLife());


        heroHandAdapter.updateData(container.getPlayer1().getDeckHand().getCards());
        heroBoardAdapter.updateData(container.getPlayer1().getDeckBoard().getCards());
        // this doesn't seem to be working
        enemyBoardAdapter.updateData(container.getPlayer2().getDeckBoard().getCards());



        // this is working better but not perfect
        enemyBoardAdapter.clear();
        enemyBoardAdapter.addAll(container.getPlayer2().getDeckBoard().getCards());

        if (enemyBoardAdapter.cards.size() > 0){
            Log.v(TAG, "ENEMY BOARD ADAPTER CARD LIST AT 0 = " +  enemyBoardAdapter.getItem(0).getCardName());
        } else {
            Log.v(TAG, "ENEMY BOARD HAS NO CARDS HEEEEEELP");
        }


        // not sure if this is necessary
        /*
        heroPlayer.setActiveDeck(container.getPlayer1().getActiveDeck());
        heroPlayer.setDeckHand(container.getPlayer1().getDeckHand());
        heroPlayer.setDeckBoard(container.getPlayer1().getDeckBoard());
        heroPlayer.setDeckDiscard(container.getPlayer1().getDeckDiscard());
        
        enemyPlayer.setActiveDeck(container.getPlayer2().getActiveDeck());
        enemyPlayer.setDeckHand(container.getPlayer2().getDeckHand());
        enemyPlayer.setDeckBoard(container.getPlayer2().getDeckBoard());
        enemyPlayer.setDeckDiscard(container.getPlayer2().getDeckDiscard()); */
    }

    private void requestInfoResend() {

    }

    @Override
    public void onRoomAutoMatching(Room room) {

    }

    @Override
    public void onLeftRoom(int i, String s) {
        finish();
    }

    @Override
    public void onRoomConnecting(Room room) {

    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {

    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {

    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.v(TAG, "ONCONNECTED TO ROOM CALLED");
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));

        if (mRoomId == null) {
            mRoomId = room.getRoomId();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        Log.v(TAG, "CONNECTION CALLBACK CALLED");
        createRoom();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "onConnectionSuspended Called");
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        // leave the room
        Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);

        // clear the flag that keeps the screen on
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // show error message and return to main screen
        Toasts.toastInput(mContext, "Disconnected from the Game.");
        finish();
    }

    @Override
    public void onP2PConnected(String s) {
        Log.v(TAG, "onP2PConnected Called");
    }

    @Override
    public void onP2PDisconnected(String s) {
        Log.v(TAG, "onP2PDisconnected Called");
    }

    //TODO assign heroPlayer & enemyPlayer from gameroom IDs
    private void setup() {
        // Determine who goes first, Determine hand/draw
        double coinFlip = Math.random();
        if (coinFlip < 0.5) {
            // person running code starts
            Log.v(TAG, "I GO FIRST");
            broadCastFirstTurn(1);
        } else {
            // other player starts
            Log.v(TAG, "OTHER PLAYER GOES FIRST");
            broadCastFirstTurn(2);
        }
        // TBD
        //    startingHandChoice();

    }

    // fix this up for next version
    /*
    private void startingHandChoice () {


        //Change to take different num cards
        //Add changing cards

        //make 4 (number of starting cards) a constant
        chooseStartingCards(4,heroPlayer,player1Hand);
        chooseStartingCards(4,player2Deck,player2Hand);

    }
*/
    private int randomCardGen(int numCards) {
        return (int) (Math.random() * numCards);
    }

    /*
        private void chooseStartingCards(Player player){
            int numberStartingCards = 4;
            Card choices[] = new Card[numberStartingCards];
            for (int i=0;i<numberStartingCards;i++ ) {
                choices[i] = player.getCards().get(randomCardGen(player.getCards().size()));
                for(Card card:choices  )
                    player.getActiveDeck().moveFromDeckToDeck(player.getDeckHand(),card);
                }
        }
    */
    // the core of the gameplay loop
    private void turn() {
        Log.v(TAG, "TURN CALLED");
        firstTurn = false;
        endTurnButton.setVisibility(View.VISIBLE);
        countDownTimer.setVisibility(View.VISIBLE);
        heroListenersOn();

        //show whose turn it is
        increaseMana(heroPlayer);
        heroPlayer.setCurrentMana(heroPlayer.getMana());
        drawCard();
        updateCardsPreviouslyPlayedCards(heroPlayer);
        updateEnemyChampion(enemyPlayer);
        updatePlayersUi();
        turnCountDown();
        setEndTurnButton();
        // start player activity
    }

    private void heroListenersOff() {
        heroHand.setOnTouchListener(null);
        heroBoard.setOnTouchListener(null);
    }

    private void heroListenersOn() {
        heroHand.setOnTouchListener(new CardTouchListener());
        heroBoard.setOnTouchListener(new CardTouchListener());
    }


    private void sendTurnSwitch() {
        byte[] bytes3 = new byte[2];
        bytes3[0] = 'T';
        bytes3[1] = 'U';
        for (Participant p : mParticipants) {
            if (!p.getParticipantId().equals(mMyId)) {
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, bytes3, mRoomId, p.getParticipantId());
            }
        }
    }


    // resets playedThisTurn and attackedThisTurn boolean flags
    private void updateCardsPreviouslyPlayedCards(Player player) {
        for (Card card : player.getDeckBoard().getCards()) {
            card.setPlayedThisTurn(false);
            card.setAttackedThisTurn(false);
        }
    }

    // checks the gamestatus from conflictresolution and instantiates endgame if the gamestatus isn't continue
    private void checkEndGame() {
        if (conflictResolution.getGameStatus() != Constants.GameStatus.CONTINUE) {
            turnTimer.cancel();
            endGame();
        }
    }

    private void setEndTurnButton() {
        endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnTimer.cancel();

                //  highlightedLayoutForHand.setVisibility(View.INVISIBLE);
                //  highlightedLayout.setVisibility(View.INVISIBLE);
                //  heroBoard.setVisibility(View.VISIBLE);
                //  heroHand.setVisibility(View.VISIBLE);

                // SEND NEW PLAYER TURN HERE

                sendMyInfo();
                sendTurnSwitch();
                endTurnButton.setVisibility(View.INVISIBLE);
                countDownTimer.setVisibility(View.INVISIBLE);
                heroListenersOff();
            }
        });
    }

    // ends the game
    private void endGame() {
        String winnerName;
        switch (conflictResolution.getGameStatus()) {
            case DRAW:
                Toast.makeText(this, "It was a draw!", Toast.LENGTH_SHORT).show();
                leaveGame();
                this.finish();
                break;
            case HEROWIN:
                winnerName = heroPlayerName.toString();
                Toast.makeText(this, "Player " + winnerName + " wins!", Toast.LENGTH_SHORT).show();
                leaveGame();
                this.finish();
                break;
            case ENEMYWIN:
                winnerName = enemyPlayerName.toString();
                Toast.makeText(this, "Player " + winnerName + " wins!", Toast.LENGTH_SHORT).show();
                leaveGame();
                this.finish();
                break;
            case CONTINUE:
                break;
        }
    }

    // figures out who is current player then updates textviews on battle board, then updates card slots
    private void updatePlayersUi() {

        heroCardsInHand.setText(Integer.toString(heroPlayer.getDeckHand().getCards().size()) + " CiH");
        heroCardsInDeck.setText(Integer.toString(heroPlayer.getActiveDeck().getCards().size()) + " CiD");
        heroCardsInBurn.setText(Integer.toString(heroPlayer.getDeckDiscard().getCards().size()) + " CiB");
        enemyCardsInBurn.setText(Integer.toString(enemyPlayer.getDeckDiscard().getCards().size()) + " CiB");
        enemyCardsInDeck.setText(Integer.toString(enemyPlayer.getActiveDeck().getCards().size()) + " CiD");
        enemyCardsInHand.setText(Integer.toString(enemyPlayer.getDeckHand().getCards().size()) + " CiH");
        updateHeroUi();
        updateEnemyUi();

    }

    // sets enemy champion image drawable
    private void updateEnemyChampion(Player enemy) {
        enemyChampion.setImageDrawable(getResources().getDrawable(enemy.getChampion().getImageId()));
    }

    // takes in player & updates player stats
    private void updateHeroUi() {
        heroPlayerName.setText(heroPlayer.getPlayerName());
        heroChampionType.setText(heroPlayer.getChampion().getChampionType());
        heroLife.setText(Integer.toString(heroPlayer.getLife()) + "L");
        heroMana.setText(Integer.toString(heroPlayer.getCurrentMana()) + "M");
    }

    private void updateEnemyUi() {
        enemyPlayerName.setText(enemyPlayer.getPlayerName());
        enemyChampionType.setText(enemyPlayer.getChampion().getChampionType());
        enemyLife.setText(Integer.toString(enemyPlayer.getLife()) + "L");
        enemyMana.setText(Integer.toString(enemyPlayer.getCurrentMana()) + "M");
    }

    // links views with XML
    private void setupUi() {

        countDownTimer = (TextView) findViewById(R.id.countdown_timer);
        endTurnButton = (Button) findViewById(R.id.end_turn_button);
        endTurnButton.setVisibility(View.INVISIBLE);
        countDownTimer.setVisibility(View.INVISIBLE);
        // enemy board views
        enemyPlayerName = (TextView) findViewById(R.id.enemy_player_name);
        enemyChampionType = (TextView) findViewById(R.id.enemy_champion_type);
        enemyLife = (TextView) findViewById(R.id.enemy_life);
        enemyMana = (TextView) findViewById(R.id.enemy_mana);
        enemyCardsInHand = (TextView) findViewById(R.id.enemy_cih);
        enemyCardsInDeck = (TextView) findViewById(R.id.enemy_cid);
        enemyCardsInBurn = (TextView) findViewById(R.id.enemy_cib);
        enemyChampionPower = (TextView) findViewById(R.id.enemy_champion_power);
        enemyChampionWeapon = (TextView) findViewById(R.id.enemy_champion_weapon);
        enemyChampion = (ImageView) findViewById(R.id.enemy_champion);


        // hero board views
        heroPlayerName = (TextView) findViewById(R.id.hero_player_name);
        heroChampionType = (TextView) findViewById(R.id.hero_champion_type);
        heroLife = (TextView) findViewById(R.id.hero_life);
        heroMana = (TextView) findViewById(R.id.hero_mana);
        heroCardsInHand = (TextView) findViewById(R.id.hero_cih);
        heroCardsInDeck = (TextView) findViewById(R.id.hero_cid);
        heroCardsInBurn = (TextView) findViewById(R.id.hero_cib);
        heroChampionPower = (TextView) findViewById(R.id.hero_champion_power);
        heroChampionWeapon = (TextView) findViewById(R.id.hero_champion_weapon);


        // highlighted Card from Board
        highlightedLayout = (ViewGroup) findViewById(R.id.card_slot_highlighted);

        highlightedButton = (ImageView) findViewById(R.id.picture_slot_highlighted);
        highlightedAttack = (TextView) findViewById(R.id.on_card_attack_highlighted);
        highlightedLife = (TextView) findViewById(R.id.on_card_hp_highlighted);
        highlightedHeart = (ImageView) findViewById(R.id.heart_highlighted);
        highlightedSword = (ImageView) findViewById(R.id.sword_highlighted);

        // highlighted Card from Hand
        highlightedLayoutForHand = (ViewGroup) findViewById(R.id.card_slot_highlighted_for_hand);

        highlightedButtonForHand = (ImageView) findViewById(R.id.picture_slot_highlighted_for_hand);
        highlightedAttackForHand = (TextView) findViewById(R.id.on_card_attack_highlighted_for_hand);
        highlightedLifeForHand = (TextView) findViewById(R.id.on_card_hp_highlighted_for_hand);
        highlightedHeartForHand = (ImageView) findViewById(R.id.heart_highlighted_for_hand);
        highlightedSwordForHand = (ImageView) findViewById(R.id.sword_highlighted_for_hand);

        // Viewgroups that contain layouts holding grids
        heroHandContainer = (ViewGroup) findViewById(R.id.grid_hero_hand_layout);
        heroBoardContainer = (ViewGroup) findViewById(R.id.grid_hero_field_layout);
        enemyBoardContainer = (ViewGroup) findViewById(R.id.grid_enemy_field_layout);
    }


    private void initilizeGrids() {

        setHeroBoard();
        setHeroHand();
        setEnemyBoard();
    }

    // methods that use arrayadapters to put cards into gridviews
    private void setHeroBoard() {
        heroBoard = (GridView) findViewById(R.id.grid_hero_field);
        heroBoardAdapter = new CardSlotAdapter(Battle.this, heroPlayer.getDeckBoard().getCards(), heroPlayer);
        heroBoard.setAdapter(heroBoardAdapter);
    }


    //
    private void setHeroHand() {
        heroHand = (GridView) findViewById(R.id.grid_hero_hand);
        heroHandAdapter = new CardSlotAdapter(Battle.this, heroPlayer.getDeckHand().getCards(), heroPlayer);
        heroHand.setAdapter(heroHandAdapter);
    }


    private void setEnemyBoard() {
        enemyBoard = (GridView) findViewById(R.id.grid_enemy_field);
        enemyBoardAdapter = new CardSlotAdapter(Battle.this, enemyPlayer.getDeckBoard().getCards(), enemyPlayer);
        enemyBoard.setAdapter(enemyBoardAdapter);
    }

    private void checkMaxCardsAndDiscard() {
        int size = heroPlayer.getDeckBoard().getCards().size();
        if (size > 6) {
            for (int i = 6; i < size; i++) {
                Card card = heroPlayer.getDeckBoard().getCards().get(0);
                card.setOnHeroBoard(false);
                heroPlayer.getDeckBoard().moveFromDeckToDeck(heroPlayer.getDeckDiscard(), heroPlayer.getDeckBoard(), card);
            }
        }
        // already happens in draw card
        /*
        int sizeHand = heroPlayer.getDeckHand().getCards().size();
        if (sizeHand > 8) {
            for (int i = 8; i < sizeHand; i++) {
                Card card = heroPlayer.getDeckHand().getCards().get(randomCardGen(heroPlayer.getDeckHand().getCards().size()));
                card.setInHeroHand(false);
                heroPlayer.getDeckHand().moveFromDeckToDeck(heroPlayer.getDeckDiscard(), heroPlayer.getDeckHand(), card);
            }
        } */
    }


    // starts 60 second timer, switches players when time is up and calls turn to start next players turn
    private void turnCountDown() {
        turnTimer = new CountDownTimer(60000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimer.setText(Long.toString(millisUntilFinished / 1000));
                sendMyInfo();
            }

            @Override
            public void onFinish() {
                // notify other player that turn has ended, have other player re accept control - visible end turn button
                //// TODO: 7/26/2017  SWITCH TURN HERE
                sendTurnSwitch();
                sendMyInfo();
                countDownTimer.setVisibility(View.INVISIBLE);
                endTurnButton.setVisibility(View.INVISIBLE);
                heroListenersOff();
            }
        }.start();
    }

    // draws card from input players deck to hand
    private void drawCard() {
        Log.v(TAG, "DRAW CARD CALLED");
        Log.v(TAG, "CARDS IN DECK = " + Integer.toString(heroPlayer.getActiveDeck().getCards().size()));
        if (heroPlayer.getActiveDeck().getCards().size() > 0 && heroPlayer.getDeckHand().getCards().size() < MAX_CARDS_HAND) {
            Log.v(TAG, "DRAW CARD MOVE FROM DECK TO DECK");
            heroPlayer.getActiveDeck().moveFromDeckToDeck(heroPlayer.getDeckHand(), heroPlayer.getActiveDeck(), heroPlayer.getActiveDeck().getCards().get(randomCardGen(heroPlayer.getActiveDeck().getCards().size())));
            Log.v(TAG, "CARDS IN HERO HAND = " + heroPlayer.getDeckHand().getCards().size());
            heroHandAdapter.notifyDataSetChanged();
        } else if (heroPlayer.getActiveDeck().getCards().size() > 0 && heroPlayer.getDeckHand().getCards().size() >= MAX_CARDS_HAND) {
            heroPlayer.getActiveDeck().moveFromDeckToDeck(heroPlayer.getDeckDiscard(), heroPlayer.getActiveDeck(), heroPlayer.getActiveDeck().getCards().get(randomCardGen(heroPlayer.getActiveDeck().getCards().size())));
        }
    }

    // checks to make sure players mana isn't over 10 and adds a mana
    private void increaseMana(Player player) {
        int mana = player.getMana();
        if (mana < 10) {
            player.setMana(mana + 1);
        }
    }

    private void initilizeEmptyDecks(Player player) {
        ArrayList<Card> emptyInitilizer = new ArrayList<>();
        ArrayList<Card> emptyInitilizer2 = new ArrayList<>();
        ArrayList<Card> emptyInitilizer3 = new ArrayList<>();
        Deck emptyDeck = new Deck(emptyInitilizer);
        Deck emptyDeck2 = new Deck(emptyInitilizer2);
        Deck emptyDeck3 = new Deck(emptyInitilizer3);
        player.setDeckBoard(emptyDeck);
        player.setDeckDiscard(emptyDeck2);
        player.setDeckHand(emptyDeck3);
    }


    private class CardDragListener implements View.OnDragListener {
        Card cardPlayed;


        public CardDragListener(Card cardPlayed) {
            this.cardPlayed = cardPlayed;
        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            int count = 0;
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    count++;
                    Log.v("DRAGSTARTEDTIMES", Integer.toString(count));
                    final Point touchPositionStart = getTouchPositionFromDragEvent(view, dragEvent);
                    Log.v("CARDISONHEROBOARDORIG", Boolean.toString(cardPlayed.isOnHeroBoard()));
                    if (!cardPlayed.isOnHeroBoard && isTouchInsideOfView(heroHandContainer, touchPositionStart)) {
                        heroBoardContainer.setBackground(getResources().getDrawable(R.drawable.green_border));
                    } else if (cardPlayed.isOnHeroBoard && isTouchInsideOfView(heroBoardContainer, touchPositionStart)) {
                        enemyChampion.setBackground(getResources().getDrawable(R.drawable.red_border));
                        if (enemyPlayer.getDeckBoard().getCards().size() > 0) {
                            enemyBoardContainer.setBackground(getResources().getDrawable(R.drawable.orange_border));
                        }
                    }
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:

                    final Point touchPositionDrop = getTouchPositionFromDragEvent(view, dragEvent);
                    int xDrop = (int) dragEvent.getX();
                    int yDrop = (int) dragEvent.getY();
                    Log.v("CARDISONHEROBOARD", Boolean.toString(cardPlayed.isOnHeroBoard()));

                    // drop location is hero board - only receives it if the card is from hand
                    if (!cardPlayed.isOnHeroBoard && isTouchInsideOfView(heroBoardContainer, touchPositionDrop)) {
                        checkMaxCardsAndDiscard();
                        Log.v("DROPPEDHEROBOARD", "SUCCESS");
                        cardPlayed.setPlayedThisTurn(true);
                        cardPlayed.setOnHeroBoard(true);
                        heroPlayer.getDeckHand().moveFromDeckToDeck(heroPlayer.getDeckBoard(), heroPlayer.getDeckHand(), cardPlayed);
                        heroPlayer.setCurrentMana(heroPlayer.getCurrentMana() - cardPlayed.getEffectiveManaCost());
                        enemyBoardAdapter.notifyDataSetChanged();
                        heroHandAdapter.notifyDataSetChanged();
                        heroBoardAdapter.notifyDataSetChanged();

                        updatePlayersUi();
                        checkEndGame();
                        resetListeners();
                        sendMyInfo();
                        return true;
                    }
                    // drop location is
                    else if (cardPlayed.isOnHeroBoard && isTouchInsideOfView(enemyChampion, touchPositionDrop)) {
                        conflictResolution.attackingChampion(heroPlayer, enemyPlayer, cardPlayed);
                        enemyBoardAdapter.notifyDataSetChanged();
                        heroHandAdapter.notifyDataSetChanged();
                        heroBoardAdapter.notifyDataSetChanged();

                        //  updatePlayersUi(playerContainer.getPlayer1(), playerContainer.getPlayer2());
                        updatePlayersUi();
                        checkEndGame();
                        resetListeners();
                        Log.v("DROPPEDENEMYCHAMP", "SUCCESS");
                        sendMyInfo();
                        return true;
                    } else if (cardPlayed.isOnHeroBoard && isTouchInsideOfView(enemyBoardContainer, touchPositionDrop)) {
                        Log.v("DROPPEDENEMYBOARD", "SUCCESS");
                        Card enemyCard;
                        if (enemyBoard.pointToPosition(xDrop, yDrop) != -1) {
                            enemyCard = enemyBoardAdapter.getItem(enemyBoard.pointToPosition(xDrop, yDrop));
                            conflictResolution.attackingCard(heroPlayer, enemyPlayer, cardPlayed, enemyCard);
                        }
                        enemyBoardAdapter.notifyDataSetChanged();
                        heroHandAdapter.notifyDataSetChanged();
                        heroBoardAdapter.notifyDataSetChanged();
                        updatePlayersUi();
                        checkEndGame();
                        resetListeners();
                        sendMyInfo();
                        return true;
                    }
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    heroBoardContainer.setBackground(null);
                    enemyChampion.setBackground(null);
                    enemyBoardContainer.setBackground(null);
                    sendMyInfo();
                    return true;
            }
            return false;
        }

    }

    private class CardTouchListener implements View.OnTouchListener {


        public CardTouchListener() {
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();

            if (MotionEvent.ACTION_DOWN == action) {
                float currentXPosition = motionEvent.getX();
                float currentYPosition = motionEvent.getY();

                switch (view.getId()) {
                    case R.id.grid_hero_hand:
                        int position = heroHand.pointToPosition((int) currentXPosition, (int) currentYPosition);
                        if (position == -1 || !heroHandAdapter.isEnabled(position)) {
                            return false;
                        }
                        final View view1 = heroHand.getChildAt(position);
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view1);
                        Card selectedCardHand = heroHandAdapter.getItem(position);
                        view1.startDrag(null, shadowBuilder, view1, 0);
                        heroBoardContainer.setOnDragListener(new CardDragListener(selectedCardHand));
                        break;
                    case R.id.grid_hero_field:
                        int position2 = heroBoard.pointToPosition((int) currentXPosition, (int) currentYPosition);
                        if (position2 == -1 || !heroBoardAdapter.isEnabled(position2)) {
                            return false;
                        }
                        Card selectedCardBoard = heroBoardAdapter.getItem(position2);
                        final View view2 = heroBoard.getChildAt(position2);
                        View.DragShadowBuilder shadowBuilder1 = new View.DragShadowBuilder(view2);
                        view2.startDrag(null, shadowBuilder1, view2, 0);
                        enemyBoardContainer.setOnDragListener(new CardDragListener(selectedCardBoard));
                        enemyChampion.setOnDragListener(new CardDragListener(selectedCardBoard));
                        break;
                    default:
                        return false;
                }
            }

            return false;
        }
    }

    // gets the on screen touch x/y during drag event (drag event normally gives you x/y in relation to view it is listening to)
    public static Point getTouchPositionFromDragEvent(View view, DragEvent dragEvent) {

        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return new Point(rect.left + Math.round(dragEvent.getX()), rect.top + Math.round(dragEvent.getY()));
    }

    // compares x & y coordinates of drop location to coordinates of a view, if they are the same returns true
    public static Boolean compareTouchLocationToView(Point touchPosition, Rect rect) {
        return touchPosition.x > rect.left && touchPosition.x < rect.right
                && touchPosition.y > rect.top && touchPosition.y < rect.bottom;
    }

    // returns true if the point that it takes in is inside the given view
    public static boolean isTouchInsideOfView(View view, Point touchPosition) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return compareTouchLocationToView(touchPosition, rect);
    }

    private void resetListeners() {
        heroBoard.setOnDragListener(null);
        enemyBoard.setOnDragListener(null);
        enemyChampion.setOnDragListener(null);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already doing resolve process
            return;
        }
        if (!BaseGameUtils.resolveConnectionFailure(this,
                mGoogleApiClient, connectionResult,
                RC_SIGN_IN, R.string.signin_other_error)) {
            mResolvingConnectionFailure = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ScreenTools.stopKeepingScreenOn(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScreenTools.keepScreenOn(this);
    }

    private void leaveGame() {
        // leave room
        Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);

// remove the flag that keeps the screen on
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    void switchToScreen(int screenId) {
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
        }
        mCurrentScreen = screenId;
        // can add notifications here based on connectedness
    }

    @Override
    protected void onStart() {
        switchToScreen(R.id.waiting_screen);
        super.onStart();
    }
}





