package com.WizardDuels;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

/**
 * @author Matthew Romanelli
 */
public class Instructions extends Activity implements OnClickListener
{
	
	/**
	 * @param Bundle savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions);
	
		//Set up click listeners for all the buttons
		View backButton = findViewById(R.id.back_button3);
		backButton.setOnClickListener(this); 
	}
	
	/**
	 * @param View v
	 */
	public void onClick(View v) 
	{
		switch (v.getId())
    	{
    	case R.id.back_button3:
    		Intent i = new Intent(this, WizardDuels7Activity.class);
    		startActivity(i);
    		break;
    	}
	}
}
