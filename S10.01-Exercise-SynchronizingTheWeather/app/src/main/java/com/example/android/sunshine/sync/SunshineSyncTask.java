package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;

//  TODO (1) Create a class called SunshineSyncTask Okay
public class SunshineSyncTask {
    //  TODO (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather Okay
    //      TODO (3) Within syncWeather, fetch new weather data Okay
//      TODO (4) If we have valid results, delete the old data and insert the new Okay


    /**
     * Performs the network request for updated weather, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider. Will notify the user that new
     * weather has been loaded if the user hasn't been notified of the weather within the last day
     * AND they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncWeather(Context context) {
        try {

            // Get the current weather url.
            URL weatherDataUrl = NetworkUtils.getUrl(context);

            // Get the response back from the url in the string.
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherDataUrl);

            // Get the content values from the json response.
            ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            // Check that we have any problem or not
            if (weatherValues != null && weatherValues.length != 0) {

                // Get the content resolver object to access the database.
                ContentResolver weatherContentResolver = context.getContentResolver();

                // Delete the data stored in the content resolver.
                weatherContentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null);

                // Insert our new data with the bulk insert in order to insert multiple data.
                weatherContentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);
            }
        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }


    }
}