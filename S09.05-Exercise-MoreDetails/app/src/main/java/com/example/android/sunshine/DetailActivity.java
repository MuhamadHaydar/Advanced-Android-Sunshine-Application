/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//      TODO (21) Implement LoaderManager.LoaderCallbacks<Cursor> Okay

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    //  TODO (18) Create a String array containing the names of the desired data columns from our ContentProvider Okay
    private final String[] PROJECTION_COLUMN_NAMES = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_DEGREES
    };
//  TODO (19) Create constant int values representing each column name's position above Okay

    private final int INDEX_DATE_COLUMN = 0;
    private final int INDEX_ID_COLUMN = 1;
    private final int INDEX_MAZ_COLUMN = 2;
    private final int INDEX_MIN_COLUMN = 3;
    private final int INDEX_HUMIDITY_COLUMN = 4;
    private final int INDEX_WIND_COLUMN = 5;
    private final int INDEX_PRESSURE_COLUMN = 6;
    private final int INDEX_DEGREES_COLUMN = 7;

    //  TODO (20) Create a constant int to identify our loader used in DetailActivity Okay
    private final int FORECAST_LOADER_ID = 3;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;
    //  TODO (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure Okay
    TextView mDate, mDescription, mHighTemp, mLowTemp, mHumidity, mWind, mPressure;

    //  TODO (10) Remove the mWeatherDisplay TextView declaration Okay
    //  TODO (15) Declare a private Uri field called mUri
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      TODO (12) Remove mWeatherDisplay TextView Okay

//      TODO (13) Find each of the TextViews by ID Okay
        mDate = findViewById(R.id.tv_date);
        mDescription = findViewById(R.id.tv_description);
        mHighTemp = findViewById(R.id.tv_high_temp);
        mLowTemp = findViewById(R.id.tv_low_temp);
        mHumidity = findViewById(R.id.tv_humidity);
        mWind = findViewById(R.id.tv_wind);
        mPressure = findViewById(R.id.tv_pressure);


//      TODO (14) Remove the code that checks for extra text Okay
        Intent intentThatStartedThisActivity = getIntent();

//      TODO (16) Use getData to get a reference to the URI passed with this Activity's Intent
        if (intentThatStartedThisActivity != null) {
            mUri = intentThatStartedThisActivity.getData();
            if (mUri == null) {
                throw new NullPointerException("The Uri for DetailActivity can not be null!");
            }

        }
//      TODO (17) Throw a NullPointerException if that URI is null Okay
//      TODO (35) Initialize the loader for DetailActivity Okay

        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        switch (loaderId) {
            case FORECAST_LOADER_ID:
                return new CursorLoader(this, mUri, PROJECTION_COLUMN_NAMES, null, null, null);
            default:
                throw new RuntimeException("Couldn't make Cursor Loader " + loaderId);
        }
    }


    //  TODO (24) Override onLoadFinished Okay
//      TODO (25) Check before doing anything that the Cursor has valid data Okay
//      TODO (26) Display a readable data string Okay
//      TODO (27) Display the weather description (using SunshineWeatherUtils) Okay
//      TODO (28) Display the high temperature Okay
//      TODO (29) Display the low temperature Okay
//      TODO (30) Display the humidity Okay
//      TODO (31) Display the wind speed and direction Okay
//      TODO (32) Display the pressure Okay
//      TODO (33) Store a forecast summary in mForecastSummary Okay
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            // Move to the first Row not the row containing the title.
            // get the date from cursor .
            long dateInMills = cursor.getLong(INDEX_DATE_COLUMN);
            String dateString = SunshineDateUtils.getFriendlyDateString(this, dateInMills, true);
            mDate.setText(dateString);

            // get the id from the cursor.
            int weatherId = cursor.getInt(INDEX_ID_COLUMN);
            String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
            mDescription.setText(description);

            /* Read high temperature from the cursor (in degrees celsius) */
            double highInCelsius = cursor.getDouble(INDEX_MAZ_COLUMN);
            String highTemp = SunshineWeatherUtils.formatTemperature(this, highInCelsius);
            mHighTemp.setText(highTemp);
            /* Read low temperature from the cursor (in degrees celsius) */
            double lowInCelsius = cursor.getDouble(INDEX_MIN_COLUMN);
            String lowTemp = SunshineWeatherUtils.formatTemperature(this, lowInCelsius);
            mLowTemp.setText(lowTemp);

            // get the humidity of weather.
            double weatherHumidity = cursor.getDouble(INDEX_HUMIDITY_COLUMN);
            String humidity = getString(R.string.format_humidity, weatherHumidity);
            mHumidity.setText(humidity);

            // Get the weather wind.
            float weatherWind = cursor.getFloat(INDEX_WIND_COLUMN);
            // Get the weather degrees to format wind into readable.
            float weatherDegree = cursor.getFloat(INDEX_DEGREES_COLUMN);
            String windString = SunshineWeatherUtils.getFormattedWind(this, weatherWind, weatherDegree);
            mWind.setText(windString);

            // Get the weather pressure.
            double weatherPressure = cursor.getDouble(INDEX_PRESSURE_COLUMN);
            String pressure = getString(R.string.format_pressure, weatherPressure);
            mPressure.setText(pressure);

            mForecastSummary = dateString + "-" + description + "-" + highTemp + "-" + lowTemp + "-" + weatherHumidity + "-" + windString + "-" + weatherPressure;
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

//  TODO (22) Override onCreateLoader Okay
//          TODO (23) If the loader requested is our detail loader, return the appropriate CursorLoader Okay


//  TODO (34) Override onLoaderReset, but don't do anything in it yet Okay

}