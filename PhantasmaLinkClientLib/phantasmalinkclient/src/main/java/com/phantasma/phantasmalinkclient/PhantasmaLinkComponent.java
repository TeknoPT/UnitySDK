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

public class PhantasmaLinkComponent extends Activity {
    //ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
    //            new ActivityResultCallback<ActivityResult>() {
    //    @Override
    //    public void onActivityResult(ActivityResult result) {
    //        if (result.getResultCode() == Activity.RESULT_OK) {
    //            Intent intent = result.getData();
    //            // Handle the Intent
    //            Log.d("Unity-PG", "Inside the result");
    //            intent.getStringExtra("Results");
    //        }
    //    }
    //});

    public static PhantasmaLinkComponent Instance;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        moveTaskToBack(true);
        Instance = this;
    }

    @Override
    protected void onStop(){
        super.onStop();
    }



    public void GetResult(Intent intent, String tx, int REQUEST_CODE){
        startActivityForResult(intent, REQUEST_CODE);
        //UnityActivity.startActivityForResult(sendTxIntent, TX_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("Unity-PG", "OnMyActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
