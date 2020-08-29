/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.sunshine.data.WeatherContract;


public class SunshineSyncUtils {

    //  TODO (1) Declare a private static boolean field called sInitialized Okay
    private static boolean sInitialized;

    //  TODO (2) Create a synchronized public static void method called initialize Okay
    public static void initialize(final Context context) {
        //  TODO (3) Only execute this method body if sInitialized is false Okay
        if (!sInitialized) {
            //  TODO (4) If the method body is executed, set sInitialized to true Okay
            sInitialized = true;
            //  TODO (5) Check to see if our weather ContentProvider is empty Okay
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    // Get the query and projection and selection.
                    Uri weatherQuery = WeatherContract.WeatherEntry.CONTENT_URI;

                    // Projection of the columns.
                    String[] weatherProjection = {WeatherContract.WeatherEntry.COLUMN_WEATHER_ID};

                    // Selection criteria.
                    String weatherSelection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                    // Return the courser with objects.
                    Cursor queryCursor = context.getContentResolver().query(WeatherContract.WeatherEntry.CONTENT_URI,
                            weatherProjection,
                            weatherSelection,
                            null,
                            null);

                    if (queryCursor == null && queryCursor.getCount() == 0) {
                        //  TODO (6) If it is empty or we have a null Cursor, sync the weather now! Okay
                        startImmediateSync(context);
                    }
                    queryCursor.close();
                    return null;
                }
            }.execute();

        } else {
            return;
        }
    }

    /**
     * Helper method to perform a sync immediately using an IntentService for asynchronous
     * execution.
     *
     * @param context The Context used to start the IntentService for the sync.
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}