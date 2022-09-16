package com.phantasma.phantasmalinkclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhantasmaLinkOnResultCallback extends Activity {
    public static final String TAG = PhantasmaLinkClientClass.TAG + "_OnResult";
    public static PhantasmaLinkOnResultCallback Instance;
    public static PhantasmaLinkClientClass.SendTransactionCallback transactionCallback;
    String transaction;

    void MyFinish(int resultID, String resultTx){
        if ( transactionCallback != null){
            transactionCallback.onShareComplete(resultID);
        }
        transactionCallback = null;
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreateBundle");
        Intent intent = getIntent();
        if (intent != null){
            transaction = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.i(TAG, "Transaction:" + transaction);
        }

        if ( intent == null || transaction == null){
            MyFinish(1, "");
            return;
        }

        //mSendTransaction = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        //    @Override
        //    public void onActivityResult(Uri result) {
        //        Log.i(TAG, "result:"+result);
        //    }
        //});


        try
        {
            Intent sendTxIntent = new Intent(Intent.ACTION_MAIN);
            sendTxIntent.setClassName(PhantasmaLinkClientClass.WALLET_PACKAGE, PhantasmaLinkClientClass.WALLET_ACTIVITY);
            sendTxIntent.putExtra("walletInteraction", transaction);
            //mSendTransaction.launch(transaction, AppCompatActivity.SEN);
            startActivityForResult(sendTxIntent,10);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(TAG,"error: " + e.getLocalizedMessage());
        }
    }

    public void ButtonClick(){
        Intent result = new Intent().putExtra(
                PhantasmaLinkResultContract.RESULT_KEY,
                "something");
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG,"onActivityResult: " + requestCode + ", " + resultCode + ", " + data);
        if ( data != null)
        {
            String result = data.getStringExtra("result");
            MyFinish(resultCode, result);
            return;
        }

        MyFinish(resultCode, "");

    }
}
