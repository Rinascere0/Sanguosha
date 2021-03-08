package sanguosha;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Special extends Card{
	Special(int c,int n,String s)
	{
		super(c,n,s);
		type=1;
	}
	
	Special(Card c,String s,String sk)
	{
		super(c,s,sk);
		type=1;
	}
	
	Special(String s,Frame f,ArrayList<Player>p,ArrayList<Card>c)
	{
		super(s,f,p,c);
		type=1;
	}
	
	void use(Player p,Scanner sc) throws Exception
	{
		int index;
		Player target;
		Card c;
		boolean b;
		ArrayList <Card> temp=new ArrayList <Card>();
		ArrayList <Integer> id=new ArrayList <Integer>();
		System.out.print(">> "+p.name+" ʹ����"+showname());
		if (skill!=null) System.out.print("("+skill+")");
		print_origin();
		if (p.name.equals("����Ӣ")&&!name.equals("����")&&!name.equals("�ֲ�˼��")&&!name.equals("�������")) 
		{
			System.out.println("\n>> ����Ӣ ���������ǡ�������һ����");
			p.cards.add(all_cards.get(0));
			all_cards.remove(0);
		}
		Thread.currentThread().sleep(300);
		switch (name)
		{
			case "��������":
				System.out.println();
				if (p.wuxie(p.name,showname(),false)) {System.out.println(">> "+p.name+" �ڡ��������С���Ч����������");break;}
				System.out.println(">> "+p.name+ " �����������");
				p.draw_card();
				p.draw_card();
				break;	

				
			case "���Ӳ���":
				if (p.bot)
				{
					target=p.get_target(2);
					c=get_card(target);
				}
				else
				{
					System.out.println();
					target=input_target("��ѡ��Ŀ��:",players);
					while(target.empty())
						target=input_target("��ѡ��Ŀ��:",players);
					c=p.input_card(target,0);
				}
				if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" �ڡ����Ӳ��š� ��Ч����������");break;}
				System.out.print(">> "+p.name+" ����� "+target.name+" ��");
				c.println();
				if (c.name.equals("����ʨ��")&&c.equip!=null) target.heal(1);
				all_cards.add(c);
				target.cards.remove(c);
				c.equip=null;
				f.update(target);
				break;

			case "˳��ǣ��":
				if (p.bot)
				{
					target=p.get_target(0);
					c=get_card(target);
				}
				else
				{
					System.out.println();
					target=input_target("��ѡ��Ŀ��:",players);
					while(target.empty()||!p.in_dist(target, false))
						target=input_target("��ѡ��Ŀ��:",players);
					c=p.input_card(target,0);
				}
				if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" �ڡ�˳��ǣ�򡿵�Ч����������");break;}
				System.out.print(">> "+p.name+" ˳���� "+target.name+" ��");
				if (c.equip!=null)
					c.println();
				else
					System.out.println("һ����");
				if (c.name.equals("����ʨ��")&&c.equip!=null) target.heal(1);
				target.cards.remove(c);
				c.equip=null;
				p.cards.add(c);
				f.update(target);
				f.update(p);
				break;
			
			case "����":				
				if (p.bot)
					if (color%2==0)
						target=p.get_target(6);
					else
						target=p.get_target(4);
				else
				{
					System.out.println();
					if (color%2==0)
						target=p.input_target("��ѡ��Ŀ��:",10);
					else
						target=p.input_target("��ѡ��Ŀ��:",2);
				}
				
				if (p.name.equals("���"))
				{
					System.out.println(">> ��� ������������������һ����");
					p.draw_card();
				}
				
				if (target.name.equals("���"))
				{
					System.out.println(">> ��� ������������������һ����");
					target.draw_card();
				}					
							
				if (target.wuxie(target.name,showname(), true)) {System.out.println(">> "+target.name+" �ڡ���������Ч����������");break;}
				b=true;
				if (p.name.equals("����")||target.name.equals("����")) System.out.println("���� ��������˫��");
				while(true)
				{
					if (b)
					{
						if (this.name.equals("����"))
						{
							if (target.bot&&target.search_killnum()<2)
							{
								target.damage(1, 0,p,origin);
								break;
							}
							else
							{
								if (target.react_kill()&&target.react_kill())  //��˫
									b=false;
								else
									target.damage(1, 0,p,origin);										
							}
						}
						else if (target.react_kill())
								b=false;
						else
						{
							target.damage(1, 0,p,origin);
							break;
						}
					}		
					else
					{
						if (target.name.equals("����"))
						{
							if (p.bot&&p.search_killnum()<2)
							{
								p.damage(1, 0,target,origin);
								break;
							}
							else
							{
								if (p.react_kill()&&p.react_kill())  //��˫
									b=false;
								else
									p.damage(1, 0,target,origin);										
							}
						}
						else if (p.react_kill())
								b=true;
						else
						{
							p.damage(1, 0,target,origin);
							break;
						}	
					}
				}
				break;
			
			case "��԰����":
				System.out.println();
				b=true;
				for (int i=p.no;i!=p.no||b;i=(i+1)%players.size())
				{
					b=false;
					if (players.get(i).hp<players.get(i).maxhp)
						if (players.get(i).wuxie( players.get(i).name,showname(),false)) 
							System.out.println(">> "+players.get(i).name+" �ڡ���԰���塿��Ч����������");
						else
							players.get(i).heal(1);
				}
				break;
				
			case "��ȷ��":
				System.out.println("\n������ơ�");
				for (int i=0;i<players.size();i++)
				{
					temp.add(all_cards.get(i));
					System.out.print("    "+String.valueOf(i)+".");
					temp.get(i).show();
					System.out.println();
					id.add(i);
				}
				all_cards.removeAll(temp);
				b=true;
				for (int i=p.no;i!=p.no||b;i=(i+1)%players.size())
				{
					target=players.get(i);
					if (players.get(i).wuxie(target.name,showname(),false)) {System.out.println(">> "+players.get(i).name+" �ڡ���ȷ�ǡ���Ч����������");continue;}
					b=false;
					if (target.bot)
						index=id.get(new Random().nextInt(id.size()));
					else
						do
						{index=target.input("��ѡ�������:",players.size());}
						while(id.indexOf(index)==-1);
					System.out.print(">> "+target.name+" ѡȡ��");
					temp.get(index).print();
					System.out.println("("+index+")");
					target.cards.add(temp.get(index));		
					id.remove(id.indexOf(index));
				}
				break;
			
			case "��������":
				System.out.println();
				for (int i=(p.no+1)%players.size();i!=p.no;i=(i+1)%players.size())
				{
					target=players.get(i);
					if (target.armor!=null&&target.armor.name=="�ټ�")
					{
							System.out.println(">> "+target.name+"�ټ׵ֵ�����������");
							continue;
					}
					if (target.name.equals("��ڼ")&&color%2==0)
					{
						System.out.println(">> ��ڼ�������Ļ������Ϊ�������ֵ�Ŀ��");
						continue;
					}
					if (target.name.equals("�ϻ�"))
					{
						System.out.println(">> �ϻ� ���������ס�������������Ч");
						continue;
					}
					if (target.name.equals("ף��"))
					{
						System.out.println(">> ף�ڷ��������󡿣� ����������Ч");
						continue;
					}
					if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" �ڡ��������֡���Ч����������");continue;}
					if (!target.react_kill()) 
					{
						if (p.search_player("�ϻ�")==null)
							target.damage(1,0,p,origin);
						else
							target.damage(1,0,p.search_player("�ϻ�"),origin);		
					}
				}
				if (stat&&p.search_player("ף��")!=null)
				{
					System.out.println(">> ף�ڷ��������񡿣��������š��������֡�"+get_printname());
					p.fetch(p.search_player("ף��").cards,this);
					set_stat();
				}
				break;
				
			case "����뷢":
				System.out.println();
				for (int i=(p.no+1)%players.size();i!=p.no;i=(i+1)%players.size())
				{
					target=players.get(i);
					if (target.armor!=null)
					{
						if (target.armor.name.equals("�ټ�"))
						{
							System.out.println(">> "+target.name+"���ټ׵ֵ�������뷢");
							continue;
						}
						if (target.armor.name.equals("������")&&target.test("������"))
							continue;
						if (target.name.equals("����")&&target.armor==null)
						{
							System.out.println(">> ���� ����������");
							if(target.test("������")) continue;
						}
					}
					if (target.wuxie(target.name,showname(), true)) {System.out.println(">> "+target.name+" �ڡ��������֡���Ч����������");continue;}
					if (!target.react("��",p)) target.damage(1,0,p,origin);			
				}
				break;
			
			case "��������":		
				if (p.bot)		
					index=(new Random(1)).nextInt()%2;	
				else
				{
					System.out.println();
					index=p.input("0.����\n1.����",2);
				}
				if (index==0)
				{
					if (p.bot) System.out.println();
					p.draw_card();
					System.out.println(">> "+p.name+" ������"+showname());
				}
				else 
				{
					if (p.bot)
						target=p.get_target(5);	
					else
						target=p.input_target("��ѡ��Ŀ��:", 9);
					if (!target.wuxie(target.name,showname(),true)) 
					{
						if (target.link)
						{
							System.out.println(">> "+p.name+" �� "+target.name+" ���������״̬");
							target.link=false;
						}
						else
						{
							System.out.println(">> "+p.name+" �� "+target.name+" ʩ��������״̬");
							target.link=true;
						}
						f.update(target);
					}
					else
					{
						System.out.println(">> "+target.name+" �ڡ�������������Ч����������");
					}
					if (p.bot)
					{
						if (new Random().nextBoolean()) 
						{
							System.out.print(">> "+p.name+" ʹ����"+showname());
							target=target.get_target(6);		
						}
					}
					else
					{
						target=target.input_target("��ѡ��һ��Ŀ����",10);						
					}
					if (target!=null)
					{
						if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" �ڡ�������������Ч����������");break;}
						if (target.link)
						{
							System.out.println(">> "+p.name+" �� "+target.name+" ���������״̬");
							target.link=false;
						}
						else
						{
							System.out.println(">> "+p.name+" �� "+target.name+" ʩ��������״̬");
							target.link=true;
						}
						f.update(target);
					}
				
						
					
				}
				break;
			
			case "��":
			{
				int d;
				if (p.bot)
				{
					target=p.get_target(3);		
					if (target.wuxie( target.name,showname(),true))  {System.out.println(">> "+target.name+" �ڡ��𹥡���Ч����������");break;}
					if (target.bot)
						c=target.cards.get(new Random().nextInt(target.cards.size()));
					else
					{
						target.show(false);
						c=target.cards.get(target.input("��չʾһ������:",target.cards.size()));
					}
					System.out.print(">> "+target.name+" չʾ��"+c.show_color());
					c.println();
					if (p.search(c.color,p.cards)==null)
						System.out.println(">> "+p.name+" ����!");
					else
					{
						System.out.print(">> "+p.name+" ������"+c.show_color());
						c=p.search(c.color,p.cards);
						c.println();
						p.cards.remove(c);
						c.drop(p, 1);
						target.damage(1, 1,p,origin);
					}		
					
				}
				else
				{
					System.out.println();
					do
					{target=p.input_target("��ѡ��Ŀ��:",6);}while(target.cards.isEmpty());
					if (target.wuxie(target.name,showname(), true))  {System.out.println(">> "+target.name+" �ڡ��𹥡���Ч����������");break;}
					if (target.bot)
						c=target.cards.get(new Random().nextInt(target.cards.size()));
					else
						c=target.cards.get(target.input("��չʾһ������:",target.cards.size()));
					System.out.print(">> "+target.name+" չʾ��"+c.show_color());
					c.println();
					if (p.search(c.color,p.cards)==null)
						System.out.println(">> "+p.name+" ����!");
					else
					{
						p.show(false);
						do{d=p.input("����һ����ͬ��ɫ���ƷŻ�:",p.cards.size());}while(d>=0&&p.cards.get(d).color!=c.color);
						if (d==-1)
							System.out.println(">> "+p.name+" ����!");
						else
						{
							c=p.cards.get(d);
							System.out.print(">> "+p.name+" ������"+c.show_color());
							c.println();
							c.drop(p,1);
							p.cards.remove(c);
							target.damage(1, 1,p,origin);
						}	
					}
				}
			}
			break;
				
			case "�赶ɱ��":
				ArrayList <Player> targets=new ArrayList<Player>();
				Player killer;
				for (int i=0;i<players.size();i++)
					if (players.get(i).weapon!=null&&i!=p.no)
						targets.add(players.get(i));
				if (p.bot)
				{
					killer=targets.get(new Random().nextInt(targets.size()));
					target=killer.get_target(1);
				}
				else
				{
					System.out.println();
					killer=input_target("��ѡ��赶��:",players,targets);
					target=killer.input_target("��ѡ��ɱ��:", 1);
					
				}
				System.out.println(">> "+p.name+" �� "+killer.name+" �ĵ�ɱ "+target.name);
				if (killer.wuxie(target.name,showname(),true))  {System.out.println(">> "+killer.name+" �ڡ��赶ɱ�ˡ���Ч����������");break;}
				c=killer.search_kill();
				if (c==null) 
				{ 
					System.out.print(">> "+killer.name+" ������ɱ\n>> "+p.name+" ����� "+killer.name+"�� ");
					killer.weapon.println();
					p.cards.add(killer.weapon);
					killer.weapon=null;
					f.update(killer);
				}
				else
				if (killer.bot)
				{
					killer.kill_target=target;
					c.use(killer, sc);
				}
				else
				{				
					System.out.println("�Ƿ��ɱ?");
					killer.show(false);
					do{index=killer.input("",killer.cards.size());}while (index>=0&&!killer.cards.get(index).is_kill());
					if (index<0) 
					{
						System.out.print(">> "+killer.name+" ������ɱ\n"+p.name+" ����� "+killer.name+"�� ");
						killer.weapon.println();
						p.cards.add(killer.weapon);
						killer.weapon=null;
						f.update(killer);
					}
					else
					{
						killer.kill_target=target;
						c.use(killer,  sc);
					}
				}
				break;
				
			case "�ֲ�˼��":
				if (p.bot)
					target=p.get_target(2);
				else
				{
					System.out.println();
					target=input_target("��ѡ��Ŀ��:",players);
					while(target.pan_le!=null)
						target=input_target("��ѡ��Ŀ��:",players);
				}
				target.pan_le=this;
				f.update(target);
				break;
				
			case "�������":
				if (p.bot)
					target=p.get_target(0);
				else
				{
					System.out.println();
					target=input_target("��ѡ��Ŀ��:",players);
					while(target.pan_duan!=null||!p.in_dist(target,false))
						target=input_target("��ѡ��Ŀ��:",players);
				}
				target.pan_duan=this;
				f.update(target);
				break;

			case "����":
				System.out.println();
				p.pan_shan=this;
				f.update(p);
				break;

		}
		drop(p,0); 
		f.update(p);
	}
}
