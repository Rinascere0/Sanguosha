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
		System.out.print(">> "+p.name+" 使用了"+showname());
		if (skill!=null) System.out.print("("+skill+")");
		print_origin();
		if (p.name.equals("黄月英")&&!name.equals("闪电")&&!name.equals("乐不思蜀")&&!name.equals("兵粮寸断")) 
		{
			System.out.println("\n>> 黄月英 发动【集智】，摸起一张牌");
			p.cards.add(all_cards.get(0));
			all_cards.remove(0);
		}
		Thread.currentThread().sleep(300);
		switch (name)
		{
			case "无中生有":
				System.out.println();
				if (p.wuxie(p.name,showname(),false)) {System.out.println(">> "+p.name+" 于【无中生有】的效果被抵消了");break;}
				System.out.println(">> "+p.name+ " 获得两张手牌");
				p.draw_card();
				p.draw_card();
				break;	

				
			case "过河拆桥":
				if (p.bot)
				{
					target=p.get_target(2);
					c=get_card(target);
				}
				else
				{
					System.out.println();
					target=input_target("请选择目标:",players);
					while(target.empty())
						target=input_target("请选择目标:",players);
					c=p.input_card(target,0);
				}
				if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" 于【过河拆桥】 的效果被抵消了");break;}
				System.out.print(">> "+p.name+" 拆掉了 "+target.name+" 的");
				c.println();
				if (c.name.equals("白银狮子")&&c.equip!=null) target.heal(1);
				all_cards.add(c);
				target.cards.remove(c);
				c.equip=null;
				f.update(target);
				break;

			case "顺手牵羊":
				if (p.bot)
				{
					target=p.get_target(0);
					c=get_card(target);
				}
				else
				{
					System.out.println();
					target=input_target("请选择目标:",players);
					while(target.empty()||!p.in_dist(target, false))
						target=input_target("请选择目标:",players);
					c=p.input_card(target,0);
				}
				if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" 于【顺手牵羊】的效果被抵消了");break;}
				System.out.print(">> "+p.name+" 顺走了 "+target.name+" 的");
				if (c.equip!=null)
					c.println();
				else
					System.out.println("一张牌");
				if (c.name.equals("白银狮子")&&c.equip!=null) target.heal(1);
				target.cards.remove(c);
				c.equip=null;
				p.cards.add(c);
				f.update(target);
				f.update(p);
				break;
			
			case "决斗":				
				if (p.bot)
					if (color%2==0)
						target=p.get_target(6);
					else
						target=p.get_target(4);
				else
				{
					System.out.println();
					if (color%2==0)
						target=p.input_target("请选择目标:",10);
					else
						target=p.input_target("请选择目标:",2);
				}
				
				if (p.name.equals("孙策"))
				{
					System.out.println(">> 孙策 发动【激昂】，摸起一张牌");
					p.draw_card();
				}
				
				if (target.name.equals("孙策"))
				{
					System.out.println(">> 孙策 发动【激昂】，摸起一张牌");
					target.draw_card();
				}					
							
				if (target.wuxie(target.name,showname(), true)) {System.out.println(">> "+target.name+" 于【决斗】的效果被抵消了");break;}
				b=true;
				if (p.name.equals("吕布")||target.name.equals("吕布")) System.out.println("吕布 发动【无双】");
				while(true)
				{
					if (b)
					{
						if (this.name.equals("吕布"))
						{
							if (target.bot&&target.search_killnum()<2)
							{
								target.damage(1, 0,p,origin);
								break;
							}
							else
							{
								if (target.react_kill()&&target.react_kill())  //无双
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
						if (target.name.equals("吕布"))
						{
							if (p.bot&&p.search_killnum()<2)
							{
								p.damage(1, 0,target,origin);
								break;
							}
							else
							{
								if (p.react_kill()&&p.react_kill())  //无双
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
			
			case "桃园结义":
				System.out.println();
				b=true;
				for (int i=p.no;i!=p.no||b;i=(i+1)%players.size())
				{
					b=false;
					if (players.get(i).hp<players.get(i).maxhp)
						if (players.get(i).wuxie( players.get(i).name,showname(),false)) 
							System.out.println(">> "+players.get(i).name+" 于【桃园结义】的效果被抵消了");
						else
							players.get(i).heal(1);
				}
				break;
				
			case "五谷丰登":
				System.out.println("\n【五谷牌】");
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
					if (players.get(i).wuxie(target.name,showname(),false)) {System.out.println(">> "+players.get(i).name+" 于【五谷丰登】的效果被抵消了");continue;}
					b=false;
					if (target.bot)
						index=id.get(new Random().nextInt(id.size()));
					else
						do
						{index=target.input("请选择五谷牌:",players.size());}
						while(id.indexOf(index)==-1);
					System.out.print(">> "+target.name+" 选取了");
					temp.get(index).print();
					System.out.println("("+index+")");
					target.cards.add(temp.get(index));		
					id.remove(id.indexOf(index));
				}
				break;
			
			case "南蛮入侵":
				System.out.println();
				for (int i=(p.no+1)%players.size();i!=p.no;i=(i+1)%players.size())
				{
					target=players.get(i);
					if (target.armor!=null&&target.armor.name=="藤甲")
					{
							System.out.println(">> "+target.name+"藤甲抵挡了南蛮入侵");
							continue;
					}
					if (target.name.equals("贾诩")&&color%2==0)
					{
						System.out.println(">> 贾诩发动【帷幕】，不为南蛮入侵的目标");
						continue;
					}
					if (target.name.equals("孟获"))
					{
						System.out.println(">> 孟获 发动【祸首】，南蛮入侵无效");
						continue;
					}
					if (target.name.equals("祝融"))
					{
						System.out.println(">> 祝融发动【巨象】， 南蛮入侵无效");
						continue;
					}
					if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" 于【南蛮入侵】的效果被抵消了");continue;}
					if (!target.react_kill()) 
					{
						if (p.search_player("孟获")==null)
							target.damage(1,0,p,origin);
						else
							target.damage(1,0,p.search_player("孟获"),origin);		
					}
				}
				if (stat&&p.search_player("祝融")!=null)
				{
					System.out.println(">> 祝融发动【巨像】，收了这张【南蛮入侵】"+get_printname());
					p.fetch(p.search_player("祝融").cards,this);
					set_stat();
				}
				break;
				
			case "万箭齐发":
				System.out.println();
				for (int i=(p.no+1)%players.size();i!=p.no;i=(i+1)%players.size())
				{
					target=players.get(i);
					if (target.armor!=null)
					{
						if (target.armor.name.equals("藤甲"))
						{
							System.out.println(">> "+target.name+"的藤甲抵挡了万箭齐发");
							continue;
						}
						if (target.armor.name.equals("八卦阵")&&target.test("八卦阵"))
							continue;
						if (target.name.equals("卧龙")&&target.armor==null)
						{
							System.out.println(">> 卧龙 发动【八阵】");
							if(target.test("八卦阵")) continue;
						}
					}
					if (target.wuxie(target.name,showname(), true)) {System.out.println(">> "+target.name+" 于【南蛮入侵】的效果被抵消了");continue;}
					if (!target.react("闪",p)) target.damage(1,0,p,origin);			
				}
				break;
			
			case "铁索连环":		
				if (p.bot)		
					index=(new Random(1)).nextInt()%2;	
				else
				{
					System.out.println();
					index=p.input("0.重铸\n1.连环",2);
				}
				if (index==0)
				{
					if (p.bot) System.out.println();
					p.draw_card();
					System.out.println(">> "+p.name+" 重铸了"+showname());
				}
				else 
				{
					if (p.bot)
						target=p.get_target(5);	
					else
						target=p.input_target("请选择目标:", 9);
					if (!target.wuxie(target.name,showname(),true)) 
					{
						if (target.link)
						{
							System.out.println(">> "+p.name+" 对 "+target.name+" 解除了连环状态");
							target.link=false;
						}
						else
						{
							System.out.println(">> "+p.name+" 对 "+target.name+" 施加了连环状态");
							target.link=true;
						}
						f.update(target);
					}
					else
					{
						System.out.println(">> "+target.name+" 于【铁索连环】的效果被抵消了");
					}
					if (p.bot)
					{
						if (new Random().nextBoolean()) 
						{
							System.out.print(">> "+p.name+" 使用了"+showname());
							target=target.get_target(6);		
						}
					}
					else
					{
						target=target.input_target("再选择一个目标吗？",10);						
					}
					if (target!=null)
					{
						if (target.wuxie(target.name,showname(),true)) {System.out.println(">> "+target.name+" 于【铁索连环】的效果被抵消了");break;}
						if (target.link)
						{
							System.out.println(">> "+p.name+" 对 "+target.name+" 解除了连环状态");
							target.link=false;
						}
						else
						{
							System.out.println(">> "+p.name+" 对 "+target.name+" 施加了连环状态");
							target.link=true;
						}
						f.update(target);
					}
				
						
					
				}
				break;
			
			case "火攻":
			{
				int d;
				if (p.bot)
				{
					target=p.get_target(3);		
					if (target.wuxie( target.name,showname(),true))  {System.out.println(">> "+target.name+" 于【火攻】的效果被抵消了");break;}
					if (target.bot)
						c=target.cards.get(new Random().nextInt(target.cards.size()));
					else
					{
						target.show(false);
						c=target.cards.get(target.input("请展示一张手牌:",target.cards.size()));
					}
					System.out.print(">> "+target.name+" 展示了"+c.show_color());
					c.println();
					if (p.search(c.color,p.cards)==null)
						System.out.println(">> "+p.name+" 放弃!");
					else
					{
						System.out.print(">> "+p.name+" 丢弃了"+c.show_color());
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
					{target=p.input_target("请选择目标:",6);}while(target.cards.isEmpty());
					if (target.wuxie(target.name,showname(), true))  {System.out.println(">> "+target.name+" 于【火攻】的效果被抵消了");break;}
					if (target.bot)
						c=target.cards.get(new Random().nextInt(target.cards.size()));
					else
						c=target.cards.get(target.input("请展示一张手牌:",target.cards.size()));
					System.out.print(">> "+target.name+" 展示了"+c.show_color());
					c.println();
					if (p.search(c.color,p.cards)==null)
						System.out.println(">> "+p.name+" 放弃!");
					else
					{
						p.show(false);
						do{d=p.input("请弃一张相同花色的牌放火攻:",p.cards.size());}while(d>=0&&p.cards.get(d).color!=c.color);
						if (d==-1)
							System.out.println(">> "+p.name+" 放弃!");
						else
						{
							c=p.cards.get(d);
							System.out.print(">> "+p.name+" 丢弃了"+c.show_color());
							c.println();
							c.drop(p,1);
							p.cards.remove(c);
							target.damage(1, 1,p,origin);
						}	
					}
				}
			}
			break;
				
			case "借刀杀人":
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
					killer=input_target("请选择借刀者:",players,targets);
					target=killer.input_target("请选择被杀者:", 1);
					
				}
				System.out.println(">> "+p.name+" 借 "+killer.name+" 的刀杀 "+target.name);
				if (killer.wuxie(target.name,showname(),true))  {System.out.println(">> "+killer.name+" 于【借刀杀人】的效果被抵消了");break;}
				c=killer.search_kill();
				if (c==null) 
				{ 
					System.out.print(">> "+killer.name+" 放弃出杀\n>> "+p.name+" 获得了 "+killer.name+"的 ");
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
					System.out.println("是否出杀?");
					killer.show(false);
					do{index=killer.input("",killer.cards.size());}while (index>=0&&!killer.cards.get(index).is_kill());
					if (index<0) 
					{
						System.out.print(">> "+killer.name+" 放弃出杀\n"+p.name+" 获得了 "+killer.name+"的 ");
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
				
			case "乐不思蜀":
				if (p.bot)
					target=p.get_target(2);
				else
				{
					System.out.println();
					target=input_target("请选择目标:",players);
					while(target.pan_le!=null)
						target=input_target("请选择目标:",players);
				}
				target.pan_le=this;
				f.update(target);
				break;
				
			case "兵粮寸断":
				if (p.bot)
					target=p.get_target(0);
				else
				{
					System.out.println();
					target=input_target("请选择目标:",players);
					while(target.pan_duan!=null||!p.in_dist(target,false))
						target=input_target("请选择目标:",players);
				}
				target.pan_duan=this;
				f.update(target);
				break;

			case "闪电":
				System.out.println();
				p.pan_shan=this;
				f.update(p);
				break;

		}
		drop(p,0); 
		f.update(p);
	}
}
