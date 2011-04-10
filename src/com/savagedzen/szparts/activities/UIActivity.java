/*
 * Copyright (C) 2011 The CyanogenMod Project
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
import com.savagedzen.szparts.R.bool;
import com.savagedzen.szparts.R.xml;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

public class UIActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private PreferenceScreen mPowerWidgetScreen;

    private static final String POWER_WIDGET_SCREEN = "pref_power_widget";

    private static final String USE_SCREENOFF_ANIM = "pref_use_screenoff_anim";
    private static final String USE_SCREENON_ANIM = "pref_use_screenon_anim";
      
    private CheckBoxPreference mUseScreenOnAnim;
    private CheckBoxPreference mUseScreenOffAnim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_settings_title_head);
        addPreferencesFromResource(R.xml.ui_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
	
        /* Power Widget*/
	mPowerWidgetScreen = (PreferenceScreen) prefSet.findPreference(POWER_WIDGET_SCREEN);

        /* Electron Beam control */
		mUseScreenOnAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENON_ANIM);
		mUseScreenOnAnim.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.USE_SCREENON_ANIM, 1) == 1);

		mUseScreenOffAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENOFF_ANIM);
		mUseScreenOffAnim.setChecked(Settings.System.getInt(getContentResolver(), 
						Settings.System.USE_SCREENOFF_ANIM, 1) == 1);
       }

    	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
     boolean value;

		// Opens Notification Power Widget Options		
        if (preference == mPowerWidgetScreen) {		
            startActivity(mPowerWidgetScreen.getIntent());		
      	} else if (preference == mUseScreenOnAnim) {
    		value = 	mUseScreenOnAnim.isChecked();
            	Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENON_ANIM, value ? 1 : 0);
        } else if (preference == mUseScreenOffAnim) {
        	value = mUseScreenOffAnim.isChecked();
            	Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENOFF_ANIM, value ? 1 : 0);
        }
        return true;
      }
}
