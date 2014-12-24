package com.WizardDuels;

public class Mana2 {

	public int mana;
	public double inc;
	public Mana2()
	{
		mana=0;
	}
	public boolean spell(int spellCost)//checks if there is enough mana
	{
		boolean ok = false;
		if(mana-spellCost>=0)
		{
			ok=true;
		}
		return ok;
	}
	public void decrease(int spellCost)//subtracts spell cost from mana
	{
		mana=mana-spellCost;
	}
	public void inc()//increase mana by 1 every so often
	{
		inc+=.065;
		
		if (inc>=1)
		{
			inc--;
			mana++;
		}
	}
}

