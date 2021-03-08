package sanguosha;

import java.util.ArrayList;
import java.util.Scanner;

public class Def extends Card{
	Def(int c,int n,String s)
	{
		super(c,n,s);
		type=5;
	}
	
	void use(Player p,Scanner sc) throws InterruptedException
	{
		Card c=p.def;
		equip="防御马";
		if (p.def!=null) p.def.drop(p,1);
		p.def=this;
		System.out.print(">> "+p.name+" 装备了防御马");
		println();
		f.update(p);
		Thread.currentThread().sleep(300);

	}

}
