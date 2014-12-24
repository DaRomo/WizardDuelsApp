package com.WizardDuels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
* @author Matthew Romanelli
*/
public class LoseScreen extends Activity implements OnClickListener
{
	
	/**
	 * @param Bundle savedInstanceState
	 */	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.losescreen);

		//Set up click listeners for all the buttons
		View mainMenu = findViewById(R.id.main_menu_button);
		mainMenu.setOnClickListener(this); 

		View playAgain = findViewById(R.id.play_again_button);
		playAgain.setOnClickListener(this); 
		
		View credits = findViewById(R.id.credits_button);
		credits.setOnClickListener(this);
	}
	
	/**
	 * @param View v
	 */
	public void onClick(View v) 
	{
		switch (v.getId())
    	{
    		case R.id.main_menu_button:
    			Intent i = new Intent(this, WizardDuels7Activity.class);
    			startActivity(i);
    			break;
    		
    		case R.id.play_again_button:
        		Intent j = new Intent(this, Gameplay.class);
        		startActivity(j);
        		break;	
    		
    	case R.id.credits_button:
    		Intent k = new Intent(this, Credits.class);
    		startActivity(k);
    		break;
    	}
	}
}