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
		if (p.get_armor("����ʨ��")) p.hp=Integer.min(p.hp+1,p.maxhp);
		Card c=p.armor;
		p.armor=this;
		equip="����";
		System.out.print(">> "+p.name+" װ���˷���");
		f.update(p);
		println();
		Thread.currentThread().sleep(300);
	}
}
