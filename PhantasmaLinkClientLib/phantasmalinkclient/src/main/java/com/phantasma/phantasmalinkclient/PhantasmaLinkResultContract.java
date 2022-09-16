package com.phantasma.phantasmalinkclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PhantasmaLinkResultContract extends ActivityResultContract<String, String> {

    public static final String RESULT_KEY = "transaction";

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        return new Intent(context, PhantasmaLinkOnResultCallback.class);
    }

    @Override
    public String parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode != Activity.RESULT_OK) return null;
        return intent.getStringExtra(RESULT_KEY);
    }
}
