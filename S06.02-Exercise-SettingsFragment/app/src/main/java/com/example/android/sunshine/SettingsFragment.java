package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings_preference);

        PreferenceManager preferenceManager = getPreferenceManager();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceManager.getSharedPreferences();
        int numOfPreference = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < numOfPreference; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), ""));
            }
        }
    }
    // Do steps 5 - 11 within SettingsFragment
    // TODO (10) Implement OnSharedPreferenceChangeListener from SettingsFragment Okay

    // TODO (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference Okay
    void setPreferenceSummary(Preference preference, Object data) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int indexOfValue = listPreference.findIndexOfValue(data.toString());
            String currentValue = (String) listPreference.getEntries()[indexOfValue];
            if (currentValue != null) {
                listPreference.setSummary(currentValue);
            }
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(data.toString());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (!(preference instanceof CheckBoxPreference)) {
            setPreferenceSummary(preference, sharedPreferences.getString(preference.getKey(), ""));

        }
    }

    // TODO (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource Okay

    // Do step 9 within onCreatePreference
    // TODO (9) Set the preference summary on each preference that isn't a CheckBoxPreference Okay

    // TODO (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop Okay

    // TODO (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart Okay

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    // TODO (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed Okay

}
