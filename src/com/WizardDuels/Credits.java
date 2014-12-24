package com.WizardDuels;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

/**
 * @author Matthew Romanelli
 */
public class Credits extends Activity implements OnClickListener
{
	/**
	 * @param Bundle savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credits);
		
		//Set up click listeners for all the buttons
		View backButton = findViewById(R.id.back_button4);
		backButton.setOnClickListener(this); 
	}
	
	/**
	 * @param View v
	 */
	public void onClick(View v) 
	{
		switch (v.getId())
    	{
    	case R.id.back_button4:
    		Intent i = new Intent(this, LoseScreen.class);
    		startActivity(i);
    		break;
    	}
	}
}
