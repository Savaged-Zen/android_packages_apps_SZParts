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

import com.savagedzen.szparts.R;
import com.savagedzen.szparts.utils.ShellCommand;
import com.savagedzen.szparts.utils.ShellCommand.CommandResult;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;

//
// HAVS Settings
//
public class HAVSActivity extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private static final String UV_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/vdd_levels_havs";

    public static final String UV_PREF = "pref_uv";
    public static final String PROFILES_PREF = "pref_uv_profiles";
    public static final String USE_CUSTOM_PREF = "pref_uv_use_custom";
    public static final String UV_CUSTOM_PREF = "pref_uv_custom";
    public static final String SOB_PREF = "pref_set_on_boot";
    public static final String ADVANCED_SCREEN = "KernelAdvancedActivity";
    public static final String APPLY_PREF = "pref_apply";

    private static final String TAG = "HAVSSettings";

    private String mSetUV = "";
    private String mSetProfile = "";
    private String mUVFormat;
    private String mProfileFormat;

    private PreferenceScreen mAdvancedScreen;

    private Preference mApply;

    private ListPreference mUndervolt;
    private ListPreference mProfiles;

    private CheckBoxPreference mUseCustom;
   // private CheckBoxPreference mSOB;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUVFormat = getString(R.string.uv_summary);
        mProfileFormat = getString(R.string.uv_profiles_summary);

        String[] availableProfiles = getResources().getStringArray(R.array.values_uv_profiles);
        String[] availableUV = getResources().getStringArray(R.array.values_uv_havs);

        setTitle(R.string.havs_title);
        addPreferencesFromResource(R.xml.havs_settings);

        PreferenceScreen PrefScreen = getPreferenceScreen();

        mAdvancedScreen = (PreferenceScreen) PrefScreen.findPreference(ADVANCED_SCREEN);
        mApply = (Preference) PrefScreen.findPreference(APPLY_PREF);
        mUseCustom = (CheckBoxPreference) PrefScreen.findPreference(USE_CUSTOM_PREF);
       // mSOB = (CheckBoxPreference) PrefScreen.findPreference(SOB_PREF);

        // Set up the warning
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.performance_settings_warning_title);
        alertDialog.setMessage(getResources().getString(R.string.performance_settings_warning));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alertDialog.show();

        if (mUseCustom.isChecked()) {
            mAdvancedScreen.setEnabled(true);
        } else if (!mUseCustom.isChecked()) {
            mAdvancedScreen.setEnabled(false);
        }

        // UV Profiles radio-list
        mProfiles = (ListPreference) PrefScreen.findPreference(PROFILES_PREF);
        mProfiles.setEntryValues(availableProfiles);
        mProfiles.setEntries(availableProfiles);
        mProfiles.setValue(mSetProfile);
        mProfiles.setSummary(String.format(mProfileFormat, mSetProfile));
        mProfiles.setOnPreferenceChangeListener(this);

        // Undervolt radio-list
        mUndervolt = (ListPreference) PrefScreen.findPreference(UV_PREF);
        mUndervolt.setEntryValues(availableUV);
        mUndervolt.setEntries(availableUV);
        mUndervolt.setValue(mSetUV);
        mUndervolt.setSummary(String.format(mUVFormat, mSetUV));
        mUndervolt.setOnPreferenceChangeListener(this);
        
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (newValue != null) {
            if (preference == mProfiles) {
                mSetProfile = (String) newValue;
                mProfiles.setSummary(String.format(mProfileFormat, mSetProfile));
                return true;
            }
            if (preference == mUndervolt) {
                mSetUV = (String) newValue;
                mUndervolt.setSummary(String.format(mUVFormat, mSetUV));
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mApply) {
                changeHAVS(mSetProfile, mSetUV);
                return true;
        }

        if (preference == mUseCustom) {
            value = mUseCustom.isChecked();
            if (value) {
                mAdvancedScreen.setEnabled(true);
                return true;
            } else if (!value) {
                mAdvancedScreen.setEnabled(false);
                return true;
            }
        }
        return false;
    }

    public boolean changeHAVS(String profile, String UV) {
        ShellCommand cmd = new ShellCommand();
        String filePath = "/system/etc/vdd_profiles/";
        String fileTOrun = null;
        String tmpProfile = null;
        String tmpUV = null;

        // Determins and sets short profile
        if ("Savaged-Zen".equals(profile)) {
            tmpProfile = "SZ";
            tmpUV = UV;
        }
        if ("SVS".equals(profile)) {
            tmpProfile = "SVS";
            tmpUV = UV;
        }

        // If tmpUV is empty, make null to show correct errors
        if("".equals(tmpUV)) {
            tmpUV = null;
        }

        // Attempt to run script based on user input
        if(tmpProfile != null && tmpUV != null) {
            fileTOrun = filePath + tmpUV + tmpProfile + ".sh";
            CommandResult r = cmd.su.runWaitFor(fileTOrun);
            if (!r.success()) {
                Log.d(TAG, "Error " + r.stderr);
                Log.d(TAG, "File: " + fileTOrun + " returned error: " + r.stderr);
                Toast.makeText(this, "Could not set profile, error.", Toast.LENGTH_LONG).show();
                return true;
            } else {
                Log.d(TAG, "Successfully executed: " + fileTOrun + "Result: " + r.stdout);
                Toast.makeText(this, "HAVS settings sucessfully changed!", Toast.LENGTH_LONG).show();
                return false;
            }
        } else if (tmpProfile == null || tmpUV == null) {
            Log.d(TAG, "No profile or UV level selected!");
            Toast.makeText(this, "No profile and/or Undervolt level selected!", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
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

    public static boolean writeOneLine(String fname, String value) {
        try {
            FileWriter fw = new FileWriter(fname);
            try {
                fw.write(value);
            } finally {
                fw.close();
            }
        } catch (IOException e) {
            String Error = "Error writing to " + fname + ". Exception: ";
            Log.e(TAG, Error, e);
            return false;
        }
        return true;
    }
}
