package sanguosha;

import java.util.ArrayList;
import java.util.Scanner;

public class Atk extends Card{
	Atk(int c,int n,String s)
	{
		super(c,n,s);
		type=4;
	}
	
	void use(Player p,Scanner sc) throws InterruptedException
	{
		Card c=p.atk;
		equip="攻击马";
		if (p.atk!=null) p.atk.drop(p,1);
		p.atk=this;
		System.out.print(">> "+p.name+" 装备了攻击马");
		println();
		f.update(p);
		Thread.currentThread().sleep(300);
	}
}
