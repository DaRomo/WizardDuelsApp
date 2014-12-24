package com.WizardDuels;

import android.os.Bundle;
import android.app.Activity;

public class SpellSlot extends Activity
{
	public int position;
	public static String name;//determines spell
	public int y;//for later
	public boolean shieldOn=false;
	public boolean boosted=false;//does not work
	public int boostTime;//same
	public boolean burgered=false;//same
	public int burgerTime;//same
	public static int sc=0;
	public static String name2;
	public static String name3;
	public static String name4;
	public static int sc2;
	public static int sc3;
	
	public SpellSlot()//uncomment name to use that spell,comment all to use fireball
	{
		//position=//for later
		//name = "giga";
		//name="shield";
		//name="boost";
		//name="invert";
		//name = "burger";
		//name = "erase";
		burgerTime=0;
		this.y=position*20+120;	//for later
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//Options2 opt2 = new Options2();
		//name = opt2.y;
		//sc = opt2.z;
		//name2 = opt2.a;
		//sc2 = opt2.b;
		//name3 = opt2.c;
		//sc3 = opt2.d;
	}
	
	public int getSpell()
	{
		if(name=="giga")
		{
			sc=10;
		}
		else if(name=="shield")
		{
			sc=20;
		}
		else if(name=="boost")
		{
			sc=50;
		}
		else if(name=="invert")
		{
			sc=20;
		}
		else if(name=="burger")
		{
			sc=1;
		}
		else if(name=="erase")
		{
			sc=10;
		}
		else				
		{
			sc=5;
		}
		return(sc);
	}
	
	public int getSpell2()
	{
		if(name2=="giga")
		{
			sc2=10;
		}
		else if(name2=="shield")
		{
			sc2=20;
		}
		else if(name2=="boost")
		{
			sc2=50;
		}
		else if(name2=="invert")
		{
			sc2=20;
		}
		else if(name2=="burger")
		{
			sc2=1;
		}
		else if(name2=="erase")
		{
			sc2=10;
		}
		else				
		{
			sc2=5;
		}
		return(sc2);
	}
	
	public int getSpell3()
	{
		if(name3=="giga")
		{
			sc3=10;
		}
		else if(name3=="shield")
		{
			sc3=20;
		}
		else if(name3=="boost")
		{
			sc3=50;
		}
		else if(name3=="invert")
		{
			sc3=20;
		}
		else if(name3=="burger")
		{
			sc3=1;
		}
		else if(name3=="erase")
		{
			sc3=10;
		}
		else				
		{
			sc3=5;
		}
		return(sc3);
	}
	
	public String useSpell()
	{
		return(name);
	}
	public void unBoost()//thing that does not work
	{
		if(boosted==true)
		{
			boostTime+=.04;
			if (boostTime>=10)
			{
				boosted=false;
				boostTime=0;
			}
		}		
	}
	public void unBurger()//same
	{
			burgerTime+=.5;
			if (burgerTime>=1)
			{
				burgered=false;
				//burgerTime=0;
			}
				
	}
}
