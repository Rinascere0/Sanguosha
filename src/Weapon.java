package sanguosha;

import java.util.ArrayList;
import java.util.Scanner;

public class Weapon extends Card{
	int dist;
	Weapon(int c,int n,String s,int d)
	{
		super(c,n,s);
		type=2;
		dist=d;
	}
	
	void use(Player p,Scanner sc) throws InterruptedException
	{
		Card c=p.weapon;
		equip="武器";
		if (p.weapon!=null)
			p.weapon.drop(p, 1);
		p.weapon=this;
		if (name.equals("诸葛连弩")) p.maxkill=100;
		System.out.print(">> "+p.name+" 装备了武器");
		println();
		f.update(p);
		Thread.currentThread().sleep(300);
	}
	
	void check() {}

}
