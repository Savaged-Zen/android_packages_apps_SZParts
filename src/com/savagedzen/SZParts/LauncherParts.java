package com.savagedzen.SZParts;


import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.provider.Settings;


public class LauncherParts extends PreferenceActivity
implements SharedPreferences.OnSharedPreferenceChangeListener , OnClickListener, OnPreferenceChangeListener{

	//private static final int REQUEST_PICK_APPLICATION = 6;
	//private static final String FARRIGHT_AB = "farrightaction_button";
	
	private ListPreference mFLAB;
	private ListPreference mFRAB;
	private ListPreference mNumLauncherButtons;
	private CheckBoxPreference mUseStockLaunchPref;
	
	private String[] mAppNames;
	private static final String LAUNCHER_STOCK = "use_stock_launcher";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.launcher_prefs);
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		PreferenceScreen prefSet = getPreferenceScreen();
		//mFLAB = (ListPreference) findPreference("launcher_farlefttab");
		//mFLAB.setEnabled(false);
		//mFLAB.setSummary(Settings.System.getString(getContentResolver(), Settings.System.FARLEFT_AB));
		//mFRAB = (ListPreference) findPreference("launcher_farrightab");
		//mFRAB.setEnabled(false);
		//mNumLauncherButtons = (ListPreference) findPreference("launcher_numlauncherbuttons");
		//mNumLauncherButtons.setEnabled(false);
	
		
		mUseStockLaunchPref = (CheckBoxPreference)prefSet.findPreference(LAUNCHER_STOCK);
		mUseStockLaunchPref.setChecked(Settings.System.getInt(getContentResolver(), Settings.System.USE_STOCK_LAUNCHER   , 0) != 0);	
		
		
		
		//PackageManager pm = getPackageManager();
		//List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
		//int max = packs.size();
		//mAppNames = new String[max];
		//for (int i = 0; i < max; i++) {  
        //   try {
        //	   Log.d("LauncherParts", "packageName: " + packs.get(i).packageName );
        //	   mAppNames[i] = packs.get(i).packageName;
        //   } catch (NullPointerException e) {
        //       Log.d("LauncherParts", "NullPointerException @: " + i);
        //   }
    		   
        //}  
     
		
		//mFLAB.setDialogTitle("Far left Action Button.");
		//mFLAB.setEntries(R.array.entries_farleftbutton);
		//mFLAB.setEntryValues(R.array.entryvalues_farleftbutton);
		
		//mFLAB.setEntries(mAppNames);
		//mFLAB.setEntryValues(mAppNames);
		//mFLAB.setOnPreferenceChangeListener(this);
		
    }
    
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;	
        if (preference == mUseStockLaunchPref) {
            value = mUseStockLaunchPref.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.USE_STOCK_LAUNCHER, value ? 1 : 0);
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            am.forceStopPackage("com.android.launcher");
        }
        return false;
    }
	

	@Override
	public void onClick(View v) {
		
	}


	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
        //if (preference == mFLAB) {
        //	Settings.System.putString(getContentResolver(), Settings.System.FARLEFT_AB, newValue.toString());
        //}
        return false;
	}



	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
	class Package {  
		private String Appname = "";
		private String PackagName = ""; 
		private Drawable icon;
	}  
}

 
