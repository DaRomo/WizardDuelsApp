package com.WizardDuels;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.content.Context;
import android.preference.PreferenceManager;

/**
 * @author Matthew Romanelli
 */
public class Options2 extends PreferenceActivity implements OnClickListener
{
	//Options names and default values
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEF = true; 
	
	/**
	 * @param Bundle savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		setContentView(R.layout.options);
		
		//Set up click listeners for all the buttons
		View backButton = findViewById(R.id.back_button2);
		backButton.setOnClickListener(this); 
	}
	
	/**
	 * @param View v
	 */	
	public void onClick(View v) 
	{
		switch (v.getId())
    	{
    	case R.id.back_button2:
    		Intent i = new Intent(this, WizardDuels7Activity.class);
    		startActivity(i);
    		break;
    	}
	}
	
	/**
	 * Get the current value of the music option
	 */
	public static boolean getMusic(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
	}
}

