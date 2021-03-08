import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Card {
	int color;
	int num;
	int type; //��������
	String name; //����ʹ����
	Card origin; //������������ʹ��ʱ��ָ��ԭ����
	String equip; //װ��λ��
	boolean stat; //ʹ�ú��Ƿ�����
	Scanner sc;
	ArrayList<Player>players;
	ArrayList<Card>all_cards;
	ArrayList<Card>sub;
	Frame f;
	String skill;
	Card(int c,int n,String s) //��ʼ��
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
	
	void print() //������ƾ�������
	{
		System.out.print("("+get_name()+")");
	}
	
	String get_color() //��ÿ��ƻ�ɫ
	{
		String c;
		switch(color)
		{
			case 0:c="����";break;
			case 1:c="����";break;
			case 2:c="÷��";break;
			case 3:c="��Ƭ";break;
			default:c="";
		}
		return c;
	}
	
	String get_equip()
	{
		if (equip==null)
			return "����";
		else
			return equip;
	}
	
	String get_name() //��ÿ��ƻ�ɫ+����
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
	
	void show() //��ӡ���ƻ�ɫ+����
	{
		System.out.print(get_name());
	}
	
	void println() //��ӡ����ʱ���ƾ�������
	{
		System.out.println("("+get_name()+")");
	}
	
	String show_color() //��ÿ��ƻ�ɫ�������ж���
	{
		return "��"+get_color()+"��";
	}
	
	String showname() //��ÿ������ƣ����ڳ��ƣ�
	{
		return "��"+name+"��"; 
	}
	
	void print_origin()
	{
		if (sub==null) origin.print();
		else
		{
			System.out.print("("+sub.get(0).get_name());
			for (int i=1;i<sub.size();i++)
				System.out.print("��"+sub.get(i).get_name());
			System.out.print(")");
			
		}
	}
	//ʹ�ÿ��ƣ��麯����
	void use(Player p,Scanner sc) throws InterruptedException, Exception
	{

	} 

	Card get_card(Player target)  //������Ŀ��һ����
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
	
	Boolean is_kill()  //�жϴ����Ƿ�Ϊ��ɱ��
	{
		return name.equals("ɱ")||name.equals("��ɱ")||name.equals("��ɱ");
	}
	
	Player input_target(String s,ArrayList<Player> players)  //����һ��Ŀ��
	{
		System.out.print(s);
		Scanner sc=new Scanner(System.in);
		int index;
		do {index=sc.nextInt();}while(index>=players.size()||index<0);
		if (index>0) return players.get(index); else return null;
	}
	
	void drop(Player p,int type) //һ�����뿪����
	{
		Player target;
		if (get_equip().equals("����")&&name.equals("�������")) p.maxkill=1;
		
		if (p.name.equals("�����")&&p.cards.isEmpty()) System.out.println(">> ����� �������ճǡ�");
		if (p.name.equals("½ѷ")&&p.cards.isEmpty())
		{
			System.out.println("½ѷ ��������Ӫ��������һ����");
			p.draw_card();
			f.update(p);
		}
		
		if (p.name.equals("�Ŵ���")&&p.cards.size()<p.maxhp-p.hp)
		{
			System.out.println(">> �Ŵ��� ���������š�������"+String.valueOf(p.maxhp-p.hp)+"����");
			for (int i=0;i<p.maxhp-p.hp;i++)
				p.draw_card();
		}
		
		if (p.name.equals("������")&&equip!=null&&type>1)
		{
			System.out.print("������ ������<�ɼ�>������������");
			p.draw_card();
			p.draw_card();
		}
		
		if (p.name.equals("�˰�")&&p.no!=f.current)
		{
			if (p.bot||p.input("�Ƿ񷢶������:",2)==1)
				p.test("����");
		}
		
		target=p.search_player("��ֲ");
		if (target!=null&&!target.equals(p)&&type==1)
		{
			if (sub==null&&color==2)
			{
				stat=false;
				System.out.print(">> ��ֲ ������Ӣ�����");
				println();
				p.search_player("��ֲ").cards.add(this.origin);
			}
			else
			{
				for (int i=0;i<sub.size();i++)
				{
					if (sub.get(i).color==2)
					{
						sub.get(i).stat=false;
						System.out.print(">> ��ֲ ������Ӣ�����");
						println();
						p.search_player("��ֲ").cards.add(sub.get(i));
					}
				}
			}
		}
		
		equip=null;
		p.fetch(all_cards,this);
	}
	
	Player input_target(String s,ArrayList<Player> players,ArrayList<Player> targets)  //����һ��Ŀ��
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
			case "����":if (color==0&&num>1&&num<10) return true;break;
			case "�ֲ�˼��":if (color!=1) return true;break;
			case "�������":if (color!=2) return true;break;
			case "������":if (color%2==1) return true;break;
			case "����":if (color%2==0) return true;break;
			case "ǱϮ":if (color!=1) return true;break;
			case "����":if (color!=1) return true;break;
			case "����":if (color==1) return true;break;
			case "�׻�":if (color==0) return true;break;
			case "����":if (color!=1) return true;break;
		}
		return false;
	}
	


}
