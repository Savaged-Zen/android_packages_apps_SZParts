package com.evervolv.EVParts;


import com.evervolv.EVParts.R;
import com.evervolv.EVParts.R.xml;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.EditTextPreference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;

public class UiOptions extends PreferenceActivity implements OnPreferenceChangeListener {

	private static final String USE_SCREENOFF_ANIM = "use_screenoff_anim";
	private static final String USE_SCREENON_ANIM = "use_screenon_anim";
	private static final String BATTERY_OPTION = "battery_option";
    private static final String HIDE_CLOCK_PREF = "hide_clock";
    private static final String AM_PM_PREF = "hide_ampm";
    private static final String USE_TRANSPARENT_STATUSBAR = "use_transparent_statusbar";
    
    private CheckBoxPreference mHideClock;
    private CheckBoxPreference mHideAmPm;
	private CheckBoxPreference mUseScreenOnAnim;
	private CheckBoxPreference mUseScreenOffAnim;
	private CheckBoxPreference mUseTransparentStatusBar;
	private ListPreference mBatteryOption;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ui_options);
		PreferenceScreen prefSet = getPreferenceScreen();

		mUseScreenOnAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENON_ANIM);
		mUseScreenOnAnim.setChecked(Settings.System.getInt(getContentResolver(), 
									Settings.System.USE_SCREENON_ANIM, 1) == 1);
		mUseScreenOffAnim = (CheckBoxPreference)prefSet.findPreference(USE_SCREENOFF_ANIM);
		mUseScreenOffAnim.setChecked(Settings.System.getInt(getContentResolver(), 
									 Settings.System.USE_SCREENOFF_ANIM, 1) == 1);		
		
		mBatteryOption = (ListPreference) prefSet.findPreference(BATTERY_OPTION);
		mBatteryOption.setOnPreferenceChangeListener(this);

		mUseTransparentStatusBar = (CheckBoxPreference)prefSet.findPreference(USE_TRANSPARENT_STATUSBAR);
		mUseTransparentStatusBar.setChecked(Settings.System.getInt(getContentResolver(), 
											Settings.System.USE_TRANSPARENT_STATUSBAR, 1) == 1);	
		
		mHideClock = (CheckBoxPreference) prefSet.findPreference(HIDE_CLOCK_PREF);
		mHideClock.setChecked(Settings.System.getInt(getContentResolver(), 
							  Settings.System.SHOW_CLOCK, 1) == 1);
		mHideAmPm = (CheckBoxPreference) prefSet.findPreference(AM_PM_PREF);
		mHideAmPm.setChecked(Settings.System.getInt(getContentResolver(), 
							 Settings.System.SHOW_CLOCK_AMPM, 1) == 1);
		
    }
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        
        if (preference == mHideClock) {
        	value = mHideClock.isChecked();
    	    Settings.System.putInt(getContentResolver(), Settings.System.SHOW_CLOCK, value ? 1 : 0);
    	} else if (preference == mHideAmPm) {
    		value = mHideAmPm.isChecked();
    	    Settings.System.putInt(getContentResolver(), Settings.System.SHOW_CLOCK_AMPM, value ? 1 : 0);
    	} else if (preference == mUseTransparentStatusBar) {
    		value = mUseTransparentStatusBar.isChecked();
    	    Settings.System.putInt(getContentResolver(), Settings.System.USE_TRANSPARENT_STATUSBAR, value ? 1 : 0);
    	} else if (preference == mUseScreenOnAnim) {
    		value = 	mUseScreenOnAnim.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENON_ANIM, value ? 1 : 0);
        } else if (preference == mUseScreenOffAnim) {
        	value = mUseScreenOffAnim.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.USE_SCREENOFF_ANIM, value ? 1 : 0);
        }
        
        return true;
    }
    
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mBatteryOption) {
        	Settings.System.putInt(getContentResolver(), Settings.System.BATTERY_OPTION, Integer.valueOf((String) objValue));
        }
        return true;
    }
    

}
