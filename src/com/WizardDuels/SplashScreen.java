package com.WizardDuels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Matthew Romanelli
 */
public class SplashScreen extends Activity {

	/**
	 * @param Bundle savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		Thread splashThread = new Thread(){

			@Override
			public void run() {
				super.run();
				
				try {
					int waited=0;
					while(waited < 1500){
						sleep(100);
						waited+=100;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					finish();
					Intent i = new Intent(SplashScreen.this,WizardDuels7Activity.class);
					startActivity(i);
				}
			}			
		};
		splashThread.start();
	}	
}
