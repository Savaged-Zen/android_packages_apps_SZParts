package com.evervolv.EVParts;


import com.evervolv.EVParts.R;
import com.evervolv.EVParts.R.xml;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.EditTextPreference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;


public class LockscreenParts extends PreferenceActivity
implements OnPreferenceChangeListener {

	private static final String CARRIER_CAP = "carrier_caption";
	private static final String LOCKSCREEN_ROTARY_LOCK = "use_rotary_lockscreen";
	
	private EditTextPreference mCarrierCaption;
	private CheckBoxPreference mUseRotaryLockPref;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.lockscreen_prefs);
		PreferenceScreen prefSet = getPreferenceScreen();
		
		mUseRotaryLockPref = (CheckBoxPreference)prefSet.findPreference(LOCKSCREEN_ROTARY_LOCK);
		mUseRotaryLockPref.setChecked(Settings.System.getInt(getContentResolver(), 
									  Settings.System.USE_ROTARY_LOCKSCREEN, 1) == 1);		
		
		mCarrierCaption = (EditTextPreference)prefSet.findPreference(CARRIER_CAP);
		mCarrierCaption.setOnPreferenceChangeListener(this);
    }	
	
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        
        if (preference == mUseRotaryLockPref) {
            value = mUseRotaryLockPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.USE_ROTARY_LOCKSCREEN, value ? 1 : 0);
            //Temporary hack to fix Phone FC's when swapping styles.
            ActivityManager am = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            am.forceStopPackage("com.android.phone");
        }
        
        return true;
    }
	
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        
        if (preference == mCarrierCaption) {
			Settings.System.putString(getContentResolver(),CARRIER_CAP, objValue.toString());
			//Didn't i say i was learning?
            ActivityManager am = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            am.forceStopPackage("com.android.phone");
		
        }
        return true;
    }
    
}
