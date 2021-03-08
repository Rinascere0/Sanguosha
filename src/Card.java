package sanguosha;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Card {
	int color;
	int num;
	int type; //卡牌类型
	String name; //卡牌使用名
	Card origin; //被当作其他牌使用时，指向原卡牌
	String equip; //装备位置
	boolean stat; //使用后是否被收走
	Scanner sc;
	ArrayList<Player>players;
	ArrayList<Card>all_cards;
	ArrayList<Card>sub;
	Frame f;
	String skill;
	Card(int c,int n,String s) //初始化
	{
		color=c;
		num=n;
		name=s;
		origin=this;
		stat=true;
		equip=null;
		skill=null;
		sub=null;
	}
	
	Card(String s,Frame frame,ArrayList<Player>p,ArrayList<Card>c)
	{
		f=frame;
		players=p;
		all_cards=c;
		color=-1;
		name=s;
		origin=this;
		sub=null;
		stat=true;
	}
	
	Card(Card c,String s,String sk)
	{
		color=c.color;
		num=c.num;
		name=s;
		skill=sk;
		origin=c;
		stat=true;
		equip=null;
		f=c.f;
		players=c.players;
		all_cards=c.all_cards;
		sub=null;
	}
	
	String get_printname()
	{
		return "("+get_name()+")";
	}
	
	void print() //输出卡牌具体内容
	{
		System.out.print("("+get_name()+")");
	}
	
	String get_color() //获得卡牌花色
	{
		String c;
		switch(color)
		{
			case 0:c="黑桃";break;
			case 1:c="红桃";break;
			case 2:c="梅花";break;
			case 3:c="方片";break;
			default:c="";
		}
		return c;
	}
	
	String get_equip()
	{
		if (equip==null)
			return "手牌";
		else
			return equip;
	}
	
	String get_name() //获得卡牌花色+文字
	{
		String d;
		switch (num)
		{
			case 0:return "";
			case 1:d="A";break;
		 	case 11:d="J";break;
		 	case 12:d="Q";break;
		 	case 13:d="K";break;
		 	default:d=String.valueOf(num);
		}
		if (origin.name.equals(name))
			return get_color()+' '+d+' '+origin.name;
		else
			return get_color()+' '+d+' '+origin.name+"("+name+")";
	}
	
	void set_stat()
	{
		stat=false;
		if (sub==null)
			origin.stat=false;
		else
			for (int i=0;i<sub.size();i++)
				sub.get(i).stat=false;
		
	}
	
	void show() //打印卡牌花色+文字
	{
		System.out.print(get_name());
	}
	
	void println() //打印出牌时卡牌具体文字
	{
		System.out.println("("+get_name()+")");
	}
	
	String show_color() //获得卡牌花色（用于判定）
	{
		return "【"+get_color()+"】";
	}
	
	String showname() //获得卡牌名称（用于出牌）
	{
		return "【"+name+"】"; 
	}
	
	void print_origin()
	{
		if (sub==null) origin.print();
		else
		{
			System.out.print("("+sub.get(0).get_name());
			for (int i=1;i<sub.size();i++)
				System.out.print("、"+sub.get(i).get_name());
			System.out.print(")");
			
		}
	}
	//使用卡牌（虚函数）
	void use(Player p,Scanner sc) throws InterruptedException, Exception
	{

	} 

	Card get_card(Player target)  //随机获得目标一张牌
	{
		Card c;
		ArrayList<Integer>index=new ArrayList<Integer>();
		if (!target.cards.isEmpty())
			index.add(0);
		if (target.weapon!=null)
			index.add(1);
		if (target.armor!=null)
			index.add(2);
		if (target.def!=null)
			index.add(3);
		if (target.atk!=null)		
			index.add(4);
		switch(index.get(new Random().nextInt(index.size())))
		{
			case 0:
					return target.cards.get(new Random().nextInt(target.cards.size()));
			case 1:
					c=target.weapon;
					target.weapon=null;
					return c;
			case 2:
					c=target.armor;
					target.armor=null;
					return c;
			case 3:
					c=target.def;
					target.def=null;
					return c;
			default:
					c=target.atk;
					target.def=null;
					return c;
			}
		}
	
	Boolean is_kill()  //判断此牌是否为【杀】
	{
		return name.equals("杀")||name.equals("雷杀")||name.equals("火杀");
	}
	
	Player input_target(String s,ArrayList<Player> players)  //输入一个目标
	{
		System.out.print(s);
		Scanner sc=new Scanner(System.in);
		int index;
		do {index=sc.nextInt();}while(index>=players.size()||index<0);
		if (index>0) return players.get(index); else return null;
	}
	
	void drop(Player p,int type) //一张牌离开区域
	{
		Player target;
		if (get_equip().equals("武器")&&name.equals("诸葛连弩")) p.maxkill=1;
		
		if (p.name.equals("诸葛亮")&&p.cards.isEmpty()) System.out.println(">> 诸葛亮 触发【空城】");
		if (p.name.equals("陆逊")&&p.cards.isEmpty())
		{
			System.out.println("陆逊 发动【连营】，摸起一张牌");
			p.draw_card();
			f.update(p);
		}
		
		if (p.name.equals("张春华")&&p.cards.size()<p.maxhp-p.hp)
		{
			System.out.println(">> 张春华 发动【伤逝】，摸起"+String.valueOf(p.maxhp-p.hp)+"张牌");
			for (int i=0;i<p.maxhp-p.hp;i++)
				p.draw_card();
		}
		
		if (p.name.equals("孙尚香")&&equip!=null&&type>1)
		{
			System.out.print("孙尚香 发动了<枭姬>，摸起两张牌");
			p.draw_card();
			p.draw_card();
		}
		
		if (p.name.equals("邓艾")&&p.no!=f.current)
		{
			if (p.bot||p.input("是否发动【屯田】:",2)==1)
				p.test("屯田");
		}
		
		target=p.search_player("曹植");
		if (target!=null&&!target.equals(p)&&type==1)
		{
			if (sub==null&&color==2)
			{
				stat=false;
				System.out.print(">> 曹植 发动落英，获得");
				println();
				p.search_player("曹植").cards.add(this.origin);
			}
			else
			{
				for (int i=0;i<sub.size();i++)
				{
					if (sub.get(i).color==2)
					{
						sub.get(i).stat=false;
						System.out.print(">> 曹植 发动落英，获得");
						println();
						p.search_player("曹植").cards.add(sub.get(i));
					}
				}
			}
		}
		
		equip=null;
		p.fetch(all_cards,this);
	}
	
	Player input_target(String s,ArrayList<Player> players,ArrayList<Player> targets)  //输入一个目标
	{
		System.out.print(s);
		Scanner sc=new Scanner(System.in);
		int index;
		do {index=sc.nextInt();}while(index>=players.size()||index<0||targets.indexOf(players.get(index))==-1);
		if (index>0) return players.get(index); else return null;
	}
	
	boolean succ(String s)
	{
		switch(s)
		{
			case "闪电":if (color==0&&num>1&&num<10) return true;break;
			case "乐不思蜀":if (color!=1) return true;break;
			case "兵粮寸断":if (color!=2) return true;break;
			case "八卦阵":if (color%2==1) return true;break;
			case "洛神":if (color%2==0) return true;break;
			case "潜袭":if (color!=1) return true;break;
			case "刚烈":if (color!=1) return true;break;
			case "再起":if (color==1) return true;break;
			case "雷击":if (color==0) return true;break;
			case "屯田":if (color!=1) return true;break;
		}
		return false;
	}
	


}
