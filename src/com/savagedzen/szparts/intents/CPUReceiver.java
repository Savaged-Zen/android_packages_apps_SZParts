/*
 * Copyright (C) 2011 The CyanogenMod Project
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

package com.savagedzen.szparts.intents;

import com.savagedzen.szparts.utils.ShellCommand;
import com.savagedzen.szparts.utils.ShellCommand.CommandResult;
import com.savagedzen.szparts.activities.HAVSActivity;
import com.savagedzen.szparts.activities.CPUActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import java.util.Arrays;
import java.util.List;

public class CPUReceiver extends BroadcastReceiver {

    private static final String TAG = "CPUSettings";

    private static final String CPU_SETTINGS_PROP = "sys.cpufreq.restored";
    private static final String HAVS_SETTINGS_PROP = "sys.havs.restored";

    @Override
    public void onReceive(Context ctx, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
            if (SystemProperties.getBoolean(CPU_SETTINGS_PROP, false) == false) {
                SystemProperties.set(CPU_SETTINGS_PROP, "true");
                configureCPU(ctx);
            } else {
                SystemProperties.set(CPU_SETTINGS_PROP, "false");
            }
            if (SystemProperties.getBoolean(HAVS_SETTINGS_PROP, false) == false) {
                SystemProperties.set(HAVS_SETTINGS_PROP, "true");
                configureHAVS(ctx);
            } else {
                SystemProperties.set(HAVS_SETTINGS_PROP, "false");
            }
        }
    }

    private void configureCPU(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        if (prefs.getBoolean(CPUActivity.SOB_PREF, false) == false) {
            Log.i(TAG, "CPU restore disabled by user preference.");
            return;
        }

        String governor = prefs.getString(CPUActivity.GOV_PREF, null);
        String minFrequency = prefs.getString(CPUActivity.MIN_FREQ_PREF, null);
        String maxFrequency = prefs.getString(CPUActivity.MAX_FREQ_PREF, null);
        boolean noSettings = (governor == null) && (minFrequency == null) && (maxFrequency == null);

        if (noSettings) {
            Log.d(TAG, "No settings saved. Nothing to restore.");
        } else {
            List<String> governors = Arrays.asList(CPUActivity.readOneLine(
                    CPUActivity.GOVERNORS_LIST_FILE).split(" "));
            List<String> frequencies = Arrays.asList(CPUActivity.readOneLine(
                    CPUActivity.FREQ_LIST_FILE).split(" "));
            if (governor != null && governors.contains(governor)) {
                CPUActivity.writeOneLine(CPUActivity.GOVERNOR, governor);
            }
            if (maxFrequency != null && frequencies.contains(maxFrequency)) {
                CPUActivity.writeOneLine(CPUActivity.FREQ_MAX_FILE, maxFrequency);
            }
            if (minFrequency != null && frequencies.contains(minFrequency)) {
                CPUActivity.writeOneLine(CPUActivity.FREQ_MIN_FILE, minFrequency);
            }
            Log.d(TAG, "CPU settings restored.");
        }
    }

    private void configureHAVS(Context ctx) {
        ShellCommand cmd = new ShellCommand();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String fileTOrun = "/data/customHAVS.sh";
        boolean havsExists = new File(fileTOrun).exists();

        if (prefs.getBoolean(HAVSActivity.HAVS_SOB_PREF, false) == false) {
            Log.i(TAG, "HAVS restore disabled by user preference.");
            Toast.makeText(ctx, "HAVS restore disabled by user preference.", Toast.LENGTH_LONG).show();
            return;
        }
        if (havsExists && prefs.getBoolean(HAVSActivity.HAVS_SOB_PREF, false) == true) {
            CommandResult r = cmd.su.runWaitFor("chmod 0744 " + fileTOrun);
            if (!r.success()) {
                Log.d(TAG, "File: " + fileTOrun + " returned error: " + r.stderr);
                Toast.makeText(ctx, "Could not set permissions.", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Permissions set " + fileTOrun + "Result: " + r.stdout);
                Toast.makeText(ctx, "HAVS settings sucessfully set permissions", Toast.LENGTH_LONG).show();
            }
            r = cmd.su.runWaitFor(fileTOrun);
            if (!r.success()) {
                Log.d(TAG, "File: " + fileTOrun + " returned error: " + r.stderr);
                Toast.makeText(ctx, "Could not restore backup, error.", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Successfully executed: " + fileTOrun + "Result: " + r.stdout);
                Toast.makeText(ctx, "HAVS settings sucessfully restored!", Toast.LENGTH_LONG).show();
            }
            return;
        } else {
            Log.d(TAG, "No HAVS backup to restore");
            Toast.makeText(ctx, "You do not have a backup to restore!", Toast.LENGTH_LONG).show();
            return;
        }
    }
}
