package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;

// TODO (9) Create a class called SunshineSyncUtils Okay
public class SunshineSyncUtils {
    //  TODO (10) Create a public static void method called startImmediateSync Okay
    public static void startImmediateSync(final Context context) {
        //  TODO (11) Within that method, start the SunshineSyncIntentService Okay
        Intent startImmediateSyncService = new Intent(context, SunshineSyncIntentService.class);
        context.startService(startImmediateSyncService);
    }

}