import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import java.util.Random;

public class Player {
	int hp,maxhp,kill,no,pos,drop_count,dist_buff,shuangxiong,stype,maxkill,team,magic,alive_num,fail;
	boolean wip,skip_judge,skip_draw,skip_act,bot,drunk,sex,alive,link,use_kill,luoyi,under,limit,trigger,once,awake,harm,active;
	String name,skill,extra_name;
	Weapon weapon;
	Armor armor;
	Card atk,def,pan_le,pan_duan,pan_shan,testcard;
	Player kill_target;
	ArrayList<Card> cards;
	ArrayList<Boolean> aval;
	ArrayList<Card> index;
	ArrayList<Card> buqu;
	Scanner sc;
	Frame f;
	ArrayList<Card> all_cards;
	ArrayList<Player> players;
	ArrayList<Card> extra;
	Player(Character c,int num,int n,boolean b)
	{
		no=n;
		pos=n;
		bot=b;
		maxhp=c.maxhp;
		hp=maxhp;
		name=c.name;
		skill=c.skill;
		stype=c.stype;
		sex=c.sex;
		team=c.team;
		extra_name=null;
		weapon=null;
		armor=null;
		atk=null;
		def=null;
		pan_le=null;
		pan_duan=null;
		pan_shan=null;
		testcard=null;
		kill=1;
		dist_buff=-1;
		drunk=false;
		under=false;
		limit=true;
		aval=new ArrayList<Boolean>();
		extra=new ArrayList<Card>();
		alive=true;
		link=false;
		trigger=false;
		awake=false;
		kill_target=null;
		alive_num=n;
		wip=false;
		sc=new Scanner(System.in);
		if (name.equals("��̩")) buqu=new ArrayList<Card>();
		switch(name)
		{
			case "�˰�":extra_name="��";break;
			case "�ӻ�":extra_name="Ȩ";break;
		}
	}
	
	void init_card(ArrayList<Card> all_cards)
	{
		cards=new ArrayList<Card>();
		for (int i=0;i<4;i++)
			cards.add(all_cards.get(i));
		all_cards.removeAll(cards);
	}		
	
	Player prev(ArrayList<Player>players)
	{
		if (no==0)
			return players.get(players.size()-1);
		else
			return players.get(no-1);
	}
	Player next(ArrayList<Player>players)
	{
		if (no==players.size()-1)
			return players.get(0);
		else
			return players.get(no+1);
	}
	
	boolean empty()
	{
		  return (cards.isEmpty()&&atk==null&&def==null&&weapon==null&&armor==null);
	}
	
	void heal(int value)
	{
		if (hp<maxhp)
		{
			hp+=value;
			System.out.println(">> "+name+" �ظ���"+String.valueOf(value)+"������");
			if (name.equals("��̩"))
			{
				for (int i=0;i<value;i++)
				{
					System.out.print("��̩����������");
					buqu.get(buqu.size()-1).println();
					buqu.remove(buqu.size()-1);
				}
			}
			f.update(this);
		}
	}
	
	void death() throws Exception
	{
		boolean b=true,t,wansha=false;
		Card c;
		System.out.println(">> "+name+" �������״̬");
		if (players.get(f.current).name.equals("��ڼ")) 
		{
			System.out.println(">> ��ڼ��������ɱ��");
			wansha=true;
		}
		for (int i=f.current;i!=f.current||b;i=(i+1)%players.size())
		{
			b=false;
			if (hp<=0&&i==no&&name.equals("��̩"))
			{
				for (int j=0;j<1-buqu.size()-hp;)
				{
					t=true;
					if (bot||!bot&&input("�Ƿ񷢶�����������",2)==1)
					{
						c=all_cards.get(0);
						all_cards.remove(0);
						System.out.print(">> ��̩��������������������Ϊ");
						c.print();
						System.out.print("(");					
						for (int k=0;k<buqu.size();k++)
						{
							System.out.print(buqu.get(k).num);
							System.out.print(',');
							if (buqu.get(k).num==c.num) 
								t=false;
						}
						System.out.print(c.num);
						System.out.println(')');
						if (!t)
						{
							System.out.println(")\n>> ��̩������������ʧ��");
							if (!react("��",this)&&!react("��",this)) break;
							else				
								hp++;
						}
						else
						{
							buqu.add(c);
							j++;
						}	
						
					}					
				}
				if (buqu.size()+hp==1) return;
			}
			if (wansha&&!players.get(i).name.equals("��ڼ")&&i!=no)
					System.out.println(">> "+players.get(i).name+"�޷�����");
			else
			{
				while(hp<=0&&(!bot||bot&&(i==no))&&(players.get(i).react("��",this)||(i==no)&&players.get(i).react("��",this))) heal(1);
				if (hp>0) break; else System.out.println(">> "+players.get(i).name+" ����");
			}

		}
		if (hp<=0) 
		{
			
				if (name.equals("��ͳ")&&limit)
				{
					drop_all(all_cards);
					System.out.println(">> ��ͳ���������������ظ�������������������");
					draw_card();
					draw_card();
					draw_card();
					hp=Integer.min(3, maxhp);
					limit=false;
				}
				else
				{
					alive=false;
					System.out.println(">> "+name+" ����orz");
					f.update(this);
					if (search_player("��ا")!=null)
					{
						System.out.println(">> ��ا���������䡿�������������");
						drop_all(search_player("��ا").cards);
					}
					else
						drop_all(all_cards);
					for (int i=no+1;i<players.size();i++)
					{
						players.get(i).no--;
						f.update(players.get(i));
					}
					players.remove(no);
					if (wip)
						throw new Exception("");
				}
			
		}
	}
	
	void loss(int value) throws Exception
	{
		hp-=value;
		System.out.println(">> "+name+" ��ʧ��"+String.valueOf(value)+"������");
		if (hp<1) death();
		if (name.equals("�Ŵ���")&&cards.size()<maxhp-hp)
		{
			System.out.println(">> �Ŵ��� ���������š�������"+String.valueOf(maxhp-hp)+"����");
			for (int i=0;i<maxhp-hp;i++)
				draw_card();
		}
		f.update(this);
	}
	
	void show(boolean b)
	{
		System.out.println("�����ơ�");
		for (int i=0;i<cards.size();i++)
		{
			System.out.print("    "+String.valueOf(i)+'.');
			cards.get(i).show();
			if (b&&aval.get(i)==true) System.out.println('*'); else System.out.println();
		}
	}
	
	void show_skill()
	{
		System.out.print("�����ܡ�\n    a."+skill);
		if (stype==3&&limit||stype==1&&once||name.equals("��ֲ")&&!under) System.out.println("*"); else System.out.println();
	}

	void show_extra()
	{
		if (!extra.isEmpty())
		{
			System.out.println("��"+extra_name+"��");
			for (int i=0;i<extra.size();i++)
				System.out.println("    "+String.valueOf(i)+"."+extra.get(i).get_name());			
		}
	}
	
	void damage(int value,int type,Player source,Card source_card) throws Exception
	{
		Card c,d;
		int index;
		Player target;
		if (source.name.equals("�Ŵ���"))
		{
			System.out.println(">> �Ŵ��� ���������顿");
			loss(value);
			return;
		}
		source.harm=true;
		if (armor!=null)
			if (get_armor("����ʨ��"))
			{
				if (value>1)System.out.println(">> "+name+" ����ʨ���µֵ����˺�Ϊ1");
				value=1;
			}
			if (get_armor("�ټ�")&&type==1)
			{
				System.out.println(">> "+name+ "�������ټף��˺�+1");
				value+=1;
			}
		hp-=value;
		System.out.print(">> "+name+" �ܵ���"+String.valueOf(value)+"��");
		switch(type)
		{
			case 0:System.out.print("�˺�");break;
			case 1:System.out.print("�����˺�");break;
			case 2:System.out.print("�׵��˺�");
		}
		if (source!=null) System.out.println("����ԴΪ"+source.name); else System.out.println("�����˺���Դ");
		if (hp<1) death();
		f.update(this);
		
		if (name.equals("�Ŵ���")&&cards.size()<maxhp-hp)
		{
			System.out.println(">> �Ŵ��� ���������š�������"+String.valueOf(maxhp-hp)+"����");
			for (int i=0;i<maxhp-hp;i++)
				draw_card();
		}
		
		if (source.name.equals("������")&&source.hp<source.maxhp&&!source.awake)
		{
			source.awake=true;
			System.out.println(">> ������ ������Ǳ�ġ�����һ���������ޣ�����á����ԡ�");
			source.maxloss();
			source.skill="����";
			source.stype=1;
		}
		
		if (source.name.equals("κ��")&&source.hp<source.maxhp&&source.in_dist(this, false))
		{
			System.out.println(">> κ�ӷ�������ǡ�����Ѫ"+String.valueOf(Integer.min(maxhp, hp+value)-hp)+"��");
			hp=Integer.min(maxhp, hp+value);
		}
		
		if (name.equals("�ӻ�"))
		{
			for (int i=0;i<value;i++)
				if (bot||input("�Ƿ񷢶���Ȩ�ơ�",2)==1) 
				{
					cards.add(all_cards.get(0));
					all_cards.remove(0);
					show(false);
					if (bot)
						c=random_card();
					else
						c=hand_card("��ѡ����Ϊ��Ȩ������:",cards);
					cards.remove(c);
					extra.add(c);
					System.out.println(">> �ӻ� ����һ���ƣ�����һ��������Ϊ��Ȩ��");
				}				
		}
		
		if (name.equals("��ا"))
		{
			if (bot&&new Random().nextInt(maxhp-hp+1)==0||!bot&&input("�Ƿ񷢶�������",2)==1)
			{
				System.out.print(">> ��ا �����ˡ�����");
				if (bot)
					target=get_target(2);
				else
					target=input_target("��ѡ��Ŀ��:",2);
				System.out.println(">> "+target.name+" ���棬��"+String.valueOf(maxhp-hp)+"����");
				for (int i=0;i<maxhp-hp;i++)
					target.draw_card();
				target.under=!target.under;
			}
		}
		
		if (name.equals("����"))
		{
			for (int i=0;i<value;i++)
			{
				if (bot&&cards.size()<maxhp||!bot&&input("�Ƿ񷢶���������:",2)==1)
				{
					if (bot)
					{
						System.out.println("���� ���������������������Ʋ�������");
						for (int j=0;j<maxhp-cards.size();j++)
							draw_card();
					}
					else
					{
						do
						{target=players.get(input("��ѡ��Ŀ��:",players.size()));}while(target.cards.size()>=Integer.min(target.maxhp,5));
						System.out.println("���� ������������"+target.name+"�����Ʋ�������");
						for (int j=0;j<target.maxhp-target.cards.size();j++)
							target.draw_card();		
						f.update(target);
					}
				}
			}
		}
		
		if (name.equals("��ܲ�"))
		{
			for (int i=0;i<value;i++)
			{
				if (bot||input("�Ƿ񷢶������ġ�",2)==1)
				{
					System.out.print(">> ��ܲ� ���������ġ������佫��");	
					if (under) System.out.print("��"); else System.out.print("��");
					under=!under;
					System.out.println("�泯��");
					for (int j=(no+1)%players.size();j!=no;j=(j+1)%players.size())
					{
						if (bot)
							c=players.get(j).get_card();
						else
						{
							System.out.print("Ŀ����"+players.get(j).name+"��");
							c=input_card(players.get(j),0);
						}
						cards.add(c);
						System.out.print(">> ��ܲٻ�ȡ��"+players.get(j).name+"��");
						if (c.equip==null) System.out.println("һ������"); else System.out.println(c.equip+c.get_printname());
						c.equip=null;
						f.update(this);
						f.update(players.get(j));
					}
				}
			}
		}
		
		if (name.equals("����"))
		{
			for (int i=0;i<value;i++)
			{
				System.out.println(">> ���� �������żơ������������");
				c=all_cards.get(0);
				d=all_cards.get(1);
				all_cards.remove(0);
				all_cards.remove(0);
				if (bot)
				{
					cards.add(c);
					cards.add(d);
				}
				else
				{
					index=input("��"+c.get_printname()+"����˭��",players.size());
					if (index>=0) 
					{
						players.get(index).cards.add(c);
						System.out.println(">> ���� ��һ���ƽ�����"+players.get(index).name);
					}
					else
						cards.add(c);
					index=input("��"+d.get_printname()+"����˭��",players.size());
					if (index>=0) 
					{
						players.get(index).cards.add(d);
						System.out.println(">> ���� ��һ���ƽ�����"+players.get(index).name);
					}
					else
						cards.add(d);
				}
			}
		}
		
		if (name.equals("�ĺ"))
		{
			if (bot||input("�Ƿ񷢶������ҡ�",2)==1)
			{
				if (test("����"))
				{
					if (source.cards.size()<2)
						source.loss(1);
					else
					{
						if (!source.bot)
							show(false);
						if (source.bot&&hp>1&&new Random().nextBoolean()||input("��ѡ��:0.�������� 1.ʧȥһ������",2)==1)
							source.loss(1);
						else
						{
							//��������
							
						}
					}
				}						
			}	
		}
		
		if (name.equals("˾��ܲ"))
		{
			c=null;
			if (bot)
				c=source.get_card();
			else if (input("�Ƿ񷢶�����:",2)==1)
				c=input_card(source,0);
			if (c!=null)
			{
					c.drop(source,2);
					cards.add(c);
					f.update(source);
					f.update(this);
					System.out.println(">> ˾��ܲ �������������������"+source.name+"��"+c.get_equip()+c.get_printname());
					c.equip=null;
				
			}
		}
		
		if (name.equals("�ܲ�"))
		{
			if (source_card!=null&&(bot||!bot&&input("�Ƿ񷢶������ۡ���",2)==1))
			{
				System.out.print(">> �ܲ� ���������ۡ�������˺���");
				fetch(cards,source_card);
				source_card.println();
				source_card.set_stat();
			}
		}
		
		if (name.equals("��ֲ")&&under&&(bot||input("�Ƿ񷢶�����ʫ��:",2)==1))
		{	
			System.out.println(">> ��ֲ ��������ʫ������������");
			under=false;
			f.update(this);
		}
			
		if (link&&(type>0))
		{
			link=false;
			for (int i=(no+1)%players.size();i!=no;i=(i+1)%players.size())
				if (players.get(i).link)
				{
					players.get(i).damage(value, type, source,null);
					break;
				}
		}
	}

	Card search(String s)
	{
		Card c=null;
		for (int i=0;i<cards.size();i++)
		{
			if (s.equals("��"))
			{
				if (name.equals("�缧")&&cards.get(i).color%2==0) return new Card(cards.get(i),s,skill); 
				if (name.equals("����")&&cards.get(i).is_kill()) return new Card(cards.get(i),s,skill); 
			}
			if (s.equals("��и�ɻ�")&&name.equals("����")&&cards.get(i).color%2==0) return new Card(cards.get(i),s,skill); 
			if (s.equals("��")&&name.equals("��׿")&&cards.get(i).color==0) return new Card(cards.get(i),s,skill); 
			if (s.equals("��")&&name.equals("��٢")&&cards.get(i).color%2==1) return new Card(cards.get(i),s,skill); 
			if (cards.get(i).name.equals(s)) return cards.get(i);

		}
		return c;
	}
	
	int search_num(String s)
	{
		int sum=0;
		for (int i=0;i<cards.size();i++)
			if (cards.get(i).name.equals(s)) sum++;
		return sum;
	}
	
	int search_killnum()
	{
		int sum=0;
		for (int i=0;i<cards.size();i++)
			if (cards.get(i).name.equals("ɱ")||cards.get(i).name.equals("��ɱ")||cards.get(i).name.equals("��ɱ")) sum++;
		return sum;
	}
	
	Card search(int color,ArrayList<Card>cards)
	{
		Card c=null;
		for (int i=0;i<cards.size();i++)
			if (cards.get(i).color==color) c=cards.get(i);
		return c;
	}
	
	Card search_kill()
	{
		for (int i=0;i<cards.size();i++)
			if (is_kill(cards.get(i))) 
			{
				if ((name.equals("����")||name.equals("�����Ű�")&&active)&&(cards.get(i).color%2==1)) return new Card(cards.get(i),"ɱ","��ʥ");
				else return cards.get(i);
			}
		return null;
	}
	
	String get_name(Card c)
	{
		if (c==null) return "  "; else return c.name;
	}
	
	void show_equip()
	{
		System.out.println("����:"+get_name(weapon));
		System.out.println("����:"+get_name(armor));
		System.out.println("-1��:"+get_name(atk));
		System.out.println("+1��:"+get_name(def));
	}
	
	void show_all(boolean b)
	{
		if (b) show(false); else System.out.println("����:"+String.valueOf(cards.size()));
		show_equip();
	}

	int kill_dist()
	{
		if (weapon==null) return 1;else return weapon.dist;
	}
	
	int atk_dist()
	{
		int i=0;
		if (name.equals("��")||name.equals("���")||name.equals("�ӵ�")||name.equals("�����")&&hp>2) i=1;
		if (name.equals("�˰�")) i=extra.size();
		if (atk==null) return 1+i;else return 2+i;
	}
	
	int def_dist()
	{
		int i=0;
		if (name.equals("��ܲ�")||name.equals("�����")&&hp<=2) i=1;
		if (def==null) return i; else return i+1;
	}
	
	boolean in_dist(Player target,boolean b)
	{
		if (b) 
			if (dist_buff==-2||dist_buff==target.no) return true;
			else return Integer.min(Math.abs(target.no-no),Math.abs(target.no+no)-players.size())<=kill_dist()+atk_dist()-target.def_dist()-1;
		else 
			if (name.equals("����Ӣ")) return true;
			else return Integer.min(Math.abs(target.no-no),Math.abs(target.no+no)-players.size())<=atk_dist()-target.def_dist();
	}
	
	boolean has_target(ArrayList<Player> players,boolean b)
	{
		for (int j=(no+1)%players.size();j!=no;j=(j+1)%players.size())
			if (in_dist(players.get(j),b))
				return true;
		return false;
	}
	
	boolean has_target(Card c,boolean b)
	{
		for (int j=(no+1)%players.size();j!=no;j=(j+1)%players.size())
			if (in_dist(players.get(j),b))
			{
				if ((is_kill(c)||c.name.equals("����"))&&(players.get(j).name.equals("�����")&&players.get(j).cards.isEmpty()))
						continue;
				return true;
			}
				
		return false;
	}
	
	boolean check(ArrayList<Player> players)
	{
		Card c;
		boolean f,g=false;
		aval.removeAll(aval);
		for (int i=0;i<cards.size();i++)
		{
			f=false;
			c=cards.get(i);
			switch (c.type)
			{
				case 0:
					if (c.name.equals("��"))
					{
						if (hp<maxhp)
							f=true;
						else
							f=false;
					}
					else if (c.name.equals("��"))
						f=true;
					else if (!c.name.equals("��"))
						f=has_target(players,true)&&kill<maxkill;
					break;
					
				case 1:
					if (c.name.equals("˳��ǣ��")||c.name.equals("�������"))
						f=has_target(players,false);
					else if (c.name.equals("�赶ɱ��"))		
					{
						for (int j=(no+1)%players.size();j!=no;j=(j+1)%players.size())
							if (players.get(j).weapon!=null)
							{
								f=true;
								break;
							}
					}
					else if (c.name.equals("����")&&pan_shan!=null) 
						f=false;
					else if (!c.name.equals("��и�ɻ�"))
						f=true;
					
					break;
					
				default:f=true;
			}
			if (f) g=true;
			aval.add(f);
		}
		return g;
	}
	
	boolean react_kill()
	{
		int trans=-1;
		if (bot)
		{
			Card c=search_kill();
			if (c==null)
				return false; 
			else 
			{
				use_kill=true;
				cards.remove(c);
				System.out.print(">> "+name+" ����ˡ�ɱ��");
				c.origin.println();
				f.update(this);
				return true;
			}
					
		}
		else
		{
			sc=new Scanner(System.in);
			Card c=search_kill();
			if (c!=null)
			{		
				show(false);
				System.out.println("�����ɱ��:");
				if ((name.equals("����")||name.equals("�����Ű�")&&active)&&input("�Ƿ񷢶�����ʥ����",2)==1) {trans=0;System.out.print("�������ƣ�");}
				int index=sc.nextInt();
				if (index<0)
					return false;
				c=cards.get(index);
				if (trans==0)
					c=transform(c,"ɱ",trans);
				else if (c.is_kill())
				{
					use_kill=true;
					System.out.print(">> "+name+"����ˡ�ɱ��");
					c.origin.println();
					cards.remove(index);
					f.update(this);
					return true;
				}
				else
					return false;
			}
			return false;
		}
	}

	Card transform(Card c,String s,int trans)
	{
		switch(trans)
		{
			case 0:if (c.color%2==1) return new Card(c,s,skill);break; //��ʥ
			case 1:if (c.is_kill()) return new Card(c,s,skill);break; //����
			case 2:if (c.color%2==0) return new Card(c,s,"���");break; //���
			case 3:if (no!=f.current&&c.color%2==1) return new Card(c,s,"����");break; //����
			case 4:if (c.color%2==0) return new Card(c,s,"����");break; //����
			case 5:if (c.color==1) return new Card(c,s,skill);break; //�Ƴ�
		}
		return c;
	}
	
	boolean react(String s,Player source) throws Exception
	{
		String str=null;
		Player target;
		if (s.equals("��")&&name.equals("��ֲ")&&!under)
		{
			if (bot||input("�Ƿ񷢶�����ʫ��",2)==1)
			{
				System.out.println("��ֲ ��������ʫ�������佫�Ʒ��棬��Ϊʹ��һ�š��ơ�");
				heal(1);
			}
		}
		if (bot)
		{
			Card c=search(s);
			if (c==null) 
				return false; 
			else 
				{
					cards.remove(c.origin);
					System.out.print(">> "+name+" �����"+c.showname());
					c.origin.drop(this,0);
					c.println();
					f.update(this);
					return true;
				}
		}
		else
		{
			sc=new Scanner(System.in);
			Card c=search(s);
			int trans=-1;
			if (c!=null)
			{
				show(false);
				System.out.println("�����"+c.name+"��:");
				if (s.equals("��"))
				{
					if ((name.equals("SP����")||name.equals("����"))&&input("�Ƿ񷢶�����������",2)==1) trans=1;
					if (name.equals("�缧")&&input("�Ƿ񷢶����������",2)==1) trans=2;
				}
				if (s.equals("��")&&name.equals("��٢")&&input("�Ƿ񷢶������ȡ���",2)==1) trans=3;
				if (s.equals("��и�ɻ�")&&name.equals("����")&&input("�Ƿ񷢶������ơ���",2)==1) trans=4;
				if (s.equals("��")&&name.equals("��׿")&&input("�Ƿ񷢶����Ƴء���",2)==1) trans=5;
				if (trans>0) System.out.print("�������ƣ�");
				int index=sc.nextInt();
				if (index<0)
					return false;
				c=cards.get(index);
				if (trans>0)				
					c=transform(c,s,trans);
				if (c.name.equals(s))
				{
					System.out.print(">> "+name);
					switch(trans)
					{
						case 1:str="����";break;
						case 2:str="���";break;
						case 3:str="����";break;
						case 4:str="����";break;
						case 5:str="�Ƴ�";break;
					}
					if (str!=null)  str="������"+str+"��,"; else str="";
					System.out.print(str+" �����"+c.showname());
					if (c.skill!=null)  System.out.print("("+c.skill+")");
					c.origin.println();
					cards.remove(index);
					c.drop(this,0);			
					f.update(this);
					
					if (name.equals("�Ž�")&&s.equals("��")&&(bot||input("�Ƿ񷢶����׻���:",2)==1))
					{
						if (bot)
						{
							
						}
						else
						{
							target=input_target("��ѡ��Ŀ��:",2);
							if (test("�׻�")) target.damage(2, 2, this,null);
						}
					}
					if (name.equals("SP����")&&trans==1)
					{
						if (!source.cards.isEmpty()&&(bot||input("�Ƿ񷢶�������:",2)==1))
						{			
							System.out.println(">> SP���� ���������󡿣����"+source.name+"һ������");
							c=source.cards.get(new Random().nextInt(source.cards.size()));
							source.cards.remove(c);
							c.drop(source,2);
							cards.add(c);
							f.update(this);
							f.update(source);
						}
					}
					return true;
				}
				else
					return false;
			}
			return false;
		}
	}
	
	void begin() throws Exception
	{
		System.out.println(">> "+name+" �غϿ�ʼ�׶�");
		f.current=no;
		wip=true;
		skip_judge=false;
		skip_draw=false;
		skip_act=false;
		use_kill=false;
		harm=false;
		active=false;
		kill_target=null;
		once=true;
		magic=0;
		int index,type;
		Player target;
		Card c;
		if (name.equals("�ĺ�Ԩ"))
		{
			if (bot&&(pan_shan!=null||pan_le!=null||pan_duan!=null)||!bot&&input("�Ƿ񷢶�������1����",2)==1)
			{
				System.out.println(">> �ĺ�Ԩ���������١��������ж��׶������ƽ׶�");
				skip_judge=true;
				skip_draw=true;
				dist_buff=-2;
				new Basic("ɱ",f,players,all_cards).use(this,sc);
				dist_buff=-1;
			}
		}
				
		if (name.equals("�˰�")&&extra.size()>=1&&!awake)
		{
			awake=true;
			System.out.println(">> �˰� �������Ѽ������ա�����һ���������ޣ���á���Ϯ��");
			skill="��Ϯ";
			stype=2;
			maxloss();
		}
		
		if (name.equals("�ӻ�")&&extra.size()>=1&&!awake)
		{		
			System.out.println(">> �ӻ� �������Ѽ���������");
			awake=true;
			if (bot&&hp>=2||!bot&&input("��ѡ��:0.�ظ�һ������ 1.��������",2)==1)
			{
				draw_card();
				draw_card();
				System.out.print(">> �ӻ� ��������");
			}
			else
			{
				heal(1);
				System.out.println(">> �ӻ� �ظ�һ������");
			}
			System.out.println("����һ���������ޣ���á����졿");	
			skill="����";
			stype=1;
			maxloss();
			f.update(this);
		}
		
		if (name.equals("��ά")&&cards.isEmpty()&&!awake)
		{
			System.out.println(">> ��ά �������Ѽ���־�̡�");
			awake=true;
			if (bot&&hp>=2||!bot&&input("��ѡ��:0.�ظ�һ������ 1.��������",2)==1)
			{
				draw_card();
				draw_card();
				System.out.print(">> ��ά ��������");
			}
			else
			{
				heal(1);
				System.out.println(">> ��ά �ظ�һ������");
			}
			System.out.println("����һ���������ޣ���á����ǡ�");	
			maxloss();
			hp=Integer.min(hp,maxhp);		
			f.update(this);
		}
		
		if (name.equals("���")&&hp==1&&!awake)
		{
			System.out.println(">> ��� �������Ѽ������ˡ�����һ���������ޣ���á�Ӣ�꡿��Ӣ�ˡ�");
			maxloss();			
			awake=true;
			f.update(this);
		}
		
		if ((name.equals("���")||name.equals("���")&&awake)&&hp<maxhp)
		{
			if (bot)
			{
				if(hp+1<maxhp)
				{
			
				System.out.println("");
				}
			}
			else
			{
				if (input("�Ƿ񷢶�Ӣ�ꣿ",2)==1)
				{
					index=input_other("��ѡ��Ŀ�꣺",players.size());
					target=players.get(index);
					type=input("0.��"+String.valueOf(maxhp-hp)+"��"+String.valueOf(1)+"\n1.��"+String.valueOf(1)+"��"+String.valueOf(maxhp-hp),2);
					if (type==0) type=maxhp-hp;
					System.out.println(">> "+name+"�����ˡ�Ӣ�꡿����"+target.name+"��"+String.valueOf(type)+"������"+String.valueOf(maxhp-hp+1-type));
					for (int i=0;i<type;i++)
						draw_card();
					for (int i=0;i<maxhp-hp+1-type;i++)
					{
						show(false);
						c=input_card(this,0);
						c.drop(this,1);
					}
				}
					
			}
		}
		
		if (name.equals("�����")||name.equals("��ά")&&awake)
		{
			if (bot||input("�Ƿ񷢶������ǡ�",2)==1)
			{
				System.out.println(">> "+name+" �����ˡ����ǡ�");
				ArrayList<Card>star=new ArrayList<Card>();
				ArrayList<Card>list=new ArrayList<Card>();
				for (int i=0;i<Integer.min(5,players.size());i++)
				{
					star.add(all_cards.get(0));
					all_cards.remove(0);
				}

				if (bot)
				{
					
				}
				else
				{
					System.out.println("�������ơ�");
					for (int i=0;i<star.size();i++)
						System.out.println(String.valueOf(i)+"."+star.get(i).get_printname());
					System.out.println("��ѡ�������ƶѶ�����:");
					list=input_cards(star);
					for (int i=0;i<list.size();i++)
						all_cards.add(0,list.get(i));
					star.removeAll(list);
					System.out.println("��ѡ�������ƶѵ׵���:");
					list=input_cards(star.size(),star);
					for (Card d:star)
						all_cards.add(d);
					System.out.println(">> "+name+"��"+String.valueOf(players.size()-list.size())+"���������ƶѶ�");
					System.out.println(">> "+name+"��"+String.valueOf(list.size())+"���������ƶѵ�");
				}
			}
		}
		
		if (name.equals("�缧"))
		{
			if (bot||input("�Ƿ񷢶������񡿣�\n",2)==1)
				while (test("����"))
					if (bot||input("�Ƿ񷢶������񡿣�\n",2)==0) break;
		}
		Thread.currentThread().sleep(300);
	}
	
	void judge() throws Exception
	{
		Player p;
		Card c;
		if(name.equals("���A"))
		{
			if (bot||input("�Ƿ񷢶����ɱ䡿�������ж��׶�:",2)==1)
			{
				show(false);
				c=hand_card("��ѡ�����õ���:",cards);
				cards.remove(c);
				c.drop(this, 1);
				System.out.println(">> ���A�������ɱ䡿������"+c.get_printname()+"�����ж��׶�");
				return;
			}
		}
		
		System.out.println(">> "+name+" �ж��׶�");
		if (pan_shan!=null)
		{
			if (wuxie(name,pan_shan.showname(),true))
			{
				System.out.println(">> "+name+" ���������ж�");
				next(players).next(players).pan_shan=pan_shan;
				f.update(next(players).next(players));
			}
			else
			{	
				if (test("����")) 
					
					damage(3,2,null,pan_shan); 
				else
				{
					if (next(players).pan_shan==null)
					{
						next(players).pan_shan=pan_shan;
						f.update(next(players));
					}
					else
					{
						next(players).next(players).pan_shan=pan_shan;
						f.update(next(players).next(players));
					}
				}
				pan_shan=null;
			}
			
			
			f.update(this);
		}
		
		if (pan_duan!=null)
		{
			if (wuxie(name,pan_duan.showname(),true))
			{
				System.out.println(">> "+name+" �����ˡ�������ϡ����ж�����������");
			}
			else
			{
				if (test("�������"))
					skip_draw=true;
			}
			all_cards.add(pan_duan);
			pan_duan=null;
			f.update(this);
		}
		
		if (pan_le!=null)
		{
			if (wuxie(name, pan_le.showname(),true))
			{
				System.out.println(">> "+name+" �����ˡ��ֲ�˼�񡿵��ж������Գ���");
			}
			else
			{
				if (test("�ֲ�˼��")) 
					skip_act=true;  
			}
			all_cards.add(pan_le);
			pan_le=null;
			
			f.update(this);
		}	
		Thread.currentThread().sleep(300);	

	}
	
	void draw() throws InterruptedException 
	{
		if (name.equals("���A"))
		{
			if(bot||input("�Ƿ񷢶����ɱ䡿���������ƽ׶�:",2)==1)
			{
				int index,i;
				Player target;
				Card c;
				show(false);
				c=hand_card("��ѡ�����õ���:",cards);
				cards.remove(c);
				c.drop(this, 1);
				System.out.println(">> ���A�������ɱ䡿������"+c.get_printname()+"���������ƽ׶�");
				System.out.print("��ѡ��һ��Ŀ��:");
				do {index=sc.nextInt();}while(index<1||index>=players.size()||players.get(index).cards.isEmpty());
				target=players.get(index);
				c=target.cards.get(new Random().nextInt(target.cards.size()));
				cards.add(c);
				target.cards.remove(c);
				System.out.println(">> ���� ����� "+target.name+"һ����"+c.get_printname());
				f.update(target);
				System.out.print("��ѡ��һ��Ŀ����");
				do {i=sc.nextInt();}while(i==0||i<-1||i>=players.size()||i==index||players.get(i).cards.isEmpty());
				if (i!=-1)
				{	
					target=players.get(i);
					c=target.cards.get(new Random().nextInt(target.cards.size()));
					cards.add(c);
					target.cards.remove(c);
					System.out.println(">> ���� ����� "+target.name+"һ����"+c.get_printname());
					f.update(target);
				}
				f.update(this);
				return;
			}
			
		}
		
		System.out.println(">> "+name+" ���ƽ׶�");
		if (name.equals("�ϻ�")&&hp<maxhp)
		{
			if (bot&&hp<maxhp-1||!bot&&input("�Ƿ񷢶������𡿣�",2)==1)
			{
				System.out.println(">> �ϻ񷢶�������");
				for (int i=0;i<maxhp-hp;i++)
					if (test("����")) heal(1);
				return;
			}
		}
		
		if (name.equals("Ԭ��"))
		{

				boolean []b=new boolean[4];
				for (Player p:players)
					if (!b[p.team]) b[p.team]=true;
				for (boolean t:b)
					if (t)
					{
						magic++;
						draw_card();
					}
				System.out.println(">> Ԭ��������ӹ����������"+String.valueOf(magic)+"����");				
		}
				
		if (name.equals("����"))
		{
			if (!bot&&input("�Ƿ񷢶������¡���",2)==1||search_kill()!=null&&new Random().nextInt(cards.size())>1)
			{
				cards.add(all_cards.get(0));
				all_cards.remove(0);
				active=true;
				System.out.println(">> ���� ���������¡�������һ����");
				f.update(this);
				return;
			}
		}
		
		if (name.equals("�����Ű�"))
		{
			if (bot||input("�Ƿ񷢶������꡿��",2)==1)
			{
				draw_card();
				draw_card();
				Card c=cards.get(cards.size()-1),d=cards.get(cards.size()-2);
				System.out.println(">> �����Ű� ���������꡿������"+c.get_printname()+","+d.get_printname());
				if (Math.abs((c.color-d.color)%2)==1) {
					System.out.println(">> �����Ű� ��á���ʥ����������ֱ���غϽ���");
					active=true;
					skill="��ʥ";
					stype=0;
				}
				f.update(this);
				return;
			}
		}
		
		if (name.equals("�����ĳ�"))
		{
			testcard=null;
			if (bot&&new Random().nextInt(cards.size())>=2||input("�Ƿ񷢶���˫�ۡ���",2)==1)
			{
				test("˫��");
				return;
			}
		}
		
		
		
		if (name.equals("����"))
		{
			int index,i;
			Card c;
			Player target;
			String s;
			
			if (bot)
			{
				ArrayList<Player>ps=get_players(0);
				if (ps.size()>=2&&new Random().nextInt(2)==1)
				{
					System.out.println(">> ���� �����ˡ�ͻϮ��");
					target=random_player(ps);
					c=target.random_card();
					target.cards.remove(c);
					cards.add(c);
					System.out.println(">> ���� ����� "+target.name+" һ����");
					target=random_player(ps);
					c=target.random_card();
					target.cards.remove(c);
					cards.add(c);
					System.out.println(">> ���� ����� "+target.name+" һ����");
				}
			
			}
			else
			{
				System.out.print("�Ƿ񷢶���ͻϮ����");
				if (sc.nextInt()==1)
				{
					System.out.println(">> ���� �����ˡ�ͻϮ��");
					System.out.print("��ѡ��һ��Ŀ��:");
					do {index=sc.nextInt();}while(index<1||index>=players.size()||players.get(index).cards.isEmpty());
					target=players.get(index);
					c=target.cards.get(new Random().nextInt(target.cards.size()));
					cards.add(c);
					target.cards.remove(c);
					System.out.println(">> ���� ����� "+target.name+" һ����");
					f.update(target);
					System.out.print("��ѡ��һ��Ŀ����");
					do {i=sc.nextInt();}while(i==0||i<-1||i>=players.size()||i==index||players.get(i).cards.isEmpty());
					if (i!=-1)
					{	
						target=players.get(i);
						c=target.cards.get(new Random().nextInt(target.cards.size()));
						cards.add(c);
						target.cards.remove(c);
						System.out.println(">> ���� ����� "+target.name+" һ����");
						f.update(target);
					}
					f.update(this);
					return;
				}
			}
		}
		draw_card();
		draw_card();
		if (name.equals("���")||name.equals("���")&&awake)
		{
			System.out.println(">> ��� ������Ӣ�ˡ�������һ����");
			draw_card();
		}
		if (name.equals("³��"))
		{
			Player target;
			ArrayList<Integer>min_list=new ArrayList<Integer>();
			int min=100,index;
			if ((bot&&cards.size()<=3)||(!bot&&input("�Ƿ񷢶�����ʩ����",2)==1))
			{
				System.out.println(">> ³�� ��������ʩ��������������");
				draw_card();
				draw_card();
				f.update(this);
				if (cards.size()>5)
				{
					for (int i=0;i<players.size();i++)
					{
						if (i!=no)
							min=Integer.min(players.get(i).cards.size(),min);
					}
					for (int i=0;i<players.size();i++)
					{
						if (i!=no&&players.get(i).cards.size()==min)
							min_list.add(i);
					}
					if (bot)
					{
						System.out.println("Ϊʲô�ҷ����ˣ�");
					}
					else
					{
						show(false);
						index=input("��ѡ��һ���������ٵ�Ŀ�꣺",min_list);
						target=players.get(index);
						System.out.print("��ѡ��"+String.valueOf(cards.size()/2)+"�����ƽ���"+target.name+"��");
						min_list.removeAll(min_list);
						for (int i=0;i<cards.size()/2;i++)
						{
							index=input_posi("",cards.size());
							target.cards.add(cards.get(index));
							if (min_list.contains(index)) i--;
						}
						System.out.print(">> ³�� ��"+String.valueOf(cards.size()/2)+"�����ƽ���"+target.name);
						cards.removeAll(target.cards);
						f.update(target);
					}
					
				}
			}
		}
		f.update(this);
		Thread.currentThread().sleep(300);
	}

	void change()
	{
		if (trigger)
		{
			for (int i=0;i<cards.size();i++)
			{
				switch(skill)
				{
					case "��ʥ":if (cards.get(i).color%2==1) cards.set(i, new Basic(cards.get(i),"ɱ",skill));break;
					case "����":if (cards.get(i).color==2) cards.set(i, new Special(cards.get(i),"��������",skill));break;
					case "��Ϯ":if (cards.get(i).color%2==0) cards.set(i, new Special(cards.get(i),"���Ӳ���",skill));break;
					case "��ɫ":if (cards.get(i).color==3) cards.set(i, new Special(cards.get(i),"�ֲ�˼��",skill));break;
					case "����":if (cards.get(i).color%2==0) cards.set(i, new Special(cards.get(i),"�������",skill));break;
					case "���":if (cards.get(i).color%2==1) cards.set(i, new Special(cards.get(i),"��",skill));break;
					case "����":if (cards.get(i).name.equals("��")) cards.set(i, new Basic(cards.get(i),"ɱ",skill));break;
					case "�Ƴ�":if (cards.get(i).color==0) cards.set(i, new Basic(cards.get(i),"��",skill));break;
					case "˫��":if ((cards.get(i).color-testcard.color)%2==1) cards.set(i, new Special(cards.get(i),"����",skill));break;	
				}
			}	
		}
		else 
		{
			for (int i=0;i<cards.size();i++)
				cards.set(i,cards.get(i).origin);
		}
	}
	
	void act() throws Exception 
	{
		Card c;
		Player target,p;
		fail=0;
		kill=0;
		if (name.equals("�ŷ�")||name.equals("�����Ű�")&&active||get_weapon("�������")) maxkill=100; else maxkill=1;
		if (name.equals("�ĺ�Ԩ"))
		{
			if (has_equip()&&(bot&&new Random().nextBoolean()||!bot&&has_equip()&&input("�Ƿ񷢶������١����������ƽ׶Σ�",2)==1))
			{
				System.out.println(">> �ĺ�Ԩ �����ˡ����١����������ƽ׶�");
				if (bot)
					c=drop_equip();
				else
					c=input_equip(this);		
				System.out.println(">> �ĺ�Ԩ ������"+c.get_printname());
				c.drop(this,1);
				dist_buff=-2;
				new Basic("ɱ",f,players,all_cards).use(this,sc);

				dist_buff=-1;
				return;
			}
		}
		if (name.equals("���A"))
		{
			if (bot)
			{
				
			}
			else
			{
				if (input("�Ƿ񷢶����ɱ䡿���������ƽ׶�:",2)==1)
				{
					c=hand_card("��ѡ�����õ���:",cards);
					cards.remove(c);
					c.drop(this, 1);
					System.out.println(">>���A �������ɱ䡿������"+c.get_printname()+"���������ƽ׶�");
					target=input_target("��ѡ���ƶ��Ƶ�Ŀ��:",6);
					c=input_card(target,2);
					do
					{
						p=target.input_target("��ѡ��"+c.get_printname()+"�Ƹ���Ŀ��",2);
						switch(c.type)
						{
							case 1:if (c.name.equals("�������")&&p.pan_duan!=null) continue;
									if (c.name.equals("�ֲ�˼��")&&p.pan_le!=null) continue;
									if (c.name.equals("����")&&p.pan_shan!=null) continue;
									break;
							case 2:if (p.weapon!=null) continue; else p.weapon=(Weapon)c;
							case 3:if (p.armor!=null) continue; else p.armor=(Armor)c;
							case 4:if (p.def!=null) continue; else p.def=c;
							case 5:if (p.atk!=null) continue; else p.atk=c;
						}
					}while(false);
					System.out.println(">>���A ��"+target.name+"��"+c.equip+c.get_printname()+"�Ƹ���"+p.extra_name);
					return;
				}
			}
		}
		System.out.println(">> "+name+" ���ƽ׶�");
		int cur=0;
		boolean b;
		while(true)
		{
			b=check(players);
			if (!b&&bot) break;
			if (!bot)
			{
				show(true);
				if (skill!=null) show_skill();
				show_extra();
				do
				{
					cur=input("��ѡ��ʹ�õ����ƻ��ܣ�",cards.size());
				}while(cur>0&&!aval.get(cur));
			}
			else
			{
				if ((once&&fail<2&&stype>0)&&new Random().nextBoolean())
				{
					cur=-2;
				}
				else
					do
					{
						cur=(new Random()).nextInt(aval.size());
					}while(!aval.get(cur));
			}
			if  (cur==-2)
			{
				switch (stype)
				{
					case 0:
						if (trigger)
						{
							trigger=false;
							change();
						}
						else
						{
							trigger=true;
							System.out.println(">> "+name+" �����ˡ�"+skill+"��");
							change();
						}
						break;
					
					default:
					try {
						use_skill();
					} catch (Exception e) {

					}
				}

			}
			else if(cur!=-1)
			{
				c=cards.get(cur);
				cards.remove(c);	
				c.use(this, sc);
			}
			else
				break;
		}
		if (trigger)
		{
			trigger=false;
			change();
		}

		Thread.currentThread().sleep(300);

	}
	
	void fetch(ArrayList<Card>list,Card c)
	{
		c.origin.stat=true;
		if (c.sub==null)
			if (c.stat)list.add(c.origin);
		else
		{
			for (int i=0;i<c.sub.size();i++)
			{
				if (c.sub.get(i).stat) list.add(c.sub.get(i));
				c.sub.get(i).stat=true;
			}
		}
	}
	
	void drop() throws InterruptedException{
		Card c;
		int limit=hp;
		
		if (name.equals("����")&&!use_kill)
		{
			System.out.println(">> ���� �������˼������������ƽ׶�");
			return;
		}
		
		if(name.equals("���A"))
		{
			if (bot||input("�Ƿ񷢶����ɱ䡿4",2)==1)
			{
				c=hand_card("��ѡ�����õ���:",cards);
				cards.remove(c);
				c.drop(this, 1);
				System.out.println(">> ���A�������ɱ䡿������"+c.get_printname()+"���������ƽ׶�");
				return;
			}
		}
		
		System.out.println(">> "+name+" ���ƽ׶�");
		
		if (name.equals("�ӻ�")) limit+=extra.size();
		if (cards.size()>limit)
		{
			limit=cards.size()-limit;
			drop_cards(limit);
		}
		else
			limit=0;
		if (name.equals("Ԭ��")&&limit<magic)
		{
			System.out.println(">> Ԭ�� ������ӹ��������Ҫ������"+String.valueOf(magic-limit)+"����");
			for (int i=0;i<magic-limit;i++)
				c=input_card(this,2);
		}
		Thread.currentThread().sleep(300);

	}
	
	void end(ArrayList<Card>all_cards) throws Exception {
		int index;
		boolean b;
		System.out.println(">> "+name+" �غϽ����׶�");
		
		if (name.equals("��׿"))
		{
			for (Player p:players)
				if (p.hp<hp)
				{
					System.out.println(">> ��׿ ������������");
					if (bot&&hp<maxhp||!bot&&input("��ѡ��:0.��һ������ 1.��һ����������",2)==1)
					{
						System.out.println(">> ��׿ ��һ����������");
						maxloss();
					}
					else
					{
						System.out.println(">> ��׿ ��һ������");
						loss(1);
					}

				}
		}
		if (name.equals("�����Ű�")&&active)
		{
			System.out.println(">> �����Ű� ʧȥ����ʥ����������");
			active=false;
			skill=null;
		}
		
		if (name.equals("�����")&&drop_count>1)
		{
			if (bot)
				if (hp<2) index=new Random(2).nextInt(); else index=new Random(3).nextInt();
			else
				index=input_posi("�Ƿ񷢶�������\n0.������\n1.���н�ɫ�ظ�һ������\n2.���н�ɫʧȥһ������\n",3);
			if (index!=0) 
			{
				System.out.print(">> ����褷����������������н�ɫ");
				b=true;
				if (index==1) 
				{
					System.out.println("�ظ�һ������");
					for (int i=no;i!=no||b;i=(i+1)%players.size())
					{
						b=false;
						players.get(i).heal(1);
					}
				}
				else
				{
					System.out.println("ʧȥһ������");
					for (int i=no;i!=no||b;i=(i+1)%players.size())
					{
						b=false;
						players.get(i).loss(1);
					}
				}
					
			}	
		}
		if (name.equals("����")) 
		{
			System.out.println(">> ���� ���������¡�");
			cards.add(all_cards.get(0));
			all_cards.remove(0);
			f.update(this);
		}
		if (name.equals("����"))
		{
			if (!under&&(bot&&new Random().nextInt(cards.size()+1)==0)||(!bot&&input("�Ƿ񷢶������ء�",2)==1))
			{
				System.out.println(">> ���� ���������ء����������Ʋ�����");
				draw_card();
				draw_card();
				draw_card();
				under=true;
				f.update(this);
			}
		}
		if (harm)
		{
			Player p=search_player("������");
			if (p!=null&&!p.equals(this))
			{
				if (p.bot)
				{
					
				}
				else if (p.search_kill()!=null) 
				{
					p.show(false);
					Card c=p.input_card("�Ƿ񷢶����ﺦ������"+name+"��ɱ");
					if (c.is_kill())
					{
						p.kill_target=this;
						c.use(p, sc);
						p.cards.remove(c);
						c.drop(p,0);
					}
				}
			}
		}
		wip=false;
		Thread.currentThread().sleep(300);
	}
	
	void maxloss() throws Exception
	{
		maxhp--;
		hp=Integer.min(hp, maxhp);
		if (maxhp==0)
			death();
	}
	
	void turn() throws Exception
	{
		if (under) 
		{
			System.out.println(">> "+name+" �������棬�������غ�");
			under=false;
			return;
		}
		begin();
		
		try {
			if(!skip_judge)judge();
			if(!skip_draw)draw();
			if (!skip_act)act();
			drunk=false;
			drop();
			f.update(this);
			end(all_cards);
		} catch (Exception e) {
			alive_num--;
			return;
		}

	}
	
	boolean has_equip()
	{
		for (int i=0;i<cards.size();i++)
			if (cards.get(i).type>1)
				return true;
		return atk!=null||def!=null||weapon!=null||armor!=null;
	}

	Card drop_equip()
	{
		Card c=null;
		for (int i=0;i<cards.size();i++)
			if (cards.get(i).type>1)
			{
				c=cards.get(i);
				cards.get(i).drop(this,1);
				cards.remove(i);
				return c;
			}
		if (atk!=null) {c=atk;atk=null;return c;}
		if (def!=null) {c=def;def=null;return c;}
		if (weapon!=null) {c=weapon;weapon=null;return c;}
		if (armor!=null) {c=armor;atk=armor;return c;}
		return c;
	}

	void drop_all(ArrayList<Card>all_cards)
	{
		all_cards.addAll(cards);
		cards.removeAll(cards);
		if (weapon!=null) {all_cards.add(weapon);weapon=null;}
		if (armor!=null) {all_cards.add(armor);armor=null;}
		if (def!=null) {all_cards.add(def);def=null;}
		if (atk!=null) {all_cards.add(atk);atk=null;}
	}

	void drop_cards(int count)
	{
		index=new ArrayList<Card>();
		if (bot)
		{
			for (int i=0;i<cards.size()-hp;i++)
				index.add(cards.get(i));
		}
		else
		{
			System.out.println("��������"+String.valueOf(count)+"������");
			show(false);
			for (int i=0;i<cards.size()-hp;i++)
					index.add(cards.get(input_posi("",cards.size())));
		}
		System.out.print(">> "+name+" ������");
		for (int i=0;i<index.size();i++)
		{
			index.get(i).print();
			if (i<index.size()-1)System.out.print(',');
		}
		System.out.println();
		for (int i=0;i<index.size();i++)
			index.get(i).drop(this,1);
		cards.removeAll(index);
		drop_count=index.size();
	}

	Player get_target(int type)
	{
		ArrayList<Integer>ids=new ArrayList<Integer>();
		for (int i=(no+1)%players.size();i!=no;i=(i+1)%players.size())
			switch (type)
			{
				case 0:if (in_dist(players.get(i), false)) ids.add(i);break;  //˳
				case 1:if (in_dist(players.get(i), true)) ids.add(i);break;  //ɱ
				case 2:ids.add(i);break; //�κ���
				case 3:if (!players.get(i).cards.isEmpty()) ids.add(i);break; //������
				case 4:if (i!=no) ids.add(i);break;
				case 5:if (!players.get(i).name.equals("��ڼ")) ids.add(i);break;
				case 6:if (!players.get(i).name.equals("��ڼ")&&i!=no) ids.add(i);break;
			}
		
		Player p=players.get(ids.get(new Random().nextInt(ids.size())));
		System.out.println("��Ŀ����"+p.name);
		return p;
	}
	
	boolean wuxie(String t_name,String s,boolean t) throws Exception
	{
		boolean b=true;
		for (int i=no;i!=no||b;i=(i+1)%players.size())
		{
			if ((players.get(i).bot&&!(b^t)||!players.get(i).bot))
			{
				if (!players.get(i).bot&&players.get(i).search("��и�ɻ�")!=null)
					System.out.println(">> "+t_name+"��"+s+" ����и�ɻ���");
				if(players.get(i).react("��и�ɻ�",this))
				{
					if (players.get(i).name.equals("����Ӣ"))		
					{
						System.out.println("����Ӣ ���������ǡ�������һ����");
						players.get(i).cards.add(all_cards.get(0));
						all_cards.remove(0);
					}
					return !wuxie(t_name,"��и�ɻ�",!t);
				}
			}
			b=false;
		}
		return false;
	}
	
	

	Player input_target(String s,int type)
	{
		Player target=null;
		Scanner sc=new Scanner(System.in);
		System.out.print(s);
		boolean b=true;
		int index;
		do {index=sc.nextInt();
			if (index<players.size()) target=players.get(index);
			switch (type)
			{
				case 0:b= (in_dist(target, false)) ;break;
				case 1:b= (in_dist(target, true)) ;break;
				case 2:b= (index!=no);break;
				case 3:b= !target.cards.isEmpty();break;
				case 4:b= target.sex;break;
				case 5:b= !target.sex;break;
				case 6:b= true;break;
				case 7:b= target.hp>hp&&!target.cards.isEmpty();break;
				case 8:b= target.hp<target.maxhp;
				case 9:b= !target.name.equals("��ڼ");break;
				case 10:b= !target.name.equals("��ڼ")&&!target.equals(this);
			}
		}while(index>=players.size()||index<0||!b);
		return players.get(index);
	}
	
	int input(String s,int n)
	{
		int index;
		String str;
		System.out.print(s);
		do
		{
			str=sc.next();
			if (str.charAt(0)>='a') index=95-Integer.valueOf(str.charAt(0)); else index=Integer.valueOf(str);
		}while(index>=n);
		return index;
	}
	
	int input(String s,ArrayList<Integer>ids)
	{
		int index;
		System.out.print(s);
		do
		{index=sc.nextInt();}while(!ids.contains(index));
		return index;
	}
	
	int input_other(String s,int n)
	{
		int index;
		Scanner sc=new Scanner(System.in);
		System.out.print(s);
		do
		{index=sc.nextInt();}while(index<0||index==no||index>=n);
		return index;
	}
	
	int input_posi(String s,int n)
	{
		int index;
		sc=new Scanner(System.in);
		System.out.print(s);
		do
		{index=sc.nextInt();}while(index<0||index>=n);
		return index;
	}
	

	Card input_card(String s)
	{
		int x=input(s,cards.size());
		if (x<0) return null;
		else
			return cards.get(x);
	}
	
	Card input_colored(String s,int color)
	{
		int index;
		System.out.print(s);
		do{index=sc.nextInt();}while(index>0&&index<cards.size()&&cards.get(index).color!=color);
		if (index<0) return null; else return cards.get(index);
	}
	
	Card input_card()
	{
		return cards.get(input_posi("��ѡ�����õ����ƣ�",cards.size()));
	}
	
	Card input_card(Player target,int type)
	{
		boolean b;
		int index;
		int r;
		Card c=null;
		do
		{
			b=true;
			if (target.no==no) show(false);
			index=input("��ѡ��Ŀ����:0.���� 1.���� 2.���� 3.������ 4.������ 5.�� 6.�� 7.��",8);
			switch(index)
			{
				case -1:
					return null;
				case 0:
					if (!target.cards.isEmpty()) 
					{
						if (target.no==no)
						{
							r=input("��ѡ������:",cards.size());
							c=cards.get(r);
						}
						else
						{
							r=new Random().nextInt(target.cards.size());
							c=target.cards.get(r);
						}
					}
					break;	
				case 1:
					if (target.weapon!=null)
						c=target.weapon;
					break;
				case 2:
					if (target.armor!=null)
						c=target.armor;
					break;
				case 3:
					if (target.def!=null)
						c=target.def;
					break;
				case 4:
					if (target.atk!=null)
						c=target.atk;
					break;
				case 5:
					if (target.pan_duan!=null&&type==0)
						c=target.pan_duan;
					break;
				case 6:
					if (target.pan_le!=null&&type==0)
						c=target.pan_le;
					break;
				case 7:
					if (target.pan_shan!=null&&type==0)
						c=target.pan_shan;
					break;
			}
			
			switch (type)
			{
				case 0:b=false;break;
				case 1:if (c.color%2==0) b=false;break;
				case 2:b=false;
			}
			
			if (!b)
			{
				switch (index)
				{
					case 0:target.cards.remove(c);break;
					case 1:target.weapon=null;break;
					case 2:target.armor=null;break;
					case 3:target.def=null;break;
					case 4:target.atk=null;break;
					case 5:target.pan_duan=null;break;
					case 6:target.pan_le=null;break;
					case 7:target.pan_shan=null;
				}
				c.drop(target, type);
			}
		}while (b);
		return c;
	}

	Card input_equip(Player target)
	{
		int index;
		int r;
		Card c;
		do
		{
			index=input("��ѡ��Ŀ����:0.���� 1.���� 2.���� 3.������ 4.������",5);
			switch(index)
			{
				case 0:
					if (!target.cards.isEmpty()) 
					{
						if (target.no==no)
						{
							r=input("��ѡ������:",cards.size());
							c=cards.get(r);
							if (c.type>1)
							{
								cards.remove(r);
								return c;
							}
						}
						else
						{
							r=new Random().nextInt(target.cards.size());
							c=target.cards.get(r);
							target.cards.remove(r);
							return c;
						}
					}
					break;	
				case 1:
					if (target.weapon!=null)
					{
						c=target.weapon;
						target.weapon=null;
						return c;
					}
					break;
				case 2:
					if (target.armor!=null)
					{
						c=target.armor;
						target.armor=null;
						return c;
					}
					break;
				case 3:
					if (target.def!=null)
					{
						c=target.def;
						target.def=null;
						return c;
					}
					break;
				case 4:
					if (target.atk!=null)
					{
						c=target.atk;
						target.def=null;
						return c;
					}
					break;
			}
		}while (true);
	}
	
	Card get_card()
	{
		Card c;
		ArrayList<Integer>index=new ArrayList<Integer>();
		if (! cards.isEmpty())
			index.add(0);
		if ( weapon!=null)
			index.add(1);
		if ( armor!=null)
			index.add(2);
		if ( def!=null)
			index.add(3);
		if ( atk!=null)		
			index.add(4);
		switch(index.get(new Random().nextInt(index.size())))
		{
			case 0:
					return  cards.get(new Random().nextInt( cards.size()));
			case 1:
					c= weapon;
					 weapon=null;
					return c;
			case 2:
					c= armor;
					 armor=null;
					return c;
			case 3:
					c= def;
					 def=null;
					return c;
			default:
					c= atk;
					 def=null;
					return c;
		}
	}
	
	boolean is_kill(Card c)
	{
		String s=c.name;
		if (name.equals("����")&&c.color%2==1) return true;
		return s.equals("ɱ")||s.equals("��ɱ")||s.equals("��ɱ");
	}
	
	void qinglongdao(ArrayList<Player> players,ArrayList<Card>all_cards,Player target,Frame f,Scanner sc) throws Exception
	{
		Card c;
		int b;
		if (bot)
		{
			c=search_kill();
			if (c!=null)
			{
				System.out.println(">> "+name+"���������������ٳ�ɱ!");
				c.use(this, sc);
			}
		}
		if (search_kill()!=null)
		{
			b=input("�Ƿ񷢶���������",2);
			if (b==1)
			{
				show(false);
				do
				{b=input("",cards.size());}while (!is_kill(cards.get(b)));
				kill_target=target;
				System.out.println(name+"���������������ٳ�ɱ!");
				cards.get(b).use(this, sc);
				all_cards.add(cards.get(b));
				cards.remove(b);
			}
		}
	}
	
	void qilingong(Player target)
	{
		int b;
		if (target.atk!=null||target.def!=null)
		{
			if (bot)
			{
				System.out.print(">> "+name+" ���������빭��ɱ����");
				if (target.def!=null)
				{
					System.out.print("������ ");
					target.def.println();
					target.def=null;
				}
				else
				{
					System.out.print("������ ");
					target.atk.println();
					target.atk=null;
				}	
			}
			else
			{
				do
				{b=input("��ѡ��ɱ������:0.������ 1.������ 2.������",3);}while(b==1&&target.def==null||b==2&&target.atk==null);
				if (b!=0)
				{
					System.out.print(name+" ���������빭��ɱ����");
					if (b==1)
					{
						System.out.print("������ ");
						target.def.println();
						target.def=null;
					}
					else
					{
						System.out.print("������ ");
						target.atk.println();
						target.atk=null;
					}		
				}
					
			}
		};
	}
	
	void cixiongjian(Player target,ArrayList<Card>all_cards,Frame f)
	{
		int b;
		if (bot) b=1;
		else b=input("�Ƿ񷢶����۽���",2);
		if (b==1)
		{
			System.out.println(">> "+name+" �����˴��۽�");
			if (target.cards.isEmpty())
			{
				System.out.println(">> "+target.name+" ѡ���öԷ���һ����");
				cards.add(all_cards.get(0));
				all_cards.remove(0);
				f.update(this);
			}
			else if (target.bot)
			{
				System.out.print(">> "+target.name+" ������ ");
				target.cards.get(0).println();
				target.cards.remove(0);
				f.update(target);
			}
			else
			{
				b=input("0.�Լ����� 1.�Է�����",2);
				if (b==0)
				{
					show(false);
					b=input("��ѡ������:",cards.size());
					target.cards.remove(target.cards.get(0));
					System.out.print(target.name+" ������ ");
					target.cards.get(0).println();
					target.cards.remove(0);
					f.update(target);
				}
				else
				{
					System.out.println(">> "+target.name+" ѡ���öԷ���һ����");
					cards.add(all_cards.get(0));
					all_cards.remove(0);
					f.update(this);
				}
			}
		}
	}
	
	boolean guanshifu(ArrayList<Card>all_cards,Scanner sc)
	{
		Card c;
		int b;
		if (bot)
		{
			if (cards.size()>=2)
			{
				do
				{c=get_card();}while (c!=weapon);
				System.out.print(">> "+name+" ������ʯ��,������");
				c.print();
				cards.remove(c);
				all_cards.add(c);
				do
				{c=get_card();}while (c!=weapon);
				System.out.print(",");
				c.print();
				cards.remove(c);
				all_cards.add(c);
				System.out.println("ǿ������!");
				return false;
			}
			else
				return true;
		}
		else
		{
			System.out.println("�Ƿ񷢶���ʯ��?");
			show(false);
			b=sc.nextInt();
			if (b==1)
			{
				System.out.println("����������");
				c=input_card(this,0);
				c.drop(this,1);
				c=input_card(this,0);
				cards.remove(c);
				c.drop(this,1);
				System.out.println(">> "+name+" �����˹�ʯ����ǿ������!");
				return false;
			}
			else
				return true;
		}
	}
	
	boolean hanbingjian(Player target)
	{
		Card c;
		if (bot)
		{
			if (!target.no_card()&&new Random().nextBoolean())
			{
				System.out.println(">> "+name+" �����˺�����");
				c=target.get_card();
				System.out.println(">> "+name+" ������ "+target.name+"��"+c.get_printname());
				target.cards.remove(c);
				f.update(target);
				if (new Random().nextBoolean()) return true;
				c=target.get_card();
				System.out.println(">> "+name+" ������ "+target.name+"��"+c.get_printname());
				target.cards.remove(c);
				f.update(target);
				return true;
			}
		}
		else
		{
			System.out.println("�Ƿ񷢶�������?");
			int b=sc.nextInt();
			if (b==1)
			{
				System.out.println(">> "+name+" �����˺�����\n��ѡ��Ŀ�����:");
				c=input_card(target,0);
				System.out.print(">> "+name+" ������ "+target.name+"��");
				if (c.name.equals("����ʨ��")&&c.equip!=null) target.heal(1);
				target.cards.remove(c);
				f.update(target);
				c.equip=null;
				c.println();
				if (target.no_card()) return true;
				System.out.println(name+"������һ������?");
				c=input_card(target,0);
				if (c!=null)
				{
					System.out.print(">> "+name+" ������ "+target.name+"��");
					if (c.name.equals("����ʨ��")&&c.equip!=null) target.heal(1);
					target.cards.remove(c);
					c.equip=null;
					c.println();
					f.update(target);
				}
				return true;
			}

		}
		return false;
	}
	
	boolean no_card()
	{
		return cards.size()==0&&weapon==null&armor==null&&atk==null&&def==null;
	}
	
	
	void draw_card()
	{
		cards.add(all_cards.get(0));
		all_cards.remove(0);
	}
	
	
	boolean test(String s) //�ж�
	{
		Player p;
		Boolean b=true;
		Card c=all_cards.get(0);
		Card d;
		all_cards.remove(0);
		System.out.print(">> "+name+" ��"+s+"���ж���Ϊ");
		c.println();
		p=search_player("˾��ܲ");
		if (p!=null)
		{
			if (p.bot)
			{
				b=(p.no==no^c.succ(s));  //�Լ��ж�ʧ�� �� �����ж��ɹ�
				if (!s.equals("������")&&!s.equals("����")&&!s.equals("�׻�")&&!s.equals("����")) b=!b;  //��Ҫ����
				if (b)
				{
					d=p.search_pan(s,!c.succ(s));
					if (d!=null)
					{
						all_cards.add(c);
						c=d;
						System.out.print("\n>> ˾��ܲ ��������š������");
						c.println();
					}
				}
			}
			else
			{
				d=p.input_card("�Ƿ񷢶�����š���");
				if (d!=null)
				{
					all_cards.add(c);
					c=d;
					System.out.print(">> ˾��ܲ ��������š������");
					c.println();
				}
			}	
		}
		p=search_player("�Ž�");
		if (p!=null)
		{
			if (bot)
			{}
			else
			{
				if (p.input("�Ƿ񷢶����������",2)==1)
				{
					d=p.input_card(this,1);
					if (d!=null)
					{
						System.out.println(">> �Ž� ����������������"+d.get_printname()+"������"+c.get_printname());
						p.cards.add(c);
						c=d;
					}
				}
			}	
		}
		testcard=c;
		
		if (!s.equals("˫��")&&!s.equals("����"))
		{
			System.out.print(">> "+name+" ��"+s);
			
			if (c.succ(s))
			{
				System.out.print("�ж��ɹ�");
				switch(s)
				{
					case"�ֲ�˼��":System.out.println("���������ƽ׶�");break;
					case"�������":System.out.println("���������ƽ׶�");break;
					case"����":System.out.println("�����Ѫ��");break;
					case"������":System.out.println("�������Ӱ����");break;
					default:System.out.println();
				}
			}
			else
			{
				System.out.print("�ж�ʧ��");
				switch(s)
				{
					case"�ֲ�˼��":System.out.println("�����Գ���");break;
					case"�������":System.out.println("����������");break;
					case"����":System.out.println("��Σ�����");break;
					case"����":System.out.println("������ж���");b=false;cards.add(c);break;
					default:System.out.println();
				}
			}
		}
		
		if (s.equals("˫��"))
		{
			cards.add(c);
			System.out.print(">> �����ĳ� ����ж���"+c.color+"�����غ�");
			if (c.color%2==0) System.out.print("��ɫ"); else System.out.print("��ɫ");
			System.out.println("���ƶ����Ե�����������");
			return true;
		}
		
		if (name.equals("����"))
		{
			System.out.print(">> ���� ��������ʡ������");
			c.println();
			p.cards.add(c);
		}
		
		if (s.equals("����"))
		{
			if (c.succ(s))
			{
				cards.add(c);
				b=false;
			}
		}
		
		if (s.equals("����")&&c.succ(s))
		{
			System.out.println(">> �˰� ��"+c.get_printname()+"��Ϊ��������佫����");
			extra.add(new Special(c,"˳��ǣ��",skill));				
			b=false;
		}
		
		if (b&&c.color==2)
		{
			p=search_player("��ֲ");
			if (p!=null)
			{
				System.out.print(">> ��ֲ ��������Ӣ�������");
				c.println();
				p.cards.add(c);
				b=false;
			}
		}

		if (b)all_cards.add(c);
		return c.succ(s);
	}
	
	Card search_pan(String s,boolean b)
	{
		for (int i=0;i<cards.size();i++)
			if (!(cards.get(i).succ(s)^b)) return cards.get(i);
		return null;
			
	}
	
	Player search_player(String s)
	{
		boolean b=true;
		for (int i=no;i!=no||b;i=(i+1)%players.size())
		{
			b=false;
			if (players.get(i).name.equals(s)) return players.get(i);
		}
		return null;
	}
	
	String get_weapon()
	{
		if (weapon==null)
			return "";
		else
			return weapon.get_name();
	}
	
	String get_armor()
	{
		if (armor==null)
			return "";
		else
			return armor.get_name();
	}
	String get_atk()
	{
		if (atk==null)
			return "";
		else
			return atk.get_name();
	}
	String get_def()
	{
		if (def==null)
			return "";
		else
			return def.get_name();
	}
	boolean get_weapon(String s){return weapon!=null&&weapon.name.equals(s);}
	boolean get_armor(String s){return armor!=null&&armor.name.equals(s);}
	boolean get_atk(String s){return atk!=null&&atk.name.equals(s);}
	boolean get_def(String s){return def!=null&&def.name.equals(s);}
	
	String color_trans(int x)
	{
		switch(x)
		{
		case 0:return "����";
		case 1:return "����";
		case 2:return "÷��";
		default:return "��Ƭ";
		}
	}

	void use_skill() throws Exception
	{

		if (stype==3&&limit||stype==1&&once||stype==2&&!(name.equals("��ֲ")&&under)&&!(name.equals("�˰�")&&extra.isEmpty()))
			switch(skill)
			{
				case "�ʵ�":rende();break;
				case "�ƺ�":zhiheng();break;
				case "����":kurou();break;
				case "����":fanjian();break;
				case "����":jieyin();break;
				case "���":lijian();break;
				case "����":qingnang();break;
				case "��ʫ":jiushi();break;
				case "����":dimeng();break;
				case "����":quhu();break;
				case "ǿϮ":qiangxi();break;
				case "����":luanwu();break;
				case "����":gongxin();break;
				case "����":tiaoxin();break;
				case "ҵ��":yeyan();break;
				case "ѣ��":xuanhuo();break;
				case "����":jianyan();break;
				case "��Ϯ":jixi();break;
				case "����":paiyi();break;
				case "���":lihun();break;
				case "�һ�":luanji();break;
				case "���":qice();break;
			}
		if (stype==1) once=false;
		if (stype==3) limit=false;
	}

	void qice() throws Exception
	{
		int index;
		Card c;
		String[] names=new String[9];
		names[0]="��������";
		names[1]="����뷢";
		names[2]="��������";
		names[3]="����";
		names[4]="���Ӳ���";
		names[5]="˳��ǣ��";
		names[6]="��԰����";
		names[7]="��";
		names[8]="�赶ɱ��";
		if (cards.isEmpty()||bot&&cards.size()>1)
		{
			fail++;
			return;
		}
		if (bot)
		{
			index=new Random().nextInt(4);
		}
		else
		{
			for (int i=0;i<9;i++)
				System.out.println(String.valueOf(i)+'.'+names[i]);
			index=input("��ѡ�����Ľ�����:",9);
		}
		c=new Special(cards.get(0),names[index],"���");
		c.sub=new ArrayList<Card>();
		for (int i=0;i<cards.size();i++)
		{
			c.sub.add(cards.get(0));
			cards.remove(0);
		}
		c.use(this, sc);
	}
	
	
	void luanji() throws Exception
	{
		if (cards.isEmpty()) 
		{
			fail++;
			return;
		}
		Card c,d=null,e;
		if (bot)
		{
			Collections.shuffle(cards,new Random());
			c=cards.get(0);
			for (Card f:cards)
				if (f.color==c.color) d=f;
		}
		else
		{
			c=hand_card("��ѡ���һ���:",cards);
			d=hand_card("��ѡ���һ���:",cards);
		}
		if (d==null||c==d||c.color!=d.color)
		{
			System.out.println(">> �����Ʋ�����Ҫ��");
			return;
		}
		else
		{
			e=new Special(c,"����뷢","�һ�");
			e.sub=new ArrayList<Card>();
			e.sub.add(c);
			e.sub.add(d);
			cards.remove(c);
			cards.remove(d);
			e.use(this, sc);
		}
		
	}
	
	void lihun()
	{
		System.out.println(">> SP���� ��������꡿�����佫�Ʒ���");
		under=true;
		if (bot)
		{
			Card c=input_card();
			cards.remove(c);
			c.drop(this, 1);
			Player target=get_target(4);
			if (target==null||target.cards.isEmpty())
			{
				fail++;
				return;
			}
			cards.addAll(target.cards);
			System.out.println(">> SP���� ����"+c.get_printname()+"�������"+target.name+"����������");
			active=true;
			f.update(this);
			f.update(target);	
		}
		else
		{
			Card c=hand_card("��ѡ�������:",cards);
			cards.remove(c);
			c.drop(this, 1);
			Player target=input_target("��ѡ�����Ŀ��:",4);
			cards.addAll(target.cards);
			target.cards.removeAll(target.cards);
			System.out.println(">> SP���� ����"+c.get_printname()+"�������"+target.name+"����������");
			active=true;
			f.update(this);
			f.update(target);	
		}
	}
	
	void paiyi() throws Exception
	{
		Card c;
		Player target=null;
		if (bot)
		{
			for (Player p:players)
				if (p.hp==1&&p.cards.size()+2>cards.size()) target=p;
			if (target==null||new Random().nextInt(4)==0)
				target=this;
			target=this;
			c=extra.get(0);
		}
		else
		{
			show_extra();
			c=extra.get(input_posi("��ѡ����ȥ�ġ�Ȩ��:",extra.size()));
			target=input_target("��ѡ��Ŀ��:",6);
		}
			extra.remove(c);
			c.drop(this,0);
			System.out.println(">> �ӻ� ��ȥһ�š�Ȩ������"+target.name+"��������");
			target.draw_card();
			target.draw_card();
			f.update(this);
			if (target.cards.size()>cards.size()) target.damage(1,0,this,null);	
		
	}
	
	void jixi() throws  Exception
	{
		if (bot)
		{
			Card c=extra.get(0);
			extra.remove(c);
			c.use(this, sc);
			c.drop(this, 0);
		}
		else
		{
			show_extra();
			Card c=extra.get(input_posi("��ѡ��ʹ�õġ��:",extra.size()));
			extra.remove(c);
			c.use(this, sc);
			c.drop(this, 0);
		}
	}
	
	void jianyan()
	{
		Card c;
		boolean b;
		Player p;
		System.out.println(">> ������ ���������ԡ�");
		if (bot)
		{
			Random r=new Random();
			int x=r.nextInt(5);
			if (search("��")==null&&search("��")==null&&r.nextInt(hp)==0)
				x=1;
			else if (r.nextInt(equip_count())==0) x=2;
			do
			{
				b=true;
				c=all_cards.get(0);
				all_cards.remove(c);
				switch(x)
				{
					case 0:if (c.type==0) b=false;break;
					case 1:if (c.type==1) b=false;break;
					case 2:if (c.type>1) b=false;break;
					case 3:if (c.color%2==0) b=false;break;
					case 4:if (c.color%2==1) b=false;break;
				}
				System.out.println(">> ������ ���ƶѶ�������"+c.get_printname());
				if (!b)
					all_cards.add(c);
			}while (b);
			System.out.println(">> ������ ��"+c.get_printname()+"�����Լ�");
			cards.add(c);	
		}
		else
		{
			int x=input("��ѡ���Ƶ����ͻ���ɫ:0.���� 1.���� 2.װ�� 3.��ɫ 4.��ɫ",5);

			do
			{
				b=true;
				c=all_cards.get(0);
				all_cards.remove(c);
				switch(x)
				{
					case 0:if (c.type==0) b=false;break;
					case 1:if (c.type==1) b=false;break;
					case 2:if (c.type>1) b=false;break;
					case 3:if (c.color%2==0) b=false;break;
					case 4:if (c.color%2==1) b=false;break;
				}
				System.out.println(">> ������ ���ƶѶ�������"+c.get_printname());
				if (!b)
					all_cards.add(c);
			}while (b);
			p=input_target("��ѡ��֮������Ŀ��:",4);
			System.out.println(">> ������ ��"+c.get_printname()+"����"+p.name);
			p.cards.add(c);
		}
	}
	
	void rende()
	{
		fail++;
	}
	
	void zhiheng()
	{
		Card c;
		int count=0;
		ArrayList<Card>list=new ArrayList<Card>();
		if (bot)
		{
			System.out.println(">> ��Ȩ �������ƺ⡿");
			count=new Random().nextInt(cards.size())+1;
			for (int i=0;i<count;i++)
			{
				c=get_card();
				System.out.println(">> ��Ȩ ������"+c.get_printname());
				if (i<count-1) System.out.print(",");
				cards.remove(c);
				c.drop(this,1);
			}
			System.out.println(">> ��Ȩ ����"+String.valueOf(count)+"����");
			for (int i=0;i<count;i++)
				draw_card();
		}
		else
		{
			System.out.println(">> ��Ȩ �������ƺ⡿");
			do
			{
				c=input_card(this,0);
				if (c!=null)
				{
					list.add(c);
					count++;
				}
			}while(c!=null);
			for (Card d:list)
			{
				System.out.println(">> ��Ȩ ������"+d.get_printname());
				d.drop(this,1);
			}
			System.out.println(">> ��Ȩ ����"+String.valueOf(count)+"����");
			for (int i=0;i<count;i++)
				draw_card();
		}
	}
	
	void kurou() throws Exception
	{
		
		if (bot&&new Random().nextInt(hp-1)<1)
		{
			fail++;
		}
		else
		{
			System.out.println(">> �Ƹ� ���������⡿");
			loss(1);
			System.out.println(">> �Ƹ� ����������");
			draw_card();
			draw_card();
		}
	}
	
	void fanjian() throws Exception
	{
		Player target;
		Card c;
		int x;
		System.out.println(">> ��� ���������䡿");
		if (bot)
			target=get_target(2);
		else
			target=input_target("��ѡ�񷴼��Ŀ��:",2);
		
		c=random_card();
		if (target.bot)
			x=new Random().nextInt(4);
		else
			x=input("��ѡ��ɫ:",4);
		System.out.println(">> "+target.name+" ѡ����"+color_trans(x));
		System.out.println(">> "+target.name+" �鵽��"+c.get_printname());
		cards.remove(c);
		target.cards.add(c);
		if (c.color!=x)
			target.damage(1, 0, this, null);
		
		
	}
	
	void lijian() throws Exception
	{
		if (cards.isEmpty()) 
		{
			fail++;
			return;
		}
		Player target1,target2;
		Card c;
		boolean b;
		if (bot)
		{
			ArrayList<Player>ps=get_players(2);
			target1=ps.get(0);
			target2=ps.get(1);
			c=random_card();
		}
		else
		{
			target1=input_target("��ѡ���ȳ�ɱ��Ŀ��:",4);
			do {target2=input_target("��ѡ����ɱ��Ŀ��:",4);}while(target1.equals(target2));
			c=cards.get(input("��ѡ�����õ�����:",cards.size()));
		}
		
		cards.remove(c);
		c.drop(this,1);
		System.out.println(">> ���� ��������䡿��������"+c.get_printname()+"����Ϊ"+target2.name+"��"+target1.name+"ʹ�á�������");
		
		if (target1.name.equals("���"))
		{
			System.out.println(">> ��� ������������������һ����");
			target1.draw_card();
		}
		
		if (target2.name.equals("���"))
		{
			System.out.println(">> ��� ������������������һ����");
			target2.draw_card();
		}	
		
		if (target2.name.equals("����")||target1.name.equals("����")) System.out.println("���� ��������˫��");
		b=true;
		while(true)
		{
			if (b)
			{
				if (this.name.equals("����"))
				{
					if (target1.bot&&target1.search_killnum()<2)
					{
						target1.damage(1, 0,target2,null);
						break;
					}
					else
					{
						if (target1.react_kill()&&target1.react_kill())  //��˫
							b=false;
						else
							target1.damage(1, 0,target2,null);										
					}
				}
				else if (target1.react_kill())
						b=false;
				else
				{
					target1.damage(1, 0,target2,null);
					break;
				}
			}		
			else
			{
				if (target1.name.equals("����"))
				{
					if (target2.bot&&target2.search_killnum()<2)
					{
						target2.damage(1, 0,target1,null);
						break;
					}
					else
					{
						if (target2.react_kill()&&target2.react_kill())  //��˫
							b=false;
						else
							target2.damage(1, 0,target1,null);										
					}
				}
				else if (target2.react_kill())
						b=true;
				else
				{
					target2.damage(1, 0,target1,null);
					break;
				}	
			}
		}
		
	}
	
	void jieyin()
	{
		Player target;
		if (bot)
		{
			fail++;
			return;
		}
		target=input_target("��ѡ������Ķ���",8);
		System.out.print("��������������:");
		ArrayList<Card> drops=input_cards(2,cards);
		for (Card c:drops)
		{
			System.out.println(">> ������ ������"+c.get_printname());
			c.drop(this,1);
		}
		heal(1);
		target.heal(1);
	}
	
	void qingnang()
	{
		Player target;
		Card c;
		if (cards.isEmpty())
		{
			fail++;
			return;
		}
		if (bot)
		{
			target=get_target(2);
			c=cards.get(new Random().nextInt(cards.size()));
			cards.remove(c);
			c.drop(this,1);
			System.out.println("��٢ ������"+c.get_printname());
			target.heal(1);
		}
		else
		{
			target=input_target("��ѡ�����ҵ�Ŀ��:",8);
			System.out.println("��٢ �����ˡ����ҡ���Ŀ����"+target.name);
			c=input_card();
			cards.remove(c);
			c.drop(this,1);
			System.out.println("��٢ ������"+c.get_printname());
			target.heal(1);
			
		}
	}
	
	void jiushi()
	{
		if (under)
		{
			fail++;
			return;
		}
		if (!bot||search_kill()!=null&&kill<maxkill&&new Random().nextBoolean())
		{
			if (!under)
			{
				System.out.println(">> ��ֲ ��������ʫ�������沢��Ϊʹ���ˡ��ơ�");
				drunk=true;
				under=true;
				f.update(this);
			}
		}
	}
	
	void dimeng()
	{
		Player target1,target2;
		if (bot)
		{
			fail=2;
			return;
		}
		else
		{
			target1=input_target("��ѡ���һ��Ŀ��:",2);
			do {target2=input_target("��ѡ��ڶ���Ŀ��:",2);}while(target1.equals(target2));
			if (Math.abs(target1.cards.size()-target2.cards.size())>cards.size()) 
				System.out.println("���������㣬����ʧ�ܣ�");			
			else
			{
				drop_cards(Math.abs(target1.cards.size()-target2.cards.size()));
				System.out.println("³�� ���������ˡ���������"+target1.name+"��"+target2.name+"������");
			}
		}
	}
	
	void quhu() throws Exception
	{
		Player target,target2;
		Card c=null,d;
		ArrayList<Player>ps=get_players(4);
		if (ps.isEmpty()||cards.isEmpty())
		{
			fail++;
			return;
		}
		if (bot)
		{
				target=random_player(ps);
				c=get_large();
				if (target.bot)
					d=target.get_large();
				else
					d=target.input_card("��ѡ��ƴ����");
				System.out.println(">> "+name+"�����"+c.get_printname());
				System.out.println(">> "+target.name+"�����"+d.get_printname());
				if (c.num>d.num)
				{
					System.out.println(">> ������"+target.name+"ƴ��Ӯ��");
				}
				else 
				{
					System.out.println(">> ������"+target.name+"ƴ��ûӮ");
					damage(1,0,target,null);
				}
				cards.remove(c);
				cards.remove(d);
			
		}
		else
		{
			target=input_target("��ѡ��������Ŀ��:",7);
			c=cards.get(input("��ѡ��ƴ�����:",cards.size()));
			d=target.get_large();
			System.out.println(">> "+name+"�����"+c.get_printname());
			System.out.println(">> "+target.name+"�����"+d.get_printname());
			if (c.num>d.num)
			{
				target2=target.input_target(">> ������"+target.name+"ƴ��Ӯ��\n��ѡ�������˺���Ŀ��:",1);
				target2.damage(1,0, target,null);
			}
			else 
			{
				System.out.println(">> ������"+target.name+"ƴ��ûӮ");
				damage(1,0,target,null);
			}
			cards.remove(c);
			cards.remove(d);
		}
	}
	
	ArrayList<Card> get_cards(int type)
	{
		ArrayList<Card>cs=new ArrayList<Card>();
		for (Card c:cards)
		{
			switch (type)
			{
			case 0:if (c.type==2) cs.add(c);break;
			}
		}
		return cs;
	}
	
	void qiangxi() throws Exception
	{
		Player target;
		Card c;
		ArrayList<Player>ps=get_players(2);
		ArrayList<Card>cs=get_cards(0);
		if (bot)
		{
			if (ps.isEmpty()&&cs.isEmpty())
			{
				fail++;
				return;
			}
			if (cs.isEmpty())
			{
				loss(1);
				target=ps.get(0);
				System.out.println(">> ��Τ ������ǿϮ����Ŀ����"+target.name);
				System.out.println(">> ��Τ ��ʧһ������");			
			}
			else
			{
				c=cs.get(0);
				if (ps.isEmpty())
				{
					target=get_target(2);
				}
				else
					target=ps.get(0);
				c=cs.get(0);
				c.drop(this,1);
				System.out.println(">> ��Τ ������ǿϮ����Ŀ����"+target.name);
				System.out.println(">> ��Τ ����"+c.get_printname());
			}
			target.damage(1, 0, this, null);
		}
		else
		{
			if (input("��ѡ��ǿϮ����ʽ��0.��Ѫ 1.������:",2)==0)
				loss(1);
			else
			{
				c=input_card(this,0);
				if (c.type!=2) return;
				c.drop(this,1);
			}
			target=input_target("��ѡ��ǿϮ��Ŀ��:",1);
			System.out.println(">> ��Τ ������ǿϮ����Ŀ����"+target.name);
			target.damage(1, 0, this, null);
			
		}
	}
	
	void gongxin() throws Exception
	{
		Player target;
		Card c=null,d=null;
		int index;
		if (bot)
		{
			ArrayList<Player>ps=get_players(0);
			if (!ps.isEmpty())
			{
				target=random_player(ps);
				System.out.println(">> ������ ���������ġ���Ŀ����"+target.name);
				System.out.print(">> ������ �ۿ���"+target.name+"������");
				for (Card e:target.cards)
					if (e.color==1&&(c==null||e.name.equals("��")||e.name.equals("��������")))
						c=e;
				if (c!=null)
				{

						d=search("��������");
						if (d!=null)
						{
							if (c.name.equals("��")||c.name.equals("��������"))
							{
								System.out.println("������"+c.get_printname()+"�����ƶѶ�");
								d.use(this, sc);
								return;
							}
						}
						System.out.println("������"+c.get_printname()+"����");
						c.drop(target,1);
				}
						
			}
			
		}	
		else
		{
			target=input_target("��ѡ��Ŀ��:",3);
			System.out.println(">> ������ ���������ġ���Ŀ����"+target.name);
			
			target.show(false);
			do {index=input("��ѡ���������:",target.cards.size());}while(index>=0&&target.cards.get(index).color!=1);			
			if (index==-1) System.out.println();
			else
			{
				c=target.cards.get(index);
				target.cards.remove(index);
				index=input_posi("0.�����ƶѶ�\n1.����",2);
				System.out.print(">> ������ �ۿ���"+target.name+"������");
				if (index==0)
				{
					System.out.println("������"+c.get_printname()+"�����ƶѶ�");
					all_cards.add(0,c);
				}
				else
				{
					System.out.println("������"+c.get_printname()+"����");
					c.drop(target,1);
				}
			}
				
			
		}
	}
	
	void luanwu()
	{
		fail++;
	}
	
	void tiaoxin() throws Exception  //ʵ��botѡ�ƣ�
	{
		Player target;
		Card c;
		int index;
		int rnd;
		System.out.print(">> ��ά �����ˡ����ơ�");
		if (bot)
		{
			ArrayList<Player>ps=get_players(1);
			if (search("��")!=null)
				rnd=3;
			else 
				rnd=2;
			
			if (!ps.isEmpty()&&new Random().nextInt(rnd)>0)
			{
				target=random_player(ps);
				c=target.search_kill();
				if (c==null) 
				{ 
					System.out.println(">> "+target.name+" ������ɱ");
					c=target.get_card();
					System.out.print(">> ��ά ������Ŀ���"+c.get_equip()+c.get_printname());
					target.cards.remove(c);
					f.update(target);
				}
				else
				{
					if (target.bot)
					{
						target.kill_target=this;
						c.use(target, sc);
						all_cards.add(c);
						c.drop(target,1);
					}
					else
					{				
						System.out.println("�����ɱ��:");
						target.show(false);
						do{index=target.input("",target.cards.size());}while (index>=0&&!target.cards.get(index).is_kill());
						if (index<0) 
						{
							System.out.println(">> "+target.name+" ������ɱ");
							c=target.get_card();
							System.out.print(">> ��ά ������Ŀ���"+c.get_equip()+c.get_printname());
							target.cards.remove(c);
							f.update(target);
						}
						else
						{
							c=target.cards.get(index);
							target.kill_target=this;
							c.use(target, sc);
							target.cards.remove(c);
							all_cards.add(c);
						}
					}
				}
			}
		}
		else
		{
			target=players.get(input_other("\n��ѡ��Ŀ��:",players.size()));
			c=target.search_kill();
			if (c==null) 
			{ 
				System.out.println(">> "+target.name+" ������ɱ");
				c=input_card(target,0);
				System.out.print(">> ��ά ������Ŀ���"+c.get_equip()+c.get_printname());
				target.cards.remove(c);
				f.update(target);
			}
			else
			{
					target.kill_target=this;
					c.use(target, sc);
					all_cards.add(c);
					c.drop(target,1);

			}
		}
	}
	
	void yeyan() throws Exception
	{
		Player target1,target2=null,target3=null;
		int d1=0,d2=0,d3=0;
		ArrayList<Card> drops=new ArrayList<Card>();
		Card c;
		if (bot)
		{
			fail++;
		}
		else
		{
			target1=input_target("��ѡ��һ��Ŀ��:",2);
			d1=input("�����������˺�:",4);
			if (d1<3) 
			{
				do{target2=input_target("�Ƿ���ѡ��һ��Ŀ��:",2);}while (target2.equals(target1));
				if (target2!=null)
				{
					d2=input("�����������˺�:",4-d1);
					if (d1+d2<3)
						do{target3=input_target("�Ƿ���ѡ��һ��Ŀ��:",2);}while (target3.equals(target1)||target3.equals(target2));
					if (target3!=null) d3=1;
				}
			}
			if (d1>1||d2>1||d3>1)
			{
				c=input_colored("����һ�ź�������:",0);
				if (c==null) return; else drops.add(c);
				c=input_colored("����һ�ź�������:",1);
				if (c==null) return; else drops.add(c);
				c=input_colored("����һ��÷������:",2);
				if (c==null) return; else drops.add(c);
				c=input_colored("����һ�ŷ�Ƭ����:",3);
				if (c==null) return; else drops.add(c);
				for (int i=0;i<4;i++)
				{
					System.out.println(">> ����� ������"+drops.get(i).get_printname());
					drops.get(i).drop(this,1);
				}
				loss(3);
			}
			System.out.println(">> ����� ������ҵ�ס�");
			if (d1>0)
				target1.damage(d1, 1,this,null);
			if (d2>0)
				target2.damage(d2, 1,this,null);
			if (d3>0)
				target3.damage(d3, 1,this,null);
		}
	}
	
	void xuanhuo()
	{
		Player target;
		Card c;
		
		if (bot)
		{
			fail++;
		}
		else
		{
			c=input_colored("��ѡ��һ�ź�������:",1);
			if (c!=null)
			{
				target=input_target("��ѡ��Ŀ��:",2);
				target.cards.add(c);
				cards.remove(c);
				System.out.println(">> ���� ������ѣ�󡿣���"+c.get_printname()+"������"+target.name);
				c=input_card(target,0);
				f.update(this);
				System.out.print(">> ���� ��ȡ��Ŀ���");
				if (c.equip==null)
					System.out.println("����"+c.get_printname());
				else
					System.out.println(c.equip+c.get_printname());
				c.drop(target,2);
				f.update(target);
				target=input_target("��ѡ�񽻸���Ŀ��:",6);
				if (target==null) target=this;
				System.out.println(">> ���� �����ƽ���"+target.name);
				target.cards.add(c);
				f.update(target);		
			}
		}
	}
	
	ArrayList<Card> input_cards(int count,ArrayList<Card> cards)
	{
		ArrayList<Card> list = new ArrayList<Card>();
		int x;
		while (count>0)
		{
			x=sc.nextInt();
			if (x<cards.size()&&!list.contains(cards.get(x)))
			{
				count--;
				list.add(cards.get(x));
			}
		}
		return list;
	}
	
	ArrayList<Card> input_cards(ArrayList<Card> cards)
	{
		ArrayList<Card> list = new ArrayList<Card>();
		int x=0;
		while (x!=-1)
		{
			x=sc.nextInt();
			if (x>=0&&x<cards.size()&&!list.contains(cards.get(x)))
				list.add(cards.get(x));
		}
		return list;
	}
	
	Card get_large()
	{
		Card d=cards.get(0);
		for (Card c:cards)
			if (c.num>d.num&&!d.name.equals("��")&&!d.name.equals("��������")) d=c;
		return d;
	}
	
	Card hand_card(String s,ArrayList<Card>list)
	{
		return list.get(input_posi(s,list.size()));
	}
	
	Card random_card()
	{
		return cards.get(new Random().nextInt(cards.size()));
	}
	
	ArrayList<Player> get_players(int type)
	{
		ArrayList<Player> ps=new ArrayList<Player>();
		for (Player p: players)
		{
			switch (type)
			{
				case 0:if (!p.cards.isEmpty()&&p.pos!=pos) ps.add(p);break;
				case 1:if (p.cards.size()<=2) ps.add(p);break;
				case 2:if (p.hp==1) ps.add(p);break;
				case 4:if (p.hp>hp) ps.add(p);break;
			}
		}
			
		return ps;
	}
	
	Player random_player(ArrayList<Player>ps)
	{
		Player target=ps.get(new Random().nextInt(ps.size()));
		ps.remove(target);
		return target;
	}
	
	ArrayList<Card> search_card(int type)
	{
		ArrayList<Card> cs=new ArrayList<Card>();
		for (Card c:cards)
		{
			if (type<=5)
			{
				if (c.type==type) cs.add(c);
			}
			else if (type==6&&c.type>1) cs.add(c);
		}
		return cs;
		
	}
	
	int equip_count()
	{
		int s=0;
		if (weapon!=null) s++;
		if (armor!=null) s++;
		if (atk!=null) s++;
		if (def!=null) s++;
		return s;
	}
}
