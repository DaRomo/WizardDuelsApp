package com.WizardDuels;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.media.MediaPlayer;
import android.media.AudioManager;

/**
 * @author Matthew Romanelli
 */
public class WizardDuels7Activity extends Activity implements OnClickListener
{
	private WizardDuels7Activity self;
	private MediaPlayer mp;
	
	/**
	 * @param Bundle savedInstanceState
	 */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.screen);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        //Set up click listeners for all the buttons
        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
        
        View instructionsButton = findViewById(R.id.instructions_button);
        instructionsButton.setOnClickListener(this);
        
        View storyButton = findViewById(R.id.story_button);
        storyButton.setOnClickListener(this);
        
        View settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);
    }
    /**
	 * @param View v
	 */
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.instructions_button:
    		Intent i = new Intent(this, Instructions.class);
    		startActivity(i);
    		break;
    	case R.id.story_button:
    		Intent j = new Intent(this, Story.class);
    		startActivity(j);
    		break;
    	case R.id.settings_button:
    		Intent k = new Intent(this, Options2.class);
    		startActivity(k);
    		break;
    	case R.id.start_button:
    		Intent l = new Intent(this, Gameplay.class);
    		startActivity(l);
    		break;
    	}
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    	Music.stop(this);
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	Music.play(this, R.raw.mainmenu);
    }
}