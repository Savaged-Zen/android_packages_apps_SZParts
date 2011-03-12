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
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

public class DisplayActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    /* Preference Screens */
    private static final String BACKLIGHT_SETTINGS = "backlight_settings";

    private static final String GENERAL_CATEGORY = "general_category";

    private static final String ELECTRON_BEAM_ANIMATION_ON = "electron_beam_animation_on";

    private static final String ELECTRON_BEAM_ANIMATION_OFF = "electron_beam_animation_off";

    private PreferenceScreen mBacklightScreen;

    /* Other */
    private static final String ROTATE_180_PREF = "pref_rotate_180";

    private CheckBoxPreference mElectronBeamAnimationOn;

    private CheckBoxPreference mElectronBeamAnimationOff;

    private CheckBoxPreference mRotate180Pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.display_settings_title_subhead);
        addPreferencesFromResource(R.xml.display_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        /* Preference Screens */
        mBacklightScreen = (PreferenceScreen) prefSet.findPreference(BACKLIGHT_SETTINGS);
        // No reason to show backlight if no light sensor on device
        if (((SensorManager) getSystemService(SENSOR_SERVICE)).getDefaultSensor(Sensor.TYPE_LIGHT) == null) {
            ((PreferenceCategory) prefSet.findPreference(GENERAL_CATEGORY))
                    .removePreference(mBacklightScreen);
        }

        /* Electron Beam control */
        boolean animateScreenLights = getResources().getBoolean(
                com.android.internal.R.bool.config_animateScreenLights);
        mElectronBeamAnimationOn = (CheckBoxPreference)prefSet.findPreference(ELECTRON_BEAM_ANIMATION_ON);
        mElectronBeamAnimationOn.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.ELECTRON_BEAM_ANIMATION_ON,
                getResources().getBoolean(com.android.internal.R.bool.config_enableScreenOnAnimation) ? 1 : 0) == 1);
        mElectronBeamAnimationOff = (CheckBoxPreference)prefSet.findPreference(ELECTRON_BEAM_ANIMATION_OFF);
        mElectronBeamAnimationOff.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.ELECTRON_BEAM_ANIMATION_OFF,
                getResources().getBoolean(com.android.internal.R.bool.config_enableScreenOffAnimation) ? 1 : 0) == 1);

        /* Hide Electron Beam controls if electron beam is disabled */
        if (animateScreenLights) {
            prefSet.removePreference(mElectronBeamAnimationOn);
            prefSet.removePreference(mElectronBeamAnimationOff);
        }

        /* Rotate 180 */
        mRotate180Pref = (CheckBoxPreference) prefSet.findPreference(ROTATE_180_PREF);
        mRotate180Pref.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATE_180, 0) == 1);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        /* Preference Screens */
        if (preference == mBacklightScreen) {
            startActivity(mBacklightScreen.getIntent());
        }
        if (preference == mElectronBeamAnimationOn) {
            value = mElectronBeamAnimationOn.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.ELECTRON_BEAM_ANIMATION_ON, value ? 1 : 0);
        }

        if (preference == mElectronBeamAnimationOff) {
            value = mElectronBeamAnimationOff.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.ELECTRON_BEAM_ANIMATION_OFF, value ? 1 : 0);
        }

        if (preference == mRotate180Pref) {
            value = mRotate180Pref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATE_180, value ? 1 : 0);
        }

        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

}
