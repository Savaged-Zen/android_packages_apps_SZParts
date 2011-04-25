/*
 * Copyright (C) 2011 Savaged-Zen
 *     Author: Joshua Seidel <jsseidel1@gmail.com>
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

import com.savagedzen.szparts.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;


public class SystemActivity extends PreferenceActivity {

    private static final String QH_SCREEN = "SoundQuietHoursActivity";

    private PreferenceScreen mQHScreen;

    private static final String TAG = "SZParts";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.system_settings_title_head);
        addPreferencesFromResource(R.xml.system_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mQHScreen = (PreferenceScreen) prefSet.findPreference(QH_SCREEN);
     }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mQHScreen) {
            startActivity(mQHScreen.getIntent());
      	}
        return true;
    }
}
