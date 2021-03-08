import java.util.ArrayList;
import java.util.Scanner;

public class Armor extends Card{
	Armor(int c,int n,String s)
	{
		super(c,n,s);
		type=3;
	}
	
	void use(Player p,Scanner sc) throws InterruptedException
	{
		if (p.armor!=null) p.armor.drop(p, 1);
		if (p.get_armor("白银狮子")) p.hp=Integer.min(p.hp+1,p.maxhp);
		Card c=p.armor;
		p.armor=this;
		equip="防具";
		System.out.print(">> "+p.name+" 装备了防具");
		f.update(p);
		println();
		Thread.currentThread().sleep(300);
	}
}
