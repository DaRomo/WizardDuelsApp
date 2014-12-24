package com.WizardDuels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * @author Matthew Romanelli
 */
public class Splash extends Activity
{
	private ImageView Logo;
	
	/**
	 * @param Bundle savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash);
			Logo = (ImageView)findViewById(R.id.logo);
			Logo.setImageResource(R.drawable.faulogonew);
			Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade);
			Logo.startAnimation(fade); 
		 Thread splashThread = new Thread()
		 {
			 	@Override
				public void run() 
			 	{
					super.run();

					try
					{
						int waited=0;
						while(waited < 5000)
						{
							sleep(100); 
							waited+=100;
						} 
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					finally
					{
						finish();
						
						Intent i = new Intent(Splash.this, SplashScreen.class);
						startActivity(i);
					}
				}				
		};
		splashThread.start();
	}
}