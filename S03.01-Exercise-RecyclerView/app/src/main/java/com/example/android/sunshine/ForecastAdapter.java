package com.example.android.sunshine;

// Within ForecastAdapter.java /////////////////////////////////////////////////////////////////
// TODO (15) Add a class file called ForecastAdapter Okay
// TODO (22) Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> Okay

// TODO (23) Create a private string array called mWeatherData Okay

// TODO (47) Create the default constructor (we will pass in parameters in a later lesson) Okay

// TODO (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder Okay
// TODO (17) Extend RecyclerView.ViewHolder Okay

// TODO (24) Override onCreateViewHolder Okay
// TODO (25) Within onCreateViewHolder, inflate the list item xml into a view Okay
// TODO (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter Okay

// TODO (27) Override onBindViewHolder Okay
// TODO (28) Set the text of the TextView to the weather for this list item's position Okay

// TODO (29) Override getItemCount Okay
// TODO (30) Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null Okay

// TODO (31) Create a setWeatherData method that saves the weatherData to mWeatherData Okay
// TODO (32) After you save mWeatherData, call notifyDataSetChanged Okay
// Within ForecastAdapter.java /////////////////////////////////////////////////////////////////


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;

    public ForecastAdapter() {
    }

    public void setmWeatherData(String[] mWeatherData) {
        this.mWeatherData = mWeatherData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        int layoutResourceId = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToRootFile = false;
        View listItemView = inflater.inflate(layoutResourceId, viewGroup, attachToRootFile);
        ForecastAdapterViewHolder forecastAdapterViewHolder = new ForecastAdapterViewHolder(listItemView);

        return forecastAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        forecastAdapterViewHolder.mWeatherTextView.setText(mWeatherData[position]);
    }

    @Override
    public int getItemCount() {
        return (mWeatherData == null) ? 0 : mWeatherData.length;
    }

    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {

        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mWeatherTextView = itemView.findViewById(R.id.tv_weather_data);
        }
    }

// Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////
// TODO (18) Create a public final TextView variable called mWeatherTextView Okay

// TODO (19) Create a constructor for this class that accepts a View as a parameter Okay
// TODO (20) Call super(view) within the constructor for ForecastAdapterViewHolder Okay
// TODO (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView Okay
// Within ForecastAdapterViewHolder ///////////////////////////////////////////////////////////


}
