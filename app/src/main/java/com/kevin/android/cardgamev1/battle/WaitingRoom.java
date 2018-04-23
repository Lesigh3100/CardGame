package com.kevin.android.cardgamev1.battle;


import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.multiplayer.realtime.Room;

public interface WaitingRoom {


    Intent createWaitingRoomIntent (GoogleApiClient var1, Room var2, int var3);



}
