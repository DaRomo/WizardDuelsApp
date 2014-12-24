package com.WizardDuels;

public class Fireball
{
	public final static float xmax = 100;//TODO
	protected final static float ymax = 150;//TODO
	protected final static float xmin = 0;//TODO
	protected final static float ymin = 0;//TODO

	public final static double acell_decay_rate = 0.6;
	public final static double acell_rate = .1;
	public volatile float xpos;
	public volatile float ypos;
	protected volatile float xvel;
	protected volatile float yvel;
	protected volatile float xacel;
	protected volatile float yacel;
	public boolean giga=false;
	public volatile int bounce_num;

	protected float rad;
	protected volatile long last_update;

	public Fireball()
	{
		xpos = 75;
		ypos = 35;
		xvel = 0;
		yvel = 5;
		xacel = 0;
		yacel = 20;
		rad = 3;
		bounce_num = 2;


		last_update = System.currentTimeMillis();
	}
	public Fireball(float initX, float initY)
	{
		xpos = initX;
		ypos = initY;
		xvel = 0;
		yvel = -10;
		xacel = 0;
		yacel = 0;
		rad = 8;
		bounce_num = 0;
	}
	public Fireball(float initX, float initY, float initTheta)
	{
		xpos = initX;
		ypos = initY;
		xvel = (float) (Math.cos(initTheta)*8);
		yvel = (float) (Math.sin(initTheta)*8);
		xacel = 0;
		yacel = 0;
		rad = 3;
		bounce_num = 2;


		last_update = System.currentTimeMillis();
	}
	public Fireball(float initX, float initY, float initX_vel, float initY_vel)
	{
		xpos = initX;
		ypos = initY;
		xvel = initX_vel;
		yvel = initY_vel;
		xacel = 0;
		yacel = 0;
		rad = 3;
		bounce_num = 2;


		last_update = System.currentTimeMillis();
	}

	public void setVelocity(float ax, float ay) 
	{
		xvel = ax;
		yvel= ay;
	}
	public void setPosition(float ax, float ay) 
	{
		xpos = ax;
		ypos= ay;
	}



	public boolean update(int x)
	{
		boolean ret_val = false;
		long current_time = System.currentTimeMillis();
		long time_diff = current_time - last_update;    
		Float newxpos = xpos;
		Float newypos = ypos;
		Float newxvel = xvel;
		Float newyvel = yvel;


		newxpos += (xvel * time_diff)/100;
		newypos += (yvel * time_diff)/100;

		newxvel += ((time_diff * xacel) / 100000);
		newyvel += ((time_diff * yacel) / 100000);


		if (newxpos-rad < xmin)
		{

			if (bounce_num > 0)
			{
				xacel *=.9;
				newxpos += (xmin-(newxpos-rad));
				newxvel *= -1;
				bounce_num--;

			}
			else
			{
				ret_val = true;
			}
		}
		if (newxpos+rad > xmax)
		{


			if (bounce_num > 0)
			{
				xacel *=.9;
				newxpos -= (newxpos + rad)-xmax;
				newxvel *= -1;
				bounce_num--;

			}
			else
			{
				ret_val = true;
			}
		}

		if (newypos-rad < ymin && giga == false)
		{
			if (bounce_num > 0)
			{
				newypos += (ymin-(newypos-rad));
				newyvel *= -1;
				bounce_num--;

			}
			else
			{
				ret_val = true;
			}
		}
		if (newypos+rad > ymax)
		{
			if (bounce_num > 0)
			{
				newypos -= (newypos + rad)-ymax;
				newyvel *= -1;
				bounce_num--;
			}
			else
			{
				ret_val = true;
			}

		}
		xpos = newxpos;
		ypos = newypos;
		xvel = newxvel*x;
		yvel = newyvel*x;

		last_update = current_time;
		return ret_val;
	}

}