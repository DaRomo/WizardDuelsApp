package com.WizardDuels;


import java.io.InputStream;
import java.util.Random;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.widget.ImageView;

/**
 * @version 2.6
 */
@SuppressWarnings("unused")
public class Gameplay extends Activity implements Callback, OnSharedPreferenceChangeListener
{

	private static final String TAG = "myTAG";
	Mana2 Bar=new Mana2();
	Options2 opt = new Options2();
	public static SpellSlot[] slots = new SpellSlot[4];	
	public static SpellSlot s = new SpellSlot();
	///////////////////////////////////////values
	Fireball giga = new Fireball(5000, -5000);
	public long deviceDT;		//difference in current system time of connected devices
	public Boolean ismyserve;	//determines if it is the user's serve
	public int my_score = 0;	//represents user's score
	public int his_score = 0;	//represents opponent's score
	public int win_score = 4;	//points needed to win
	public static float AI_response;	//used for difficulty level
	protected static final int balldiameter = 8;
	private static final boolean D = false;//set true to enable debugging aid

	//options menu values
	private static final int RESUME = 5;
	private static final int START_OVER = 6;
	private static final int QUIT = 7;
	///////////////////////////////////////drawing stuff
	public SurfaceView myview;
	public SurfaceHolder myholder;
	public PointF gridcenter;
	public Paint textpaint;	
	public Paint ballpaint;
	public Paint paddlepaint;	
	public Paint serve_line_paint; //
	public int xmin, xmax, ymin, ymax; //
	private float hratio;
	private float wratio;   
	/////////////////////
	public AnimationDrawable particles;
	public ImageView imageAnim;
	public static boolean didCollide=false;
	public boolean isLocked=false;
	public int counter=0;
	Matrix matrix = new Matrix();
	public static char collidedAt;
	public static float xcol;
	public static float ycol;	
	public Bitmap bitmapTable, bitmapAcorn, bitmapHisSq, bitmapMySq,bitmapMyHealth,bitmapHisHealth, bitmapManaBar, bitmapSpell1, bitmapSpell2, bitmapSpell3;
	public InputStream isTable,isAcorn,isFemale,isMale, isLL;
	Stack<InputStream> isParticle = new Stack<InputStream>();
	Stack<Bitmap> bitmapParticle = new Stack<Bitmap>();	
	///////////////////////////////////////new values
	protected Intent winintent;
	protected Intent loseintent;
	protected Boolean screen_contact;
	protected boolean track_flag;
	///////////////////////////////////////game objects
	private Gameplay self;	
	public int maxacorns = 5; //
	public Fireball[] myAcorns;	//
	public int Acornsinplay;//represents number of acorns in play
	public int serveque; 	//represents number of acorns in current serve queue
	public int hisserveque; 	//represents number of acorns in current serve queue
	public Wizard myWizard; //
	public Wizard hisSquirrel;//
	public Boolean ismale;//
	public Random myrand;//
	protected volatile physics p;
	///////////////////////////////////////sound FX
	private int SFX = 1;	
	protected long serve_que_interval = 1000/*2 seconds*/;	
	protected long my_serve_que_interval = 2000/*2 seconds*/;	
	protected long lastAcorn= System.currentTimeMillis();
	protected long mylastAcorn= System.currentTimeMillis();
	///////////////////////////////////////
	private int spell1;

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		boolean result = super.onPrepareOptionsMenu(menu);
		p.phy_pause();
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		boolean result = super.onCreateOptionsMenu(menu);
		
		menu.add(0, RESUME, 0, "Resume");
		menu.add(0, START_OVER, 0, "Start Over");
		menu.add(0, QUIT, 0, "Quit");

		return result;
	}

	/* Handles menu item selections */
	
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		switch (item.getItemId())
		{
			case RESUME:
				p.phy_resume();
				return true;
				
			case START_OVER:
				Intent j = new Intent(this, Gameplay.class);
	    		startActivity(j);
				return true;
				
			case QUIT:
				Intent k = new Intent(this, WizardDuels7Activity.class);
	    		startActivity(k);
				return true;
		}		
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.main);
		myview = (SurfaceView) findViewById(R.id.surface);
		myholder = myview.getHolder();		
		myholder.addCallback(self);
		gridcenter = new PointF(50, 75);
		textpaint = new Paint();
		ballpaint = new Paint();
		paddlepaint = new Paint();
		serve_line_paint = new Paint();
		track_flag = false;
		///////////////////////////////////////new values
		//Intent startingIntent = getIntent();
		screen_contact = false;
		///////////////////////////////////////
		isTable=this.getResources().openRawResource(R.drawable.gameplay2);
		bitmapTable = BitmapFactory.decodeStream(isTable);
		
		isAcorn=this.getResources().openRawResource(R.drawable.fireball);
		bitmapAcorn = BitmapFactory.decodeStream(isAcorn);		
		
		isLL = this.getResources().openRawResource(R.drawable.redbar);
		bitmapMyHealth = BitmapFactory.decodeStream(isLL);
		
		isLL = this.getResources().openRawResource(R.drawable.redbar);
		bitmapHisHealth = BitmapFactory.decodeStream(isLL);
		
		isLL = this.getResources().openRawResource(R.drawable.redbar);
		bitmapSpell1 = BitmapFactory.decodeStream(isLL);
		
		isLL = this.getResources().openRawResource(R.drawable.greenbar);
		bitmapManaBar = BitmapFactory.decodeStream(isLL);
		
		isMale=this.getResources().openRawResource(R.drawable.character1);
		bitmapMySq = BitmapFactory.decodeStream(isMale);
		
		isFemale=this.getResources().openRawResource(R.drawable.character2);
		bitmapHisSq = BitmapFactory.decodeStream(isFemale); 
		
		winintent= new Intent(self, WinScreen.class);
		loseintent= new Intent(self, LoseScreen.class);
					
		hisserveque = maxacorns;
		
		myAcorns = new Fireball[10];
		
		//Options2 opt = new Options2();
		//AI_response = opt.x;
		
		
		slots[0].name = "giga";
		slots[1].name2 = "giga";
		slots[2].name3 = "giga";
		slots[3].name4 = "none";
		
		Acornsinplay = 0;			//

		xmax = (int) Fireball.xmax;			//
		ymax = (int) Fireball.ymax;			//
		xmin = (int) Fireball.xmin;			//
		ymin = (int) Fireball.ymin;			//

		myrand = new Random(System.currentTimeMillis());//
		ismale = true;//

		myWizard = new Wizard();	//
		hisSquirrel = new Wizard(10);	//
		paddlepaint.setColor(Color.GREEN);
		serve_line_paint.setStrokeWidth(2);	
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		// register preference change listener
        prefs.registerOnSharedPreferenceChangeListener(this);

        // and set remembered preferences
        spell1 = Integer.parseInt(prefs.getString("sp1", "4"));

	}

	@Override
	public boolean onTrackballEvent(MotionEvent event)
	{
		float xpos = event.getX();

		if (track_flag)
			myWizard.xpos -= 100*(xpos / wratio);
		else
			myWizard.xpos += 100*(xpos / wratio);

		if (myWizard.xpos >= xmax-myWizard.rad)
			myWizard.xpos = xmax-myWizard.rad;
		if (myWizard.xpos <= xmin+myWizard.rad)
			myWizard.xpos = xmin+myWizard.rad;
		try
		{
			if (!p.playing)
			{
				handle_win();
			}

		}
		catch(NullPointerException exception)
		{

		}
		return false;
	}

	protected float screen_contact_point_X = -1;
	protected float screen_contact_point_Y = -1;

	protected int bar_error = 15;

	@Override										// changed to myWizard
	public boolean onTouchEvent(MotionEvent event) 
	{
		if (p != null)
		{
			if (!p.running)
				p.running = true;
		}
		
		screen_contact_point_X = event.getX();
		screen_contact_point_Y = event.getY() - bar_error;
		
		if (screen_contact_point_X < 30 && screen_contact_point_Y >= 385 && screen_contact_point_Y <= 415)
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{

					if(Bar.spell(50))
					{
						Bar.decrease(50);
						his_score--;
					}
				
				
				/*
				for (int i = 0; i<2; i++)
				{
					if (Bar.spell(slots[i].getSpell()))
					{
						giga.xpos=myWizard.xpos;
						giga.ypos=myWizard.ypos;
					}
					else if(slots[i].useSpell()=="boost")
					{
						if(slots[i].boosted=true)
						{
							Bar.mana+=slots[i].getSpell();
						}
					}
					else if(slots[i].useSpell()=="shield")
					{
						if(slots[i].shieldOn=true)
						{
							Bar.mana+=slots[i].getSpell();
						}
						slots[i].shieldOn=true;
					}
					else if(slots[i].useSpell()=="invert")
					{
						for(int j=0;j<Acornsinplay;j++)
						{
							myAcorns[i].update(-1);
						}
					}
					else if(slots[i].useSpell()=="burger")
					{
						if(slots[i].burgered=true)
						{
							Bar.mana+=slots[i].getSpell();
						}
						slots[i].burgered=true;
					}
					else if (slots[i].useSpell()=="erase")
					{
						for(int m = 0;m<Acornsinplay;m++)
						{
							myAcorns[m].bounce_num=0;
							myAcorns[m].xpos=myAcorns[m].xmax+20;
							myAcorns[m].update(1);
						}
					}
				}	*/
			}
		}
				
		if (screen_contact_point_Y/hratio > 115)
		{//move squirrel
			if (screen_contact)
				screen_contact = false;
			
			if ((event.getAction() == MotionEvent.ACTION_DOWN)||(event.getAction() == MotionEvent.ACTION_UP))
	        {
	            float xpos = event.getX();
	            myWizard.xpos = xpos / wratio;
	            myWizard.padpos.add(myWizard.xpos);
	            myWizard.xvel = 0;
	        } 
	        else if (event.getAction() == MotionEvent.ACTION_MOVE)
	        {
	            float xpos = event.getX();
	            myWizard.xpos = xpos / wratio;
	            long current_time = System.currentTimeMillis();
	            long dt = current_time - myWizard.last_update;
	            myWizard.last_update = current_time;
	            if (!myWizard.padpos.isEmpty())
	            	myWizard.xvel = (myWizard.xpos - myWizard.padpos.get(0)) *dt;
	            myWizard.padpos.add(myWizard.xpos);
	        }
			
		}
		
		else
		{//serve
			if (Bar.spell(1) && Acornsinplay < maxacorns && ((System.currentTimeMillis() - serve_que_interval) > mylastAcorn))
			{

				if ((event.getAction() == MotionEvent.ACTION_DOWN)
						||(event.getAction() == MotionEvent.ACTION_MOVE))
				{
					if (!screen_contact)
						screen_contact = true;
				}

				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (!screen_contact)
						screen_contact = true;

					//launch fireball
					else
					{
						myAcorns[Acornsinplay] = new Fireball(myWizard.xpos, myWizard.ypos-10,
							((screen_contact_point_X/wratio) - myWizard.xpos)/8,
							((screen_contact_point_Y/hratio) - myWizard.ypos)/8);
						Acornsinplay++;
						mylastAcorn = System.currentTimeMillis();
					}
					screen_contact = false;	
					Bar.decrease(1);
				}
			}			
		}
		
		try
		{
			if (!p.playing)
				handle_win();
		}
		catch(NullPointerException exception)
		{	}
		return false;
	}

	public class physics extends Thread
	{
		private volatile boolean running = true;
		private volatile boolean playing = false;		
		private long pausetime;
		private long resume_time;

		public void run()
		{
			lastAcorn= System.currentTimeMillis();
			mylastAcorn= System.currentTimeMillis();
			drawit2();
			while (running) 
			{
				if ((myAcorns != null)&& playing)	
				{
					drawit();

					int x = collision();				
					update_hisPad();

					int pointwin = 0;
					for (int i=0; (i< Acornsinplay && pointwin==0); i++)
					{
						if(myAcorns[i].update(1))//acorn is bounced out
						{
							Acornsinplay--;
							if (hisserveque <= 0)
							lastAcorn = System.currentTimeMillis();
							hisserveque++;
							for (int j = i; j< Acornsinplay; j++)//delete acorn after bounce limit
								myAcorns[j] = myAcorns[j+1];				
						}
					}
					giga.update(1);
					Bar.inc();
					//drawit();

					if (x == 1)
					{
						if(s.boosted)
						{
							my_score++;
						}
						my_score++;
						Bar.mana+=5;
						handle_win();	
					}	

					if (x==-1)
					{
						if(s.shieldOn)
						{
							s.shieldOn=false;
						}
						else
						{
							his_score++;
						}
						Bar.mana++;
						handle_win();
					}
				}
			}
		}

		public void safeStop() 
		{
			playing = false;
			running = false;
			interrupt();
		}
		
		public void phy_pause()
		{
			playing = false;
			pausetime = System.currentTimeMillis();			
			
		}
		public void phy_resume()
		{
			resume_time = System.currentTimeMillis();
			for (int i=0; (i< Acornsinplay); i++)//
			{
				myAcorns[i].last_update += resume_time-pausetime;
				giga.last_update += resume_time-pausetime;//
			}
			hisSquirrel.last_update+= resume_time-pausetime;
			playing = true;
		}
	}

	private void handle_win()
	{

		if (my_score >= win_score)
		{
			screen_contact=true;
			//p.running=true;
			self.startActivityForResult(winintent,0);
		}
		if (his_score>=win_score)
		{
			screen_contact=true;
			//p.running=true;
			self.startActivityForResult(loseintent,0);	
		}
		else
		{			
			serveque = 0; 				
			hisserveque = maxacorns;	
			p.playing = true;
		}
	}

	@SuppressWarnings("static-access")
	public int collision()
	{
		int ret_val=2;
		
		try 
		{
			p.sleep(1);
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}

		for (int i=0; (i < Acornsinplay)&&(ret_val ==2); i++)//
		{
			float xdiff = myWizard.xpos - myAcorns[i].xpos;//
			float ydiff = myWizard.ypos - myAcorns[i].ypos;    	//	
			float distance = (float) Math.sqrt((xdiff*xdiff)+(ydiff*ydiff));//
			if (distance < (myWizard.rad + myAcorns[i].rad))
			{
				ret_val = -1;//
				Acornsinplay--;
				if (hisserveque <= 0)
				lastAcorn = System.currentTimeMillis();
				hisserveque++;
				for (int j = i; j< Acornsinplay; j++)//delete acorn after bounce limit
					myAcorns[j] = myAcorns[j+1];
			}
			xdiff = hisSquirrel.xpos - myAcorns[i].xpos;//
			ydiff = hisSquirrel.ypos - myAcorns[i].ypos;//    		
			distance = (float) Math.sqrt((xdiff*xdiff)+(ydiff*ydiff));//
			if (distance < (myWizard.rad + myAcorns[i].rad))//
			{
				ret_val=1;
				Acornsinplay--;
				if (hisserveque <= 0)
				lastAcorn = System.currentTimeMillis();
				hisserveque++;
				for (int j = i; j< Acornsinplay; j++)//delete acorn after bounce limit
					myAcorns[j] = myAcorns[j+1];	
			}
		}
		float xdiff = hisSquirrel.xpos - giga.xpos;//
		float ydiff = hisSquirrel.ypos - giga.ypos;    	//	
		float distance = (float) Math.sqrt((xdiff*xdiff)+(ydiff*ydiff));
		if(distance<(hisSquirrel.rad+giga.rad))
		{
			ret_val=1;
			giga.ypos=-5000;
		}
		return ret_val;//
	}

	public void update_hisPad()
	{
		if (D) Log.d(TAG, "update his pad"); //
		long current_time = System.currentTimeMillis();
		long time_diff = current_time - hisSquirrel.last_update;   
		float newxpos = hisSquirrel.xpos;
		float newxvel = hisSquirrel.xvel;
		float newxacel = hisSquirrel.xacel;		
		int im_threat = get_iminent_threat();	
		if (im_threat == -1)//there is no threat
		{
			newxacel = (gridcenter.x - hisSquirrel.xpos)/50;//go back to center			
			if (hisSquirrel.xpos-hisSquirrel.rad<xmin && hisSquirrel.xvel<0)// squirrel boundary
			{
				newxpos += (xmin-(newxpos-hisSquirrel.rad));	//
				newxvel *= 0;							//
			}
			if (hisSquirrel.xpos+hisSquirrel.rad>xmax && hisSquirrel.xvel>0)// squirrel boundary
			{
				newxpos -= ((newxpos+hisSquirrel.rad) -xmax);	//
				newxvel *= 0;							//
			}
			newxpos += (newxvel * time_diff)/100;
			newxvel *=.8;
			newxvel += (newxacel * time_diff)/100;
			
			if (newxvel > 6)//max velocity
				newxvel = 6;//max velocity
			if (newxvel < -6)//max velocity
				newxvel = -6;//max velocity
			hisSquirrel.xpos = newxpos;
			hisSquirrel.xvel = newxvel;
		}
		else//handle threat
		{			
			//run away from squirrel intercept
			float sqXintcpt = ((hisSquirrel.ypos+hisSquirrel.rad)- myAcorns[im_threat].ypos)/myAcorns[im_threat].yvel;
			sqXintcpt = myAcorns[im_threat].xpos+ sqXintcpt*myAcorns[im_threat].xvel;
			if (sqXintcpt-myAcorns[im_threat].rad < xmin)
			{
				sqXintcpt += (xmin-(sqXintcpt-myAcorns[im_threat].rad));
			}
			if (sqXintcpt+myAcorns[im_threat].rad > xmax)
			{
				sqXintcpt -= (sqXintcpt + myAcorns[im_threat].rad)-xmax;
			}
						
			newxacel = 1000/((myAcorns[im_threat].ypos-hisSquirrel.ypos)*(hisSquirrel.xpos-(sqXintcpt)));
			
			if(giga.ypos-10>0)
			{	
				if (sqXintcpt-giga.rad -4< xmin)
				{
					sqXintcpt += (xmin-(sqXintcpt-giga.rad-4));
				}
				if (sqXintcpt+giga.rad +4> xmax)
				{
					sqXintcpt -= (sqXintcpt + giga.rad+4)-xmax;
				}
				newxacel = 1000/((giga.ypos-hisSquirrel.ypos)*(hisSquirrel.xpos-(sqXintcpt)));
			}
			if (hisSquirrel.xpos-hisSquirrel.rad<xmin)
			{
				newxvel = 0;
				hisSquirrel.xpos = xmin;
			}
			else if (hisSquirrel.xpos+hisSquirrel.rad>xmax)
			{
				newxvel = 0;
				hisSquirrel.xpos = xmax;
			}
			else
			{
				newxvel += (newxacel * time_diff)/100;
				newxpos += (newxvel * time_diff)/100;
				hisSquirrel.xpos = newxpos;
				hisSquirrel.xvel = newxvel;
			}
		}
		
		hisSquirrel.last_update = current_time;
		
		// launch ball if 50% of the time at given interval

		if ((hisserveque>0) 
				&& (Acornsinplay < maxacorns) && 
				((System.currentTimeMillis() - serve_que_interval)
						> lastAcorn))
		{
			if (myrand.nextFloat() < .2)
			{
				myAcorns[Acornsinplay] = new Fireball(hisSquirrel.xpos, hisSquirrel.ypos+10, (float) (myrand.nextFloat()*2.512 + .314));
				Acornsinplay++;
				hisserveque--;
				lastAcorn = System.currentTimeMillis();
			}
		}		
	}
	
	private int get_iminent_threat()//returns index of closest acorn with positive velocity
	{						//if there are no acorns with a positive velocity, -1 is returned
		int ret_val = -1;
		for (int i=0; i<Acornsinplay; i++)
		{//		acorn is coming towards me or is in touching range
			if ((myAcorns[i].yvel <0 || myAcorns[i].ypos < hisSquirrel.ypos+15)//acorn is coming towards me or is in touching range
					&& myAcorns[i].ypos < gridcenter.y-25)//acorn is in threat range
			{
				if (ret_val==-1)
					ret_val = i;//first one
				else if (myAcorns[i].ypos < myAcorns[ret_val].ypos)//get most iminent(closest)
					ret_val = i;
			}
		}
		return ret_val;		
	}
	
	public void drawit()
	{  	
		Canvas c = null;
		try {
			c = myholder.lockCanvas();
			if (c != null) 
			{
				hratio = c.getHeight()/Fireball.ymax;//
				wratio = c.getWidth()/Fireball.xmax;   // 			

				//draw background
				c.drawBitmap(bitmapTable, null,new Rect(0,0,c.getWidth(),c.getHeight()),null);
				 								
				//draw my health bar
				for (int i=win_score;i>= his_score; i--)
				{
					if (i == 0)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								0,//left border
								650,//top border
								480,//right border
								660),//bottom border
								null);					
					}
					else if (i == 1)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								60,//left border
								650,//top border
								420,//right border
								660),//bottom border
								null);					
					}
					else if (i == 2)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								120,//left border
								650,//top border
								360,//right border
								660),//bottom border
								null);				
					}
					else if (i == 3)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								180,//left border
								650,//top border
								300,//right border
								660),//bottom border
								null);				
					}
					else if (i == 4)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								240,//left border
								650,//top border
								240,//right border
								660),//bottom border
								null);		
					}

				}
				//draw his health bar
				for (int i=win_score;i>= my_score; i--)
				{
					if (i == 0)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								0,//left border
								100,//top border
								480,//right border
								110),//bottom border
								null);					
					}
					else if (i == 1)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								60,//left border
								100,//top border
								420,//right border
								110),//bottom border
								null);					
					}
					else if (i == 2)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								120,//left border
								100,//top border
								360,//right border
								110),//bottom border
								null);				
					}
					else if (i == 3)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								180,//left border
								100,//top border
								300,//right border
								110),//bottom border
								null);				
					}
					else if (i == 4)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								240,//left border
								100,//top border
								240,//right border
								110),//bottom border
								null);		
					}

				}
				/*
				//draw my serve queue
				for (int i=0 ; i<serveque ; i++)
				{
					c.drawBitmap(bitmapAcorn, null, new RectF(
							(xmax-((i+1)*balldiameter))*wratio,//left border
							(ymax-balldiameter - 2)*hratio,//top border
							(xmax-i*balldiameter)*wratio,//right border
							(ymax-2)*hratio),//bottom border
							null);
				}
				//draw his serve queue
					for (int i=0 ; i<hisserveque ; i++)
					{
						c.drawBitmap(bitmapAcorn, null, new RectF(
								(xmin+i*balldiameter)*wratio,//left border
								
								(ymin+2)*hratio,//top border
								
								(xmin+((i+1)*balldiameter))*wratio,//right border
								
								(ymin+balldiameter+2)*hratio),//bottom border
								null);
					}
				*/
				// draw fireballs
				for (int i=0; (i< Acornsinplay); i++)
				{
					c.drawBitmap(bitmapAcorn, null, new RectF(
							wratio*(myAcorns[i].xpos-8),//left border
							hratio*(myAcorns[i].ypos-8),//top border
							wratio*(myAcorns[i].xpos+8),//right border
							hratio*(myAcorns[i].ypos+8)),//bottom border
							null);
				}
			
				//draw serve line
				if (screen_contact)
				{
					c.drawLine(myWizard.xpos*wratio, (myWizard.ypos-10)*hratio,
							screen_contact_point_X, screen_contact_point_Y, serve_line_paint);
				}
				

				// draw my wizard
				c.drawBitmap(bitmapMySq, null, new RectF(
						wratio*(myWizard.xpos-myWizard.rad-4),//left border
						hratio*(myWizard.ypos-myWizard.rad-4),//top border
						wratio*(myWizard.xpos+myWizard.rad+4),//right border
						hratio*(myWizard.ypos+myWizard.rad+4)),//bottom border
						null);

				// draw his wizard
				c.drawBitmap(bitmapHisSq, null, new RectF(
						wratio*(hisSquirrel.xpos-hisSquirrel.rad-4),//left border
						hratio*(hisSquirrel.ypos-hisSquirrel.rad-4),//top border
						wratio*(hisSquirrel.xpos+hisSquirrel.rad+4),//right border
						hratio*(hisSquirrel.ypos+hisSquirrel.rad+4)),//bottom border
						null);
			}
		
			// draw mana bar
			c.drawBitmap(bitmapManaBar, null, new RectF(460, -10*Bar.mana+840, 480, 840), null);
			
			//draw first spell icon
			c.drawBitmap(bitmapSpell1, null, new RectF(0, 385, 30, 415), null);
}
		finally 
		{
			if (c != null) 
			{
				myholder.unlockCanvasAndPost(c);
			}
		}
	}
	public void drawit2()
	{  	
		Canvas c = null;
		try {
			c = myholder.lockCanvas();
			if (c != null) 
			{
				hratio = c.getHeight()/Fireball.ymax;//
				wratio = c.getWidth()/Fireball.xmax;   // 			

				//draw background
				c.drawBitmap(bitmapTable, null,new Rect(0,0,c.getWidth(),c.getHeight()),null);

				//draw my health bar
				for (int i=win_score;i>= his_score; i--)
				{
					if (i == 0)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								0,//left border
								650,//top border
								480,//right border
								660),//bottom border
								null);					
					}
					else if (i == 1)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								60,//left border
								650,//top border
								420,//right border
								660),//bottom border
								null);					
					}
					else if (i == 2)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								120,//left border
								650,//top border
								360,//right border
								660),//bottom border
								null);				
					}
					else if (i == 3)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								180,//left border
								650,//top border
								300,//right border
								660),//bottom border
								null);				
					}
					else if (i == 4)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								240,//left border
								650,//top border
								240,//right border
								660),//bottom border
								null);		
					}
				}
				//draw his health bar
				for (int i=win_score;i>= my_score; i--)
				{
					if (i == 0)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								0,//left border
								100,//top border
								480,//right border
								110),//bottom border
								null);					
					}
					else if (i == 1)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								60,//left border
								100,//top border
								420,//right border
								110),//bottom border
								null);					
					}
					else if (i == 2)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								120,//left border
								100,//top border
								360,//right border
								110),//bottom border
								null);				
					}
					else if (i == 3)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								180,//left border
								100,//top border
								300,//right border
								110),//bottom border
								null);				
					}
					else if (i == 4)
					{
						c.drawBitmap(bitmapMyHealth, null, new RectF(
								240,//left border
								100,//top border
								240,//right border
								110),//bottom border
								null);		
					}

				}			
				// draw my wizard
				c.drawBitmap(bitmapMySq, null, new RectF(
						wratio*(myWizard.xpos-myWizard.rad-4),//left border
						hratio*(myWizard.ypos-myWizard.rad-4),//top border
						wratio*(myWizard.xpos+myWizard.rad+4),//right border
						hratio*(myWizard.ypos+myWizard.rad+4)),//bottom border
						null);
				
				// draw his wizard
				c.drawBitmap(bitmapHisSq, null, new RectF(
						wratio*(hisSquirrel.xpos-hisSquirrel.rad-4),//left border
						hratio*(hisSquirrel.ypos-hisSquirrel.rad-4),//top border
						wratio*(hisSquirrel.xpos+hisSquirrel.rad+4),//right border
						hratio*(hisSquirrel.ypos+hisSquirrel.rad+4)),//bottom border
						null);				
			}
		} finally {
			if (c != null) 
			{
				myholder.unlockCanvasAndPost(c);
			}
		}
	}
	
	public void drawit3()
	{
		Canvas c = null;
		try 
		{
			c = myholder.lockCanvas();
			if (c != null) 
			{
				c.drawBitmap(bitmapSpell1, null, new RectF(210, 370, 270, 430), null);
			}
		}
		finally 
		{
			if (c != null) 
			{
				myholder.unlockCanvasAndPost(c);
			}
		}
		
	}
	
	public void surfaceCreated(SurfaceHolder holder)
	{
		p = new physics();
		p.start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{

		try {
			p.safeStop();
		} finally {
			p = null;
		}		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if ((resultCode == Activity.RESULT_OK) && (data.getBooleanExtra("main", false)==true))
		{
			setResult(Activity.RESULT_OK);
			finish();
		} 
		{
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
	}
	
    @Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}
    
    @Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.gameplay);
	}
    
    // handle updates to preferences
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) 
    {
        if (key.equals("spellValues"))
        {
            spell1 = Integer.parseInt(prefs.getString("sp1", "4"));
        }
        // etc
    }

}