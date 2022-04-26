package com.phantasma.phantasmalinkclient;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PhantasmaLinkClientClass {
    public static String sessionIdIntent = "sessionId";
    private static String sessionId;
    private static Activity UnityActivity;
    private static final int TX_CODE = 10;
    private static final String WALLET_PACKAGE = "com.Phantasma.PoltergeistWallet"; // "com.phantasma.poltergeistmodule.MainActivity"
    private static final String WALLET_ACTIVITY = "com.phantasma.poltergeistmodule.MainActivity";
    private static PhantasmaLinkComponent phantasmaLinkComponent;

    public static void ReceiveActivity(Activity activity){

        UnityActivity = activity;
        phantasmaLinkComponent = new PhantasmaLinkComponent();

        //Intent test = new Intent(UnityActivity.getApplicationContext(), PhantasmaLinkComponent.class);
        //test.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        //UnityActivity.start(test);
    }

    public void SendTransaction(String tx){
        Intent sendTxIntent = new Intent(Intent.ACTION_SEND, Uri.parse(tx));
        sendTxIntent.setClassName(WALLET_PACKAGE, WALLET_ACTIVITY);
        sendTxIntent.putExtra("walletInteraction", tx);
        UnityActivity.startActivityForResult(sendTxIntent, TX_CODE);
    }

    public String SendMyCommand(String tx){
        Intent sendTxIntent = new Intent(Intent.ACTION_MAIN);
        sendTxIntent.setClassName(WALLET_PACKAGE, WALLET_ACTIVITY);
        sendTxIntent.putExtra("walletInteraction", tx);
        Log.d("Unity-PG", "Inside SendCommand");
        UnityActivity.startActivity(sendTxIntent);
        return "Reached here";
    }

    public void OpenWallet(){
        //Intent wallet = new Intent(Intent.ACTION_VIEW, "");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(WALLET_PACKAGE, WALLET_ACTIVITY);
        intent.putExtra("open_wallet", "fromPhantasmaLinkClient");
        Log.d("Unity-PG", "Inside open wallet");
        UnityActivity.startActivity(intent);
    }

    public String DoSomething(){

        return "Something inside the Plugin";
    }

}
