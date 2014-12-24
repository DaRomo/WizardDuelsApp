package com.WizardDuels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Matthew Romanelli
 */
public class WinScreen extends Activity implements OnClickListener
{	
	/**
	 * @param Bundle savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.winscreen);

		//Set up click listeners for all the buttons
		View mainMenu = findViewById(R.id.main_menu_button_3);
		mainMenu.setOnClickListener(this); 
		
		View playAgain = findViewById(R.id.play_again_button_3);
		playAgain.setOnClickListener(this);
	
		View credits = findViewById(R.id.credits_button_3);
		credits.setOnClickListener(this);
	}
	/**
	 * @param View v
	 */
	public void onClick(View v) 
	{
		switch (v.getId())
    	{
    	case R.id.main_menu_button_3:
    		Intent i = new Intent(this, WizardDuels7Activity.class);
    		startActivity(i);
    		break;
    	case R.id.play_again_button_3:
    		Intent j = new Intent(this, Gameplay.class);
    		startActivity(j);
    		break;
    	case R.id.credits_button_3:
    		Intent k = new Intent(this, Credits2.class);
    		startActivity(k);
    		break;
    	}
	}
}