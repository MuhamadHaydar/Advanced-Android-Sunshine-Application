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
package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    private static final String TAG = ForecastAdapter.class.getSimpleName();

    //  TODO (14) Remove the mWeatherData declaration and the setWeatherData method Okay

    //  TODO (1) Declare a private final Context field called mContext Okay
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    final private ForecastAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    //  TODO (2) Declare a private Cursor field called mCursor Okay
    private Cursor mCursor;
//  TODO (3) Add a Context field to the constructor and store that context in mContext Okay

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ForecastAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
//      TODO (5) Delete the current body of onBindViewHolder Okay
//      TODO (6) Move the cursor to the appropriate position Okay
//      TODO (7) Generate a weather summary with the date, description, high and low Okay
        // Move the weather to current position.
        mCursor.moveToPosition(position);

        // Get the index of whole cursor.
//        int dateIndex = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
//        int highTempIndex = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
//        int minTempIndex = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);

        /* Read date from the cursor */
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_COLUMN_DATE);
        /* Get human readable string using our utility method */
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        /* Use the weatherId to obtain the proper description */
        int weatherId = mCursor.getInt(MainActivity.INDEX_COLUMN_ID);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        /* Read high temperature from the cursor (in degrees celsius) */
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_COLUMN_MAX_TEMP);
        /* Read low temperature from the cursor (in degrees celsius) */
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_COLUMN_MIN_TEMP);

        String highAndLowTemperature =
                SunshineWeatherUtils.formatHighLows(mContext, highInCelsius, lowInCelsius);

        // Combine whole of the data returned by the cursor and display it.
        String weatherForThisDay = dateString + " - " + description + " - " + highAndLowTemperature;
//      TODO (8) Display the summary that you created above Okay
        forecastAdapterViewHolder.weatherSummary.setText(weatherForThisDay);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
//      TODO (9) Delete the current body of getItemCount Okay
//      TODO (10) If mCursor is null, return 0. Otherwise, return the count of mCursor Okay
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }


//  TODO (11) Create a new method that allows you to swap Cursors. Okay
//      TODO (12) After the new Cursor is set, call notifyDataSetChanged Okay

    public void swapCursors(Cursor currentCursor) {
        mCursor = currentCursor;
        notifyDataSetChanged();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView weatherSummary;

        ForecastAdapterViewHolder(View view) {
            super(view);
            weatherSummary = view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            //  TODO (13) Instead of passing the String from the data array, use the weatherSummary text! Okay
            String weatherForThisDay = weatherSummary.getText().toString();
            mClickHandler.onClick(weatherForThisDay);
        }
    }
}