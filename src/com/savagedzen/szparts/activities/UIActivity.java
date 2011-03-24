/*
 * Copyright (C) 2011 Savaged-Zen
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

package com.savagedzen.szparts.activities;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.savagedzen.szparts.R;

public class UIActivity extends PreferenceActivity {

    private static final String POWER_WIDGET_SCREEN = "pref_power_widget";

    private PreferenceScreen mPowerWidgetScreen;

    private static final String STATUS_BAR_BOTTOM = "pref_status_bar_bottom";

    private CheckBoxPreference mStatusBarBottom;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_settings_title_head);
        addPreferencesFromResource(R.xml.ui_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mPowerWidgetScreen = (PreferenceScreen) prefSet.findPreference(POWER_WIDGET_SCREEN);

        // Set the status bar to the bottom of the screen
        mStatusBarBottom = (CheckBoxPreference) prefSet.findPreference(STATUS_BAR_BOTTOM);
        mStatusBarBottom.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_BOTTOM, 0) == 1);

    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        // Opens Notification Power Widget Options
        if (preference == mPowerWidgetScreen) {
            startActivity(mPowerWidgetScreen.getIntent());
            return true;
        }
        // Status Bar Top/Bottom
        if (preference == mStatusBarBottom) {
            value = mStatusBarBottom.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_BOTTOM,
                    value ? 1 : 0);
            return true;
        }
        return false;
    }

}
