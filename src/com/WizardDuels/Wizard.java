package com.WizardDuels;

import java.util.ArrayList;

public class Wizard 
{
	
	public enum character {MALE,FEMALE};
	character chartype;
	
	
	static final int max_vel = 10;
	protected volatile float xpos;
	protected volatile float ypos;
	protected volatile float xvel;
	protected volatile float yvel;
	protected volatile float xacel;
	protected int rad;
	protected volatile ArrayList<Float> padpos;
	protected volatile long last_update;
	
	protected volatile long vel_update_freq;
	protected volatile long last_vel_update;
	
	public Wizard()
	{
        xpos = 50;
        ypos = 135;
        xvel = 0;
        yvel = 0;
        xacel = 0;
        rad = 7;
        padpos = new ArrayList<Float>();
        padpos.add(xpos);
        last_update = System.currentTimeMillis();
        last_vel_update = System.currentTimeMillis();
        vel_update_freq = 3000;
	}
	public Wizard(float initY)
	{
        xpos = 50;
        ypos = initY;
        xvel = 0;
        yvel = 0;
        xacel = 0;
        rad = 7;
        padpos = new ArrayList<Float>();
        padpos.add(xpos);
        last_update = System.currentTimeMillis();
        last_vel_update = System.currentTimeMillis();
        vel_update_freq = 3000;
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
}
