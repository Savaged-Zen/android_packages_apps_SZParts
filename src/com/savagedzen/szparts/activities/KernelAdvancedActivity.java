/*
 * Copyright (C) 2011 Savaged-Zen
 *     Author: Mike Wielgosz <mwielgosz@gmail.com>
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
import com.savagedzen.szparts.utils.ShellCommand;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;

//
// Kernel Related Settings
//
public class KernelAdvancedActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {


    private static final String TAG = "KernelAdvancedSettings";

    public static final String FREQ_LIST_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
    public static final String FREQ_PREF = "pref_uv_custom_freq";
    public static final String UVMIN_PREF = "pref_uv_custom_min";
    public static final String UVMAX_PREF = "pref_uv_custom_max";
    public static final String CURRENT_PREF = "pref_advanced_current";

    private String mSetFreq = "";
    private String mSetUVmin = "";
    private String mSetUVmax = "";
    private String mFreqFormat;
    private String mUVminFormat;
    private String mUVmaxFormat;

    private Preference mAdvCurrent;
    private EditTextPreference mFreq;
    private EditTextPreference mUVmin;
    private EditTextPreference mUVmax;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFreqFormat = getString(R.string.uv_freq_summary);
        mUVminFormat = getString(R.string.uv_custom_min_summary);
        mUVmaxFormat = getString(R.string.uv_custom_max_summary);

        setTitle(R.string.kernel_advanced_title);
        addPreferencesFromResource(R.xml.kernel_advanced_settings);

        PreferenceScreen PrefScreen = getPreferenceScreen();

        String[] availableFrequencies = readOneLine(FREQ_LIST_PATH).split(" ");
        String availableUVmin = "";
        String availableUVmax = "";

        // Set up the warning
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.uv_warning_title);
        alertDialog.setMessage(getResources().getString(R.string.uv_warning));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alertDialog.show();

        // Current settings
        //mAdvCurrent = (Preference) PrefScreen.findPreferece(CURRENT_PREF);
        

        // Frequencies
        mFreq = (EditTextPreference) PrefScreen.findPreference(FREQ_PREF);
        mFreq.setSummary(String.format(mFreqFormat, mSetFreq));
        mFreq.setOnPreferenceChangeListener(this);

        // UV min
        mUVmin = (EditTextPreference) PrefScreen.findPreference(UVMIN_PREF);
        mUVmin.setSummary(String.format(mUVmaxFormat, mSetUVmax));
        mUVmin.setOnPreferenceChangeListener(this);

        // UV max
        mUVmax = (EditTextPreference) PrefScreen.findPreference(UVMAX_PREF);
        mUVmax.setSummary(String.format(mUVmaxFormat, mSetUVmax));
        mUVmax.setOnPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        mFreq.setSummary(String.format(mFreqFormat, mSetFreq));
        mUVmin.setSummary(String.format(mUVminFormat, mSetUVmin));
        mUVmax.setSummary(String.format(mUVmaxFormat, mSetUVmax));
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean value;

        if (newValue != null) {
            if (preference == mFreq) {
                mSetFreq = (String) newValue;
                mFreq.setSummary(String.format(mFreqFormat, mSetFreq));
            }
            if (preference == mUVmin) {
                mSetUVmin = (String) newValue;
                mUVmin.setSummary(String.format(mUVminFormat, mSetUVmin));
            }
            if (preference == mUVmax) {
                mSetUVmax = (String) newValue;
                mUVmax.setSummary(String.format(mUVmaxFormat, mSetUVmax));
            }
        }

        return true;
    }

    public static String readOneLine(String fname) {
        BufferedReader br;
        String line = null;

        try {
            br = new BufferedReader(new FileReader(fname), 512);
            try {
                line = br.readLine();
            } finally {
                br.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "IO Exception when reading /sys/ file", e);
        }
        return line;
    }

}
