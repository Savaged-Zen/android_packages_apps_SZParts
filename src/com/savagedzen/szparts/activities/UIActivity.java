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
//    private static final String LCDD_PREF = "pref_lcdd";
//    private static final String LCDD_PROP = "ro.sf.lcd_density";
//    private static final String LCDD_PERSIST_PROP = "persist.sys.lcd_density";
//    private static final String LCDD_DEFAULT = "240";
  
//    private EditTextPreference mLcddPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_settings_title_head);
        addPreferencesFromResource(R.xml.ui_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
	
        /* Power Widget*/
	mPowerWidgetScreen = (PreferenceScreen) prefSet.findPreference(POWER_WIDGET_SCREEN);

   	/* LCD Density Changer */
//        mLcddPref = (EditTextPreference) prefSet.findPreference(LCDD_PREF);
//        mLcddPref.setText(SystemProperties.get(LCDD_PROP,
//                SystemProperties.get(LCDD_PERSIST_PROP, LCDD_DEFAULT)));
//        mLcddPref.setOnPreferenceChangeListener(this); 

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
//        if (preference == mLcddPref) {
//            if (newValue != null) {
//                SystemProperties.set(LCDD_PERSIST_PROP, (String)newValue);
//                return true;
//	    }
//        }
	return false;
    }
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        // Opens Notification Power Widget Options		
        if (preference == mPowerWidgetScreen) {		
            startActivity(mPowerWidgetScreen.getIntent());		
            return true;		
        }
        return false;
    }
}
