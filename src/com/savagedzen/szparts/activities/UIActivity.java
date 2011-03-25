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

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.SystemProperties;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

public class UIActivity extends PreferenceActivity implements 
OnPreferenceChangeListener {
	private static final String POWER_WIDGET_SCREEN = "pref_power_widget";
	private PreferenceScreen mPowerWidgetScreen;


    /* Preference Screens */

    private PreferenceScreen mExtrasScreen;

    /* Other */

    private static final String LCDD_PREF = "pref_lcdd";
    private static final String LCDD_PROP = "ro.sf.lcd_density";
    private static final String LCDD_PERSIST_PROP = "persist.sys.lcd_density";
    private static final String LCDD_DEFAULT = "240";
    private static final String ELECTRON_BEAM_ANIMATION_ON = 
"electron_beam_animation_on";
    private static final String ELECTRON_BEAM_ANIMATION_OFF = 
"electron_beam_animation_off";

  
    private EditTextPreference mLcddPref;

    private CheckBoxPreference mElectronBeamAnimationOn;
    private CheckBoxPreference mElectronBeamAnimationOff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.ui_settings_title_head);
        addPreferencesFromResource(R.xml.ui_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
	
        /* Power Widget*/
	mPowerWidgetScreen = (PreferenceScreen) prefSet.findPreference(POWER_WIDGET_SCREEN);

        /* Electron Beam control */
        boolean animateScreenLights = getResources().getBoolean(
                com.android.internal.R.bool.config_animateScreenLights);
        mElectronBeamAnimationOn = 
(CheckBoxPreference)prefSet.findPreference(ELECTRON_BEAM_ANIMATION_ON);
        
mElectronBeamAnimationOn.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.ELECTRON_BEAM_ANIMATION_ON, 0) == 1);
        mElectronBeamAnimationOff = 
(CheckBoxPreference)prefSet.findPreference(ELECTRON_BEAM_ANIMATION_OFF);
        
mElectronBeamAnimationOff.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.ELECTRON_BEAM_ANIMATION_OFF, 1) == 1);

        /* Hide Electron Beam controls if electron beam is disabled */
        if (animateScreenLights) {
            prefSet.removePreference(mElectronBeamAnimationOn);
            prefSet.removePreference(mElectronBeamAnimationOff);

        }

   	/* LCD Density Changer */
	mLcddPref = (EditTextPreference) prefSet.findPreference(LCDD_PREF);
        mLcddPref.setText(SystemProperties.get(LCDD_PROP,
                SystemProperties.get(LCDD_PERSIST_PROP, LCDD_DEFAULT)));
        mLcddPref.setOnPreferenceChangeListener(this); 

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mLcddPref) {
            if (newValue != null) {
                SystemProperties.set(LCDD_PERSIST_PROP, (String)newValue);
        return true;
	    }
        }
	return false;
	}
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, 
Preference preference) {
        boolean value;
        // Opens Notification Power Widget Options		
        if (preference == mPowerWidgetScreen) {		
            startActivity(mPowerWidgetScreen.getIntent());		
            return true;		
        }
	if (preference == mElectronBeamAnimationOn) {
            value = mElectronBeamAnimationOn.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.ELECTRON_BEAM_ANIMATION_ON, value ? 1 : 0);
        }

        if (preference == mElectronBeamAnimationOff) {
            value = mElectronBeamAnimationOff.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.ELECTRON_BEAM_ANIMATION_OFF, value ? 1 : 
0);
        }

        return true;
    }
}
