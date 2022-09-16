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

    public static final PhantasmaLinkClientClass Instance = new PhantasmaLinkClientClass();
    protected static final String TAG = "Unity-Link";

    public static String sessionIdIntent = "sessionId";
    private static String sessionId;
    private static Activity UnityActivity;
    private static final int TX_CODE = 10;
    protected static final String WALLET_PACKAGE = "com.Phantasma.PoltergeistWallet"; // "com.phantasma.poltergeistmodule.MainActivity"
    protected static final String WALLET_ACTIVITY = "com.phantasma.poltergeistmodule.MainActivity";

    public static PhantasmaLinkClientClass getInstance() { return Instance; }

    public interface SendTransactionCallback {
        public void onShareComplete(int result);
    }

    public static void ReceiveActivity(Activity activity){
        UnityActivity = activity;
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

    public void sendTransaction( final String transaction, final SendTransactionCallback callback)
    {
        UnityActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"Sending transaction");
                try {
                    Intent sendTxIntent = new Intent();
                    sendTxIntent.setAction(Intent.ACTION_MAIN);
                    sendTxIntent.putExtra(Intent.EXTRA_TEXT, transaction);
                    sendTxIntent.setClass(UnityActivity, PhantasmaLinkOnResultCallback.class);
                    PhantasmaLinkOnResultCallback.transactionCallback = callback;
                    UnityActivity.startActivity(sendTxIntent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.i(TAG,"error sharing intent: " + e);
                }
            }
        });
    }


}
