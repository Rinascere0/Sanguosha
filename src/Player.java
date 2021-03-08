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
		if (name.equals("周泰")) buqu=new ArrayList<Card>();
		switch(name)
		{
			case "邓艾":extra_name="田";break;
			case "钟会":extra_name="权";break;
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
			System.out.println(">> "+name+" 回复了"+String.valueOf(value)+"点体力");
			if (name.equals("周泰"))
			{
				for (int i=0;i<value;i++)
				{
					System.out.print("周泰丢弃不屈牌");
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
		System.out.println(">> "+name+" 进入濒死状态");
		if (players.get(f.current).name.equals("贾诩")) 
		{
			System.out.println(">> 贾诩发动【完杀】");
			wansha=true;
		}
		for (int i=f.current;i!=f.current||b;i=(i+1)%players.size())
		{
			b=false;
			if (hp<=0&&i==no&&name.equals("周泰"))
			{
				for (int j=0;j<1-buqu.size()-hp;)
				{
					t=true;
					if (bot||!bot&&input("是否发动【不屈】？",2)==1)
					{
						c=all_cards.get(0);
						all_cards.remove(0);
						System.out.print(">> 周泰发动【不屈】，不屈牌为");
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
							System.out.println(")\n>> 周泰【不屈】发动失败");
							if (!react("桃",this)&&!react("酒",this)) break;
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
			if (wansha&&!players.get(i).name.equals("贾诩")&&i!=no)
					System.out.println(">> "+players.get(i).name+"无法出桃");
			else
			{
				while(hp<=0&&(!bot||bot&&(i==no))&&(players.get(i).react("桃",this)||(i==no)&&players.get(i).react("酒",this))) heal(1);
				if (hp>0) break; else System.out.println(">> "+players.get(i).name+" 放弃");
			}

		}
		if (hp<=0) 
		{
			
				if (name.equals("庞统")&&limit)
				{
					drop_all(all_cards);
					System.out.println(">> 庞统发动【涅槃】，回复三点体力并摸三张牌");
					draw_card();
					draw_card();
					draw_card();
					hp=Integer.min(3, maxhp);
					limit=false;
				}
				else
				{
					alive=false;
					System.out.println(">> "+name+" 死亡orz");
					f.update(this);
					if (search_player("曹丕")!=null)
					{
						System.out.println(">> 曹丕发动【行殇】，获得其所有牌");
						drop_all(search_player("曹丕").cards);
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
		System.out.println(">> "+name+" 流失了"+String.valueOf(value)+"点体力");
		if (hp<1) death();
		if (name.equals("张春华")&&cards.size()<maxhp-hp)
		{
			System.out.println(">> 张春华 发动【伤逝】，摸起"+String.valueOf(maxhp-hp)+"张牌");
			for (int i=0;i<maxhp-hp;i++)
				draw_card();
		}
		f.update(this);
	}
	
	void show(boolean b)
	{
		System.out.println("【手牌】");
		for (int i=0;i<cards.size();i++)
		{
			System.out.print("    "+String.valueOf(i)+'.');
			cards.get(i).show();
			if (b&&aval.get(i)==true) System.out.println('*'); else System.out.println();
		}
	}
	
	void show_skill()
	{
		System.out.print("【技能】\n    a."+skill);
		if (stype==3&&limit||stype==1&&once||name.equals("曹植")&&!under) System.out.println("*"); else System.out.println();
	}

	void show_extra()
	{
		if (!extra.isEmpty())
		{
			System.out.println("【"+extra_name+"】");
			for (int i=0;i<extra.size();i++)
				System.out.println("    "+String.valueOf(i)+"."+extra.get(i).get_name());			
		}
	}
	
	void damage(int value,int type,Player source,Card source_card) throws Exception
	{
		Card c,d;
		int index;
		Player target;
		if (source.name.equals("张春华"))
		{
			System.out.println(">> 张春华 发动【绝情】");
			loss(value);
			return;
		}
		source.harm=true;
		if (armor!=null)
			if (get_armor("白银狮子"))
			{
				if (value>1)System.out.println(">> "+name+" 白银狮子勇抵挡，伤害为1");
				value=1;
			}
			if (get_armor("藤甲")&&type==1)
			{
				System.out.println(">> "+name+ "被火烧藤甲，伤害+1");
				value+=1;
			}
		hp-=value;
		System.out.print(">> "+name+" 受到了"+String.valueOf(value)+"点");
		switch(type)
		{
			case 0:System.out.print("伤害");break;
			case 1:System.out.print("火焰伤害");break;
			case 2:System.out.print("雷电伤害");
		}
		if (source!=null) System.out.println("，来源为"+source.name); else System.out.println("，无伤害来源");
		if (hp<1) death();
		f.update(this);
		
		if (name.equals("张春华")&&cards.size()<maxhp-hp)
		{
			System.out.println(">> 张春华 发动【伤逝】，摸起"+String.valueOf(maxhp-hp)+"张牌");
			for (int i=0;i<maxhp-hp;i++)
				draw_card();
		}
		
		if (source.name.equals("界徐庶")&&source.hp<source.maxhp&&!source.awake)
		{
			source.awake=true;
			System.out.println(">> 界徐庶 发动【潜心】，减一点体力上限，并获得【荐言】");
			source.maxloss();
			source.skill="荐言";
			source.stype=1;
		}
		
		if (source.name.equals("魏延")&&source.hp<source.maxhp&&source.in_dist(this, false))
		{
			System.out.println(">> 魏延发动【狂骨】，回血"+String.valueOf(Integer.min(maxhp, hp+value)-hp)+"点");
			hp=Integer.min(maxhp, hp+value);
		}
		
		if (name.equals("钟会"))
		{
			for (int i=0;i<value;i++)
				if (bot||input("是否发动【权计】",2)==1) 
				{
					cards.add(all_cards.get(0));
					all_cards.remove(0);
					show(false);
					if (bot)
						c=random_card();
					else
						c=hand_card("请选择作为【权】的牌:",cards);
					cards.remove(c);
					extra.add(c);
					System.out.println(">> 钟会 摸起一张牌，并将一张手牌作为【权】");
				}				
		}
		
		if (name.equals("曹丕"))
		{
			if (bot&&new Random().nextInt(maxhp-hp+1)==0||!bot&&input("是否发动【放逐】",2)==1)
			{
				System.out.print(">> 曹丕 发动了【放逐】");
				if (bot)
					target=get_target(2);
				else
					target=input_target("请选择目标:",2);
				System.out.println(">> "+target.name+" 翻面，补"+String.valueOf(maxhp-hp)+"张牌");
				for (int i=0;i<maxhp-hp;i++)
					target.draw_card();
				target.under=!target.under;
			}
		}
		
		if (name.equals("荀彧"))
		{
			for (int i=0;i<value;i++)
			{
				if (bot&&cards.size()<maxhp||!bot&&input("是否发动【节命】:",2)==1)
				{
					if (bot)
					{
						System.out.println("荀彧 发动节命，令荀彧将手牌补至上限");
						for (int j=0;j<maxhp-cards.size();j++)
							draw_card();
					}
					else
					{
						do
						{target=players.get(input("请选择目标:",players.size()));}while(target.cards.size()>=Integer.min(target.maxhp,5));
						System.out.println("荀彧 发动节命，令"+target.name+"将手牌补至上限");
						for (int j=0;j<target.maxhp-target.cards.size();j++)
							target.draw_card();		
						f.update(target);
					}
				}
			}
		}
		
		if (name.equals("神曹操"))
		{
			for (int i=0;i<value;i++)
			{
				if (bot||input("是否发动【归心】",2)==1)
				{
					System.out.print(">> 神曹操 发动【归心】，将武将牌");	
					if (under) System.out.print("正"); else System.out.print("反");
					under=!under;
					System.out.println("面朝上");
					for (int j=(no+1)%players.size();j!=no;j=(j+1)%players.size())
					{
						if (bot)
							c=players.get(j).get_card();
						else
						{
							System.out.print("目标是"+players.get(j).name+"，");
							c=input_card(players.get(j),0);
						}
						cards.add(c);
						System.out.print(">> 神曹操获取了"+players.get(j).name+"的");
						if (c.equip==null) System.out.println("一张手牌"); else System.out.println(c.equip+c.get_printname());
						c.equip=null;
						f.update(this);
						f.update(players.get(j));
					}
				}
			}
		}
		
		if (name.equals("郭嘉"))
		{
			for (int i=0;i<value;i++)
			{
				System.out.println(">> 郭嘉 发动【遗计】，获得两张牌");
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
					index=input("将"+c.get_printname()+"交给谁：",players.size());
					if (index>=0) 
					{
						players.get(index).cards.add(c);
						System.out.println(">> 郭嘉 将一张牌交给了"+players.get(index).name);
					}
					else
						cards.add(c);
					index=input("将"+d.get_printname()+"交给谁：",players.size());
					if (index>=0) 
					{
						players.get(index).cards.add(d);
						System.out.println(">> 郭嘉 将一张牌交给了"+players.get(index).name);
					}
					else
						cards.add(d);
				}
			}
		}
		
		if (name.equals("夏侯惇"))
		{
			if (bot||input("是否发动【刚烈】",2)==1)
			{
				if (test("刚烈"))
				{
					if (source.cards.size()<2)
						source.loss(1);
					else
					{
						if (!source.bot)
							show(false);
						if (source.bot&&hp>1&&new Random().nextBoolean()||input("请选择:0.弃两张牌 1.失去一点体力",2)==1)
							source.loss(1);
						else
						{
							//弃两张牌
							
						}
					}
				}						
			}	
		}
		
		if (name.equals("司马懿"))
		{
			c=null;
			if (bot)
				c=source.get_card();
			else if (input("是否发动反馈:",2)==1)
				c=input_card(source,0);
			if (c!=null)
			{
					c.drop(source,2);
					cards.add(c);
					f.update(source);
					f.update(this);
					System.out.println(">> 司马懿 发动【反馈】，获得了"+source.name+"的"+c.get_equip()+c.get_printname());
					c.equip=null;
				
			}
		}
		
		if (name.equals("曹操"))
		{
			if (source_card!=null&&(bot||!bot&&input("是否发动【奸雄】？",2)==1))
			{
				System.out.print(">> 曹操 发动【奸雄】，获得伤害牌");
				fetch(cards,source_card);
				source_card.println();
				source_card.set_stat();
			}
		}
		
		if (name.equals("曹植")&&under&&(bot||input("是否发动【酒诗】:",2)==1))
		{	
			System.out.println(">> 曹植 发动【酒诗】，翻回正面");
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
			if (s.equals("闪"))
			{
				if (name.equals("甄姬")&&cards.get(i).color%2==0) return new Card(cards.get(i),s,skill); 
				if (name.equals("赵云")&&cards.get(i).is_kill()) return new Card(cards.get(i),s,skill); 
			}
			if (s.equals("无懈可击")&&name.equals("卧龙")&&cards.get(i).color%2==0) return new Card(cards.get(i),s,skill); 
			if (s.equals("酒")&&name.equals("董卓")&&cards.get(i).color==0) return new Card(cards.get(i),s,skill); 
			if (s.equals("桃")&&name.equals("华佗")&&cards.get(i).color%2==1) return new Card(cards.get(i),s,skill); 
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
			if (cards.get(i).name.equals("杀")||cards.get(i).name.equals("雷杀")||cards.get(i).name.equals("火杀")) sum++;
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
				if ((name.equals("关羽")||name.equals("关兴张苞")&&active)&&(cards.get(i).color%2==1)) return new Card(cards.get(i),"杀","武圣");
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
		System.out.println("武器:"+get_name(weapon));
		System.out.println("防具:"+get_name(armor));
		System.out.println("-1马:"+get_name(atk));
		System.out.println("+1马:"+get_name(def));
	}
	
	void show_all(boolean b)
	{
		if (b) show(false); else System.out.println("手牌:"+String.valueOf(cards.size()));
		show_equip();
	}

	int kill_dist()
	{
		if (weapon==null) return 1;else return weapon.dist;
	}
	
	int atk_dist()
	{
		int i=0;
		if (name.equals("马超")||name.equals("马岱")||name.equals("庞德")||name.equals("公孙瓒")&&hp>2) i=1;
		if (name.equals("邓艾")) i=extra.size();
		if (atk==null) return 1+i;else return 2+i;
	}
	
	int def_dist()
	{
		int i=0;
		if (name.equals("神曹操")||name.equals("公孙瓒")&&hp<=2) i=1;
		if (def==null) return i; else return i+1;
	}
	
	boolean in_dist(Player target,boolean b)
	{
		if (b) 
			if (dist_buff==-2||dist_buff==target.no) return true;
			else return Integer.min(Math.abs(target.no-no),Math.abs(target.no+no)-players.size())<=kill_dist()+atk_dist()-target.def_dist()-1;
		else 
			if (name.equals("黄月英")) return true;
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
				if ((is_kill(c)||c.name.equals("决斗"))&&(players.get(j).name.equals("诸葛亮")&&players.get(j).cards.isEmpty()))
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
					if (c.name.equals("桃"))
					{
						if (hp<maxhp)
							f=true;
						else
							f=false;
					}
					else if (c.name.equals("酒"))
						f=true;
					else if (!c.name.equals("闪"))
						f=has_target(players,true)&&kill<maxkill;
					break;
					
				case 1:
					if (c.name.equals("顺手牵羊")||c.name.equals("兵粮寸断"))
						f=has_target(players,false);
					else if (c.name.equals("借刀杀人"))		
					{
						for (int j=(no+1)%players.size();j!=no;j=(j+1)%players.size())
							if (players.get(j).weapon!=null)
							{
								f=true;
								break;
							}
					}
					else if (c.name.equals("闪电")&&pan_shan!=null) 
						f=false;
					else if (!c.name.equals("无懈可击"))
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
				System.out.print(">> "+name+" 打出了【杀】");
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
				System.out.println("请出【杀】:");
				if ((name.equals("关羽")||name.equals("关兴张苞")&&active)&&input("是否发动【武圣】？",2)==1) {trans=0;System.out.print("请打出手牌：");}
				int index=sc.nextInt();
				if (index<0)
					return false;
				c=cards.get(index);
				if (trans==0)
					c=transform(c,"杀",trans);
				else if (c.is_kill())
				{
					use_kill=true;
					System.out.print(">> "+name+"打出了【杀】");
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
			case 0:if (c.color%2==1) return new Card(c,s,skill);break; //武圣
			case 1:if (c.is_kill()) return new Card(c,s,skill);break; //龙胆
			case 2:if (c.color%2==0) return new Card(c,s,"倾国");break; //倾国
			case 3:if (no!=f.current&&c.color%2==1) return new Card(c,s,"急救");break; //急救
			case 4:if (c.color%2==0) return new Card(c,s,"看破");break; //看破
			case 5:if (c.color==1) return new Card(c,s,skill);break; //酒池
		}
		return c;
	}
	
	boolean react(String s,Player source) throws Exception
	{
		String str=null;
		Player target;
		if (s.equals("酒")&&name.equals("曹植")&&!under)
		{
			if (bot||input("是否发动【酒诗】",2)==1)
			{
				System.out.println("曹植 发动【酒诗】，将武将牌翻面，视为使用一张【酒】");
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
					System.out.print(">> "+name+" 打出了"+c.showname());
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
				System.out.println("请出【"+c.name+"】:");
				if (s.equals("闪"))
				{
					if ((name.equals("SP赵云")||name.equals("赵云"))&&input("是否发动【龙胆】？",2)==1) trans=1;
					if (name.equals("甄姬")&&input("是否发动【倾国】？",2)==1) trans=2;
				}
				if (s.equals("桃")&&name.equals("华佗")&&input("是否发动【急救】？",2)==1) trans=3;
				if (s.equals("无懈可击")&&name.equals("卧龙")&&input("是否发动【看破】？",2)==1) trans=4;
				if (s.equals("酒")&&name.equals("董卓")&&input("是否发动【酒池】？",2)==1) trans=5;
				if (trans>0) System.out.print("请打出手牌：");
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
						case 1:str="龙胆";break;
						case 2:str="倾国";break;
						case 3:str="急救";break;
						case 4:str="看破";break;
						case 5:str="酒池";break;
					}
					if (str!=null)  str="发动【"+str+"】,"; else str="";
					System.out.print(str+" 打出了"+c.showname());
					if (c.skill!=null)  System.out.print("("+c.skill+")");
					c.origin.println();
					cards.remove(index);
					c.drop(this,0);			
					f.update(this);
					
					if (name.equals("张角")&&s.equals("闪")&&(bot||input("是否发动【雷击】:",2)==1))
					{
						if (bot)
						{
							
						}
						else
						{
							target=input_target("请选择目标:",2);
							if (test("雷击")) target.damage(2, 2, this,null);
						}
					}
					if (name.equals("SP赵云")&&trans==1)
					{
						if (!source.cards.isEmpty()&&(bot||input("是否发动【冲阵】:",2)==1))
						{			
							System.out.println(">> SP赵云 发动【冲阵】，获得"+source.name+"一张手牌");
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
		System.out.println(">> "+name+" 回合开始阶段");
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
		if (name.equals("夏侯渊"))
		{
			if (bot&&(pan_shan!=null||pan_le!=null||pan_duan!=null)||!bot&&input("是否发动【神速1】？",2)==1)
			{
				System.out.println(">> 夏侯渊发动【神速】，跳过判定阶段与摸牌阶段");
				skip_judge=true;
				skip_draw=true;
				dist_buff=-2;
				new Basic("杀",f,players,all_cards).use(this,sc);
				dist_buff=-1;
			}
		}
				
		if (name.equals("邓艾")&&extra.size()>=1&&!awake)
		{
			awake=true;
			System.out.println(">> 邓艾 发动觉醒技【凿险】，减一点体力上限，获得【急袭】");
			skill="急袭";
			stype=2;
			maxloss();
		}
		
		if (name.equals("钟会")&&extra.size()>=1&&!awake)
		{		
			System.out.println(">> 钟会 发动觉醒技【自立】");
			awake=true;
			if (bot&&hp>=2||!bot&&input("请选择:0.回复一点体力 1.摸两张牌",2)==1)
			{
				draw_card();
				draw_card();
				System.out.print(">> 钟会 摸两张牌");
			}
			else
			{
				heal(1);
				System.out.println(">> 钟会 回复一点体力");
			}
			System.out.println("，减一点体力上限，获得【排异】");	
			skill="排异";
			stype=1;
			maxloss();
			f.update(this);
		}
		
		if (name.equals("姜维")&&cards.isEmpty()&&!awake)
		{
			System.out.println(">> 姜维 发动觉醒技【志继】");
			awake=true;
			if (bot&&hp>=2||!bot&&input("请选择:0.回复一点体力 1.摸两张牌",2)==1)
			{
				draw_card();
				draw_card();
				System.out.print(">> 姜维 摸两张牌");
			}
			else
			{
				heal(1);
				System.out.println(">> 姜维 回复一点体力");
			}
			System.out.println("，减一点体力上限，获得【观星】");	
			maxloss();
			hp=Integer.min(hp,maxhp);		
			f.update(this);
		}
		
		if (name.equals("孙策")&&hp==1&&!awake)
		{
			System.out.println(">> 孙策 发动觉醒技【魂姿】，减一点体力上限，获得【英魂】【英姿】");
			maxloss();			
			awake=true;
			f.update(this);
		}
		
		if ((name.equals("孙坚")||name.equals("孙策")&&awake)&&hp<maxhp)
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
				if (input("是否发动英魂？",2)==1)
				{
					index=input_other("请选择目标：",players.size());
					target=players.get(index);
					type=input("0.摸"+String.valueOf(maxhp-hp)+"弃"+String.valueOf(1)+"\n1.摸"+String.valueOf(1)+"弃"+String.valueOf(maxhp-hp),2);
					if (type==0) type=maxhp-hp;
					System.out.println(">> "+name+"发动了【英魂】，让"+target.name+"摸"+String.valueOf(type)+"张牌弃"+String.valueOf(maxhp-hp+1-type));
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
		
		if (name.equals("诸葛亮")||name.equals("姜维")&&awake)
		{
			if (bot||input("是否发动【观星】",2)==1)
			{
				System.out.println(">> "+name+" 发动了【观星】");
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
					System.out.println("【观星牌】");
					for (int i=0;i<star.size();i++)
						System.out.println(String.valueOf(i)+"."+star.get(i).get_printname());
					System.out.println("请选择至于牌堆顶的牌:");
					list=input_cards(star);
					for (int i=0;i<list.size();i++)
						all_cards.add(0,list.get(i));
					star.removeAll(list);
					System.out.println("请选择至于牌堆底的牌:");
					list=input_cards(star.size(),star);
					for (Card d:star)
						all_cards.add(d);
					System.out.println(">> "+name+"将"+String.valueOf(players.size()-list.size())+"张牌置入牌堆顶");
					System.out.println(">> "+name+"将"+String.valueOf(list.size())+"张牌置入牌堆底");
				}
			}
		}
		
		if (name.equals("甄姬"))
		{
			if (bot||input("是否发动【洛神】？\n",2)==1)
				while (test("洛神"))
					if (bot||input("是否发动【洛神】？\n",2)==0) break;
		}
		Thread.currentThread().sleep(300);
	}
	
	void judge() throws Exception
	{
		Player p;
		Card c;
		if(name.equals("张郃"))
		{
			if (bot||input("是否发动【巧变】，跳过判定阶段:",2)==1)
			{
				show(false);
				c=hand_card("请选择弃置的牌:",cards);
				cards.remove(c);
				c.drop(this, 1);
				System.out.println(">> 张郃发动【巧变】，弃置"+c.get_printname()+"跳过判定阶段");
				return;
			}
		}
		
		System.out.println(">> "+name+" 判定阶段");
		if (pan_shan!=null)
		{
			if (wuxie(name,pan_shan.showname(),true))
			{
				System.out.println(">> "+name+" 跳过闪电判定");
				next(players).next(players).pan_shan=pan_shan;
				f.update(next(players).next(players));
			}
			else
			{	
				if (test("闪电")) 
					
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
				System.out.println(">> "+name+" 跳过了【兵粮寸断】的判定，可以摸牌");
			}
			else
			{
				if (test("兵粮寸断"))
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
				System.out.println(">> "+name+" 跳过了【乐不思蜀】的判定，可以出牌");
			}
			else
			{
				if (test("乐不思蜀")) 
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
		if (name.equals("张郃"))
		{
			if(bot||input("是否发动【巧变】，跳过摸牌阶段:",2)==1)
			{
				int index,i;
				Player target;
				Card c;
				show(false);
				c=hand_card("请选择弃置的牌:",cards);
				cards.remove(c);
				c.drop(this, 1);
				System.out.println(">> 张郃发动【巧变】，弃置"+c.get_printname()+"，跳过摸牌阶段");
				System.out.print("请选择一名目标:");
				do {index=sc.nextInt();}while(index<1||index>=players.size()||players.get(index).cards.isEmpty());
				target=players.get(index);
				c=target.cards.get(new Random().nextInt(target.cards.size()));
				cards.add(c);
				target.cards.remove(c);
				System.out.println(">> 张辽 获得了 "+target.name+"一张牌"+c.get_printname());
				f.update(target);
				System.out.print("再选择一名目标吗？");
				do {i=sc.nextInt();}while(i==0||i<-1||i>=players.size()||i==index||players.get(i).cards.isEmpty());
				if (i!=-1)
				{	
					target=players.get(i);
					c=target.cards.get(new Random().nextInt(target.cards.size()));
					cards.add(c);
					target.cards.remove(c);
					System.out.println(">> 张辽 获得了 "+target.name+"一张牌"+c.get_printname());
					f.update(target);
				}
				f.update(this);
				return;
			}
			
		}
		
		System.out.println(">> "+name+" 摸牌阶段");
		if (name.equals("孟获")&&hp<maxhp)
		{
			if (bot&&hp<maxhp-1||!bot&&input("是否发动【再起】？",2)==1)
			{
				System.out.println(">> 孟获发动【再起】");
				for (int i=0;i<maxhp-hp;i++)
					if (test("再起")) heal(1);
				return;
			}
		}
		
		if (name.equals("袁术"))
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
				System.out.println(">> 袁术发动【庸肆】，多摸"+String.valueOf(magic)+"张牌");				
		}
				
		if (name.equals("许褚"))
		{
			if (!bot&&input("是否发动【裸衣】？",2)==1||search_kill()!=null&&new Random().nextInt(cards.size())>1)
			{
				cards.add(all_cards.get(0));
				all_cards.remove(0);
				active=true;
				System.out.println(">> 许褚 发动【裸衣】，少摸一张牌");
				f.update(this);
				return;
			}
		}
		
		if (name.equals("关兴张苞"))
		{
			if (bot||input("是否发动【父魂】？",2)==1)
			{
				draw_card();
				draw_card();
				Card c=cards.get(cards.size()-1),d=cards.get(cards.size()-2);
				System.out.println(">> 关兴张苞 发动【父魂】，亮出"+c.get_printname()+","+d.get_printname());
				if (Math.abs((c.color-d.color)%2)==1) {
					System.out.println(">> 关兴张苞 获得【武圣】【咆哮】直到回合结束");
					active=true;
					skill="武圣";
					stype=0;
				}
				f.update(this);
				return;
			}
		}
		
		if (name.equals("颜良文丑"))
		{
			testcard=null;
			if (bot&&new Random().nextInt(cards.size())>=2||input("是否发动【双雄】？",2)==1)
			{
				test("双雄");
				return;
			}
		}
		
		
		
		if (name.equals("张辽"))
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
					System.out.println(">> 张辽 发动了【突袭】");
					target=random_player(ps);
					c=target.random_card();
					target.cards.remove(c);
					cards.add(c);
					System.out.println(">> 张辽 获得了 "+target.name+" 一张牌");
					target=random_player(ps);
					c=target.random_card();
					target.cards.remove(c);
					cards.add(c);
					System.out.println(">> 张辽 获得了 "+target.name+" 一张牌");
				}
			
			}
			else
			{
				System.out.print("是否发动【突袭】？");
				if (sc.nextInt()==1)
				{
					System.out.println(">> 张辽 发动了【突袭】");
					System.out.print("请选择一名目标:");
					do {index=sc.nextInt();}while(index<1||index>=players.size()||players.get(index).cards.isEmpty());
					target=players.get(index);
					c=target.cards.get(new Random().nextInt(target.cards.size()));
					cards.add(c);
					target.cards.remove(c);
					System.out.println(">> 张辽 获得了 "+target.name+" 一张牌");
					f.update(target);
					System.out.print("再选择一名目标吗？");
					do {i=sc.nextInt();}while(i==0||i<-1||i>=players.size()||i==index||players.get(i).cards.isEmpty());
					if (i!=-1)
					{	
						target=players.get(i);
						c=target.cards.get(new Random().nextInt(target.cards.size()));
						cards.add(c);
						target.cards.remove(c);
						System.out.println(">> 张辽 获得了 "+target.name+" 一张牌");
						f.update(target);
					}
					f.update(this);
					return;
				}
			}
		}
		draw_card();
		draw_card();
		if (name.equals("周瑜")||name.equals("孙策")&&awake)
		{
			System.out.println(">> 周瑜 发动【英姿】，多摸一张牌");
			draw_card();
		}
		if (name.equals("鲁肃"))
		{
			Player target;
			ArrayList<Integer>min_list=new ArrayList<Integer>();
			int min=100,index;
			if ((bot&&cards.size()<=3)||(!bot&&input("是否发动【好施】？",2)==1))
			{
				System.out.println(">> 鲁肃 发动【好施】，多摸两张牌");
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
						System.out.println("为什么我发动了？");
					}
					else
					{
						show(false);
						index=input("请选择一名手牌最少的目标：",min_list);
						target=players.get(index);
						System.out.print("请选择"+String.valueOf(cards.size()/2)+"张手牌交给"+target.name+"：");
						min_list.removeAll(min_list);
						for (int i=0;i<cards.size()/2;i++)
						{
							index=input_posi("",cards.size());
							target.cards.add(cards.get(index));
							if (min_list.contains(index)) i--;
						}
						System.out.print(">> 鲁肃 将"+String.valueOf(cards.size()/2)+"张手牌交给"+target.name);
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
					case "武圣":if (cards.get(i).color%2==1) cards.set(i, new Basic(cards.get(i),"杀",skill));break;
					case "连环":if (cards.get(i).color==2) cards.set(i, new Special(cards.get(i),"铁索连环",skill));break;
					case "奇袭":if (cards.get(i).color%2==0) cards.set(i, new Special(cards.get(i),"过河拆桥",skill));break;
					case "国色":if (cards.get(i).color==3) cards.set(i, new Special(cards.get(i),"乐不思蜀",skill));break;
					case "断粮":if (cards.get(i).color%2==0) cards.set(i, new Special(cards.get(i),"兵粮寸断",skill));break;
					case "火计":if (cards.get(i).color%2==1) cards.set(i, new Special(cards.get(i),"火攻",skill));break;
					case "龙胆":if (cards.get(i).name.equals("闪")) cards.set(i, new Basic(cards.get(i),"杀",skill));break;
					case "酒池":if (cards.get(i).color==0) cards.set(i, new Basic(cards.get(i),"酒",skill));break;
					case "双雄":if ((cards.get(i).color-testcard.color)%2==1) cards.set(i, new Special(cards.get(i),"决斗",skill));break;	
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
		if (name.equals("张飞")||name.equals("关兴张苞")&&active||get_weapon("诸葛连弩")) maxkill=100; else maxkill=1;
		if (name.equals("夏侯渊"))
		{
			if (has_equip()&&(bot&&new Random().nextBoolean()||!bot&&has_equip()&&input("是否发动【神速】，跳过出牌阶段？",2)==1))
			{
				System.out.println(">> 夏侯渊 发动了【神速】，跳过出牌阶段");
				if (bot)
					c=drop_equip();
				else
					c=input_equip(this);		
				System.out.println(">> 夏侯渊 丢弃了"+c.get_printname());
				c.drop(this,1);
				dist_buff=-2;
				new Basic("杀",f,players,all_cards).use(this,sc);

				dist_buff=-1;
				return;
			}
		}
		if (name.equals("张郃"))
		{
			if (bot)
			{
				
			}
			else
			{
				if (input("是否发动【巧变】，跳过出牌阶段:",2)==1)
				{
					c=hand_card("请选择弃置的牌:",cards);
					cards.remove(c);
					c.drop(this, 1);
					System.out.println(">>张郃 发动【巧变】，弃置"+c.get_printname()+"，跳过出牌阶段");
					target=input_target("请选择移动牌的目标:",6);
					c=input_card(target,2);
					do
					{
						p=target.input_target("请选择将"+c.get_printname()+"移给的目标",2);
						switch(c.type)
						{
							case 1:if (c.name.equals("兵粮寸断")&&p.pan_duan!=null) continue;
									if (c.name.equals("乐不思蜀")&&p.pan_le!=null) continue;
									if (c.name.equals("闪电")&&p.pan_shan!=null) continue;
									break;
							case 2:if (p.weapon!=null) continue; else p.weapon=(Weapon)c;
							case 3:if (p.armor!=null) continue; else p.armor=(Armor)c;
							case 4:if (p.def!=null) continue; else p.def=c;
							case 5:if (p.atk!=null) continue; else p.atk=c;
						}
					}while(false);
					System.out.println(">>张郃 将"+target.name+"的"+c.equip+c.get_printname()+"移给了"+p.extra_name);
					return;
				}
			}
		}
		System.out.println(">> "+name+" 出牌阶段");
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
					cur=input("请选择使用的手牌或技能：",cards.size());
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
							System.out.println(">> "+name+" 发动了【"+skill+"】");
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
		
		if (name.equals("吕蒙")&&!use_kill)
		{
			System.out.println(">> 吕蒙 发动【克己】，跳过弃牌阶段");
			return;
		}
		
		if(name.equals("张郃"))
		{
			if (bot||input("是否发动【巧变】4",2)==1)
			{
				c=hand_card("请选择弃置的牌:",cards);
				cards.remove(c);
				c.drop(this, 1);
				System.out.println(">> 张郃发动【巧变】，弃置"+c.get_printname()+"，跳过弃牌阶段");
				return;
			}
		}
		
		System.out.println(">> "+name+" 弃牌阶段");
		
		if (name.equals("钟会")) limit+=extra.size();
		if (cards.size()>limit)
		{
			limit=cards.size()-limit;
			drop_cards(limit);
		}
		else
			limit=0;
		if (name.equals("袁术")&&limit<magic)
		{
			System.out.println(">> 袁术 发动【庸肆】，需要额外弃"+String.valueOf(magic-limit)+"张牌");
			for (int i=0;i<magic-limit;i++)
				c=input_card(this,2);
		}
		Thread.currentThread().sleep(300);

	}
	
	void end(ArrayList<Card>all_cards) throws Exception {
		int index;
		boolean b;
		System.out.println(">> "+name+" 回合结束阶段");
		
		if (name.equals("董卓"))
		{
			for (Player p:players)
				if (p.hp<hp)
				{
					System.out.println(">> 董卓 发动【崩坏】");
					if (bot&&hp<maxhp||!bot&&input("请选择:0.减一点体力 1.减一点体力上限",2)==1)
					{
						System.out.println(">> 董卓 减一点体力上限");
						maxloss();
					}
					else
					{
						System.out.println(">> 董卓 减一点体力");
						loss(1);
					}

				}
		}
		if (name.equals("关兴张苞")&&active)
		{
			System.out.println(">> 关兴张苞 失去【武圣】【咆哮】");
			active=false;
			skill=null;
		}
		
		if (name.equals("神周瑜")&&drop_count>1)
		{
			if (bot)
				if (hp<2) index=new Random(2).nextInt(); else index=new Random(3).nextInt();
			else
				index=input_posi("是否发动琴音？\n0.不发动\n1.所有角色回复一点体力\n2.所有角色失去一点体力\n",3);
			if (index!=0) 
			{
				System.out.print(">> 神周瑜发动【琴音】，所有角色");
				b=true;
				if (index==1) 
				{
					System.out.println("回复一点体力");
					for (int i=no;i!=no||b;i=(i+1)%players.size())
					{
						b=false;
						players.get(i).heal(1);
					}
				}
				else
				{
					System.out.println("失去一点体力");
					for (int i=no;i!=no||b;i=(i+1)%players.size())
					{
						b=false;
						players.get(i).loss(1);
					}
				}
					
			}	
		}
		if (name.equals("貂蝉")) 
		{
			System.out.println(">> 貂蝉 发动【闭月】");
			cards.add(all_cards.get(0));
			all_cards.remove(0);
			f.update(this);
		}
		if (name.equals("曹仁"))
		{
			if (!under&&(bot&&new Random().nextInt(cards.size()+1)==0)||(!bot&&input("是否发动【拒守】",2)==1))
			{
				System.out.println(">> 曹仁 发动【拒守】，摸三张牌并翻面");
				draw_card();
				draw_card();
				draw_card();
				under=true;
				f.update(this);
			}
		}
		if (harm)
		{
			Player p=search_player("界徐庶");
			if (p!=null&&!p.equals(this))
			{
				if (p.bot)
				{
					
				}
				else if (p.search_kill()!=null) 
				{
					p.show(false);
					Card c=p.input_card("是否发动【诛害】，对"+name+"出杀");
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
			System.out.println(">> "+name+" 翻回正面，跳过本回合");
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
			System.out.println("你需弃置"+String.valueOf(count)+"张手牌");
			show(false);
			for (int i=0;i<cards.size()-hp;i++)
					index.add(cards.get(input_posi("",cards.size())));
		}
		System.out.print(">> "+name+" 弃置了");
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
				case 0:if (in_dist(players.get(i), false)) ids.add(i);break;  //顺
				case 1:if (in_dist(players.get(i), true)) ids.add(i);break;  //杀
				case 2:ids.add(i);break; //任何人
				case 3:if (!players.get(i).cards.isEmpty()) ids.add(i);break; //有手牌
				case 4:if (i!=no) ids.add(i);break;
				case 5:if (!players.get(i).name.equals("贾诩")) ids.add(i);break;
				case 6:if (!players.get(i).name.equals("贾诩")&&i!=no) ids.add(i);break;
			}
		
		Player p=players.get(ids.get(new Random().nextInt(ids.size())));
		System.out.println("，目标是"+p.name);
		return p;
	}
	
	boolean wuxie(String t_name,String s,boolean t) throws Exception
	{
		boolean b=true;
		for (int i=no;i!=no||b;i=(i+1)%players.size())
		{
			if ((players.get(i).bot&&!(b^t)||!players.get(i).bot))
			{
				if (!players.get(i).bot&&players.get(i).search("无懈可击")!=null)
					System.out.println(">> "+t_name+"于"+s+" 求【无懈可击】");
				if(players.get(i).react("无懈可击",this))
				{
					if (players.get(i).name.equals("黄月英"))		
					{
						System.out.println("黄月英 发动【集智】，摸起一张牌");
						players.get(i).cards.add(all_cards.get(0));
						all_cards.remove(0);
					}
					return !wuxie(t_name,"无懈可击",!t);
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
				case 9:b= !target.name.equals("贾诩");break;
				case 10:b= !target.name.equals("贾诩")&&!target.equals(this);
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
		return cards.get(input_posi("请选择弃置的手牌：",cards.size()));
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
			index=input("请选择目标牌:0.手牌 1.武器 2.防具 3.防御马 4.攻击马 5.断 6.乐 7.闪",8);
			switch(index)
			{
				case -1:
					return null;
				case 0:
					if (!target.cards.isEmpty()) 
					{
						if (target.no==no)
						{
							r=input("请选择手牌:",cards.size());
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
			index=input("请选择目标牌:0.手牌 1.武器 2.防具 3.防御马 4.攻击马",5);
			switch(index)
			{
				case 0:
					if (!target.cards.isEmpty()) 
					{
						if (target.no==no)
						{
							r=input("请选择手牌:",cards.size());
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
		if (name.equals("关羽")&&c.color%2==1) return true;
		return s.equals("杀")||s.equals("火杀")||s.equals("雷杀");
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
				System.out.println(">> "+name+"发动了青龙刀，再出杀!");
				c.use(this, sc);
			}
		}
		if (search_kill()!=null)
		{
			b=input("是否发动青龙刀？",2);
			if (b==1)
			{
				show(false);
				do
				{b=input("",cards.size());}while (!is_kill(cards.get(b)));
				kill_target=target;
				System.out.println(name+"发动了青龙刀，再出杀!");
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
				System.out.print(">> "+name+" 发动了麒麟弓，杀掉了");
				if (target.def!=null)
				{
					System.out.print("防御马 ");
					target.def.println();
					target.def=null;
				}
				else
				{
					System.out.print("攻击马 ");
					target.atk.println();
					target.atk=null;
				}	
			}
			else
			{
				do
				{b=input("请选择杀掉的马:0.不发动 1.防御马 2.攻击马",3);}while(b==1&&target.def==null||b==2&&target.atk==null);
				if (b!=0)
				{
					System.out.print(name+" 发动了麒麟弓，杀掉了");
					if (b==1)
					{
						System.out.print("防御马 ");
						target.def.println();
						target.def=null;
					}
					else
					{
						System.out.print("攻击马 ");
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
		else b=input("是否发动雌雄剑？",2);
		if (b==1)
		{
			System.out.println(">> "+name+" 发动了雌雄剑");
			if (target.cards.isEmpty())
			{
				System.out.println(">> "+target.name+" 选择让对方摸一张牌");
				cards.add(all_cards.get(0));
				all_cards.remove(0);
				f.update(this);
			}
			else if (target.bot)
			{
				System.out.print(">> "+target.name+" 弃置了 ");
				target.cards.get(0).println();
				target.cards.remove(0);
				f.update(target);
			}
			else
			{
				b=input("0.自己弃牌 1.对方摸牌",2);
				if (b==0)
				{
					show(false);
					b=input("请选择手牌:",cards.size());
					target.cards.remove(target.cards.get(0));
					System.out.print(target.name+" 弃置了 ");
					target.cards.get(0).println();
					target.cards.remove(0);
					f.update(target);
				}
				else
				{
					System.out.println(">> "+target.name+" 选择让对方摸一张牌");
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
				System.out.print(">> "+name+" 发动贯石斧,丢弃了");
				c.print();
				cards.remove(c);
				all_cards.add(c);
				do
				{c=get_card();}while (c!=weapon);
				System.out.print(",");
				c.print();
				cards.remove(c);
				all_cards.add(c);
				System.out.println("强制命中!");
				return false;
			}
			else
				return true;
		}
		else
		{
			System.out.println("是否发动贯石斧?");
			show(false);
			b=sc.nextInt();
			if (b==1)
			{
				System.out.println("请弃两张牌");
				c=input_card(this,0);
				c.drop(this,1);
				c=input_card(this,0);
				cards.remove(c);
				c.drop(this,1);
				System.out.println(">> "+name+" 发动了贯石斧，强制命中!");
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
				System.out.println(">> "+name+" 发动了寒冰剑");
				c=target.get_card();
				System.out.println(">> "+name+" 弃置了 "+target.name+"的"+c.get_printname());
				target.cards.remove(c);
				f.update(target);
				if (new Random().nextBoolean()) return true;
				c=target.get_card();
				System.out.println(">> "+name+" 弃置了 "+target.name+"的"+c.get_printname());
				target.cards.remove(c);
				f.update(target);
				return true;
			}
		}
		else
		{
			System.out.println("是否发动寒冰剑?");
			int b=sc.nextInt();
			if (b==1)
			{
				System.out.println(">> "+name+" 发动了寒冰剑\n请选择目标的牌:");
				c=input_card(target,0);
				System.out.print(">> "+name+" 弃置了 "+target.name+"的");
				if (c.name.equals("白银狮子")&&c.equip!=null) target.heal(1);
				target.cards.remove(c);
				f.update(target);
				c.equip=null;
				c.println();
				if (target.no_card()) return true;
				System.out.println(name+"再弃置一张牌吗?");
				c=input_card(target,0);
				if (c!=null)
				{
					System.out.print(">> "+name+" 弃置了 "+target.name+"的");
					if (c.name.equals("白银狮子")&&c.equip!=null) target.heal(1);
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
	
	
	boolean test(String s) //判定
	{
		Player p;
		Boolean b=true;
		Card c=all_cards.get(0);
		Card d;
		all_cards.remove(0);
		System.out.print(">> "+name+" 对"+s+"的判定牌为");
		c.println();
		p=search_player("司马懿");
		if (p!=null)
		{
			if (p.bot)
			{
				b=(p.no==no^c.succ(s));  //自己判定失败 或 别人判定成功
				if (!s.equals("八卦阵")&&!s.equals("刚烈")&&!s.equals("雷击")&&!s.equals("屯田")) b=!b;  //需要改判
				if (b)
				{
					d=p.search_pan(s,!c.succ(s));
					if (d!=null)
					{
						all_cards.add(c);
						c=d;
						System.out.print("\n>> 司马懿 发动【鬼才】，打出");
						c.println();
					}
				}
			}
			else
			{
				d=p.input_card("是否发动【鬼才】？");
				if (d!=null)
				{
					all_cards.add(c);
					c=d;
					System.out.print(">> 司马懿 发动【鬼才】，打出");
					c.println();
				}
			}	
		}
		p=search_player("张角");
		if (p!=null)
		{
			if (bot)
			{}
			else
			{
				if (p.input("是否发动【鬼道】？",2)==1)
				{
					d=p.input_card(this,1);
					if (d!=null)
					{
						System.out.println(">> 张角 发动【鬼道】，打出"+d.get_printname()+"，换回"+c.get_printname());
						p.cards.add(c);
						c=d;
					}
				}
			}	
		}
		testcard=c;
		
		if (!s.equals("双雄")&&!s.equals("悲歌"))
		{
			System.out.print(">> "+name+" 对"+s);
			
			if (c.succ(s))
			{
				System.out.print("判定成功");
				switch(s)
				{
					case"乐不思蜀":System.out.println("，跳过出牌阶段");break;
					case"兵粮寸断":System.out.println("，跳过摸牌阶段");break;
					case"闪电":System.out.println("，大掉血！");break;
					case"八卦阵":System.out.println("，打出无影闪！");break;
					default:System.out.println();
				}
			}
			else
			{
				System.out.print("判定失败");
				switch(s)
				{
					case"乐不思蜀":System.out.println("，可以出牌");break;
					case"兵粮寸断":System.out.println("，可以摸牌");break;
					case"闪电":System.out.println("，危机解除");break;
					case"再起":System.out.println("，获得判定牌");b=false;cards.add(c);break;
					default:System.out.println();
				}
			}
		}
		
		if (s.equals("双雄"))
		{
			cards.add(c);
			System.out.print(">> 颜良文丑 获得判定牌"+c.color+"，本回合");
			if (c.color%2==0) System.out.print("红色"); else System.out.print("黑色");
			System.out.println("手牌都可以当作【决斗】");
			return true;
		}
		
		if (name.equals("郭嘉"))
		{
			System.out.print(">> 郭嘉 发动【天妒】，获得");
			c.println();
			p.cards.add(c);
		}
		
		if (s.equals("洛神"))
		{
			if (c.succ(s))
			{
				cards.add(c);
				b=false;
			}
		}
		
		if (s.equals("屯田")&&c.succ(s))
		{
			System.out.println(">> 邓艾 将"+c.get_printname()+"作为“田”置于武将牌上");
			extra.add(new Special(c,"顺手牵羊",skill));				
			b=false;
		}
		
		if (b&&c.color==2)
		{
			p=search_player("曹植");
			if (p!=null)
			{
				System.out.print(">> 曹植 发动【落英】，获得");
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
		case 0:return "黑桃";
		case 1:return "红桃";
		case 2:return "梅花";
		default:return "方片";
		}
	}

	void use_skill() throws Exception
	{

		if (stype==3&&limit||stype==1&&once||stype==2&&!(name.equals("曹植")&&under)&&!(name.equals("邓艾")&&extra.isEmpty()))
			switch(skill)
			{
				case "仁德":rende();break;
				case "制衡":zhiheng();break;
				case "苦肉":kurou();break;
				case "反间":fanjian();break;
				case "结姻":jieyin();break;
				case "离间":lijian();break;
				case "青囊":qingnang();break;
				case "酒诗":jiushi();break;
				case "缔盟":dimeng();break;
				case "驱虎":quhu();break;
				case "强袭":qiangxi();break;
				case "乱武":luanwu();break;
				case "攻心":gongxin();break;
				case "挑衅":tiaoxin();break;
				case "业炎":yeyan();break;
				case "眩惑":xuanhuo();break;
				case "荐言":jianyan();break;
				case "急袭":jixi();break;
				case "排异":paiyi();break;
				case "离魂":lihun();break;
				case "乱击":luanji();break;
				case "奇策":qice();break;
			}
		if (stype==1) once=false;
		if (stype==3) limit=false;
	}

	void qice() throws Exception
	{
		int index;
		Card c;
		String[] names=new String[9];
		names[0]="无中生有";
		names[1]="万箭齐发";
		names[2]="南蛮入侵";
		names[3]="决斗";
		names[4]="过河拆桥";
		names[5]="顺手牵羊";
		names[6]="桃园结义";
		names[7]="火攻";
		names[8]="借刀杀人";
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
			index=input("请选择当作的锦囊牌:",9);
		}
		c=new Special(cards.get(0),names[index],"奇策");
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
			c=hand_card("请选择乱击牌:",cards);
			d=hand_card("请选择乱击牌:",cards);
		}
		if (d==null||c==d||c.color!=d.color)
		{
			System.out.println(">> 两张牌不符合要求！");
			return;
		}
		else
		{
			e=new Special(c,"万箭齐发","乱击");
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
		System.out.println(">> SP貂蝉 发动【离魂】，将武将牌翻面");
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
			System.out.println(">> SP貂蝉 弃置"+c.get_printname()+"，并获得"+target.name+"的所有手牌");
			active=true;
			f.update(this);
			f.update(target);	
		}
		else
		{
			Card c=hand_card("请选择离魂牌:",cards);
			cards.remove(c);
			c.drop(this, 1);
			Player target=input_target("请选择离魂目标:",4);
			cards.addAll(target.cards);
			target.cards.removeAll(target.cards);
			System.out.println(">> SP貂蝉 弃置"+c.get_printname()+"，并获得"+target.name+"的所有手牌");
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
			c=extra.get(input_posi("请选择移去的“权”:",extra.size()));
			target=input_target("请选择目标:",6);
		}
			extra.remove(c);
			c.drop(this,0);
			System.out.println(">> 钟会 移去一张【权】，令"+target.name+"摸两张牌");
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
			Card c=extra.get(input_posi("请选择使用的“田”:",extra.size()));
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
		System.out.println(">> 界徐庶 发动【荐言】");
		if (bot)
		{
			Random r=new Random();
			int x=r.nextInt(5);
			if (search("闪")==null&&search("桃")==null&&r.nextInt(hp)==0)
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
				System.out.println(">> 界徐庶 从牌堆顶亮出了"+c.get_printname());
				if (!b)
					all_cards.add(c);
			}while (b);
			System.out.println(">> 界徐庶 将"+c.get_printname()+"交给自己");
			cards.add(c);	
		}
		else
		{
			int x=input("请选择牌的类型或颜色:0.基本 1.锦囊 2.装备 3.黑色 4.红色",5);

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
				System.out.println(">> 界徐庶 从牌堆顶亮出了"+c.get_printname());
				if (!b)
					all_cards.add(c);
			}while (b);
			p=input_target("请选择将之交给的目标:",4);
			System.out.println(">> 界徐庶 将"+c.get_printname()+"交给"+p.name);
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
			System.out.println(">> 孙权 发动【制衡】");
			count=new Random().nextInt(cards.size())+1;
			for (int i=0;i<count;i++)
			{
				c=get_card();
				System.out.println(">> 孙权 弃置了"+c.get_printname());
				if (i<count-1) System.out.print(",");
				cards.remove(c);
				c.drop(this,1);
			}
			System.out.println(">> 孙权 摸起"+String.valueOf(count)+"张牌");
			for (int i=0;i<count;i++)
				draw_card();
		}
		else
		{
			System.out.println(">> 孙权 发动【制衡】");
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
				System.out.println(">> 孙权 弃置了"+d.get_printname());
				d.drop(this,1);
			}
			System.out.println(">> 孙权 摸起"+String.valueOf(count)+"张牌");
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
			System.out.println(">> 黄盖 发动【苦肉】");
			loss(1);
			System.out.println(">> 黄盖 摸起两张牌");
			draw_card();
			draw_card();
		}
	}
	
	void fanjian() throws Exception
	{
		Player target;
		Card c;
		int x;
		System.out.println(">> 周瑜 发动【反间】");
		if (bot)
			target=get_target(2);
		else
			target=input_target("请选择反间的目标:",2);
		
		c=random_card();
		if (target.bot)
			x=new Random().nextInt(4);
		else
			x=input("请选择花色:",4);
		System.out.println(">> "+target.name+" 选择了"+color_trans(x));
		System.out.println(">> "+target.name+" 抽到了"+c.get_printname());
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
			target1=input_target("请选择先出杀的目标:",4);
			do {target2=input_target("请选择后出杀的目标:",4);}while(target1.equals(target2));
			c=cards.get(input("请选择弃置的手牌:",cards.size()));
		}
		
		cards.remove(c);
		c.drop(this,1);
		System.out.println(">> 貂蝉 发动【离间】，弃置了"+c.get_printname()+"，视为"+target2.name+"对"+target1.name+"使用【决斗】");
		
		if (target1.name.equals("孙策"))
		{
			System.out.println(">> 孙策 发动【激昂】，摸起一张牌");
			target1.draw_card();
		}
		
		if (target2.name.equals("孙策"))
		{
			System.out.println(">> 孙策 发动【激昂】，摸起一张牌");
			target2.draw_card();
		}	
		
		if (target2.name.equals("吕布")||target1.name.equals("吕布")) System.out.println("吕布 发动【无双】");
		b=true;
		while(true)
		{
			if (b)
			{
				if (this.name.equals("吕布"))
				{
					if (target1.bot&&target1.search_killnum()<2)
					{
						target1.damage(1, 0,target2,null);
						break;
					}
					else
					{
						if (target1.react_kill()&&target1.react_kill())  //无双
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
				if (target1.name.equals("吕布"))
				{
					if (target2.bot&&target2.search_killnum()<2)
					{
						target2.damage(1, 0,target1,null);
						break;
					}
					else
					{
						if (target2.react_kill()&&target2.react_kill())  //无双
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
		target=input_target("请选择结姻的对象",8);
		System.out.print("请弃置两张手牌:");
		ArrayList<Card> drops=input_cards(2,cards);
		for (Card c:drops)
		{
			System.out.println(">> 孙尚香 弃置了"+c.get_printname());
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
			System.out.println("华佗 弃置了"+c.get_printname());
			target.heal(1);
		}
		else
		{
			target=input_target("请选择青囊的目标:",8);
			System.out.println("华佗 发动了【青囊】，目标是"+target.name);
			c=input_card();
			cards.remove(c);
			c.drop(this,1);
			System.out.println("华佗 弃置了"+c.get_printname());
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
				System.out.println(">> 曹植 发动【酒诗】，翻面并视为使用了【酒】");
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
			target1=input_target("请选择第一个目标:",2);
			do {target2=input_target("请选择第二个目标:",2);}while(target1.equals(target2));
			if (Math.abs(target1.cards.size()-target2.cards.size())>cards.size()) 
				System.out.println("手牌数不足，发动失败！");			
			else
			{
				drop_cards(Math.abs(target1.cards.size()-target2.cards.size()));
				System.out.println("鲁肃 发动【缔盟】，交换了"+target1.name+"与"+target2.name+"的手牌");
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
					d=target.input_card("请选择拼点牌");
				System.out.println(">> "+name+"打出了"+c.get_printname());
				System.out.println(">> "+target.name+"打出了"+d.get_printname());
				if (c.num>d.num)
				{
					System.out.println(">> 荀彧与"+target.name+"拼点赢了");
				}
				else 
				{
					System.out.println(">> 荀彧与"+target.name+"拼点没赢");
					damage(1,0,target,null);
				}
				cards.remove(c);
				cards.remove(d);
			
		}
		else
		{
			target=input_target("请选择驱虎的目标:",7);
			c=cards.get(input("请选择拼点的牌:",cards.size()));
			d=target.get_large();
			System.out.println(">> "+name+"打出了"+c.get_printname());
			System.out.println(">> "+target.name+"打出了"+d.get_printname());
			if (c.num>d.num)
			{
				target2=target.input_target(">> 荀彧与"+target.name+"拼点赢了\n请选择令其伤害的目标:",1);
				target2.damage(1,0, target,null);
			}
			else 
			{
				System.out.println(">> 荀彧与"+target.name+"拼点没赢");
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
				System.out.println(">> 典韦 发动【强袭】，目标是"+target.name);
				System.out.println(">> 典韦 流失一点体力");			
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
				System.out.println(">> 典韦 发动【强袭】，目标是"+target.name);
				System.out.println(">> 典韦 弃置"+c.get_printname());
			}
			target.damage(1, 0, this, null);
		}
		else
		{
			if (input("请选择【强袭】方式（0.掉血 1.武器）:",2)==0)
				loss(1);
			else
			{
				c=input_card(this,0);
				if (c.type!=2) return;
				c.drop(this,1);
			}
			target=input_target("请选择【强袭】目标:",1);
			System.out.println(">> 典韦 发动【强袭】，目标是"+target.name);
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
				System.out.println(">> 神吕蒙 发动【攻心】，目标是"+target.name);
				System.out.print(">> 神吕蒙 观看了"+target.name+"的手牌");
				for (Card e:target.cards)
					if (e.color==1&&(c==null||e.name.equals("桃")||e.name.equals("无中生有")))
						c=e;
				if (c!=null)
				{

						d=search("无中生有");
						if (d!=null)
						{
							if (c.name.equals("桃")||c.name.equals("无中生有"))
							{
								System.out.println("，并将"+c.get_printname()+"置于牌堆顶");
								d.use(this, sc);
								return;
							}
						}
						System.out.println("，并将"+c.get_printname()+"弃置");
						c.drop(target,1);
				}
						
			}
			
		}	
		else
		{
			target=input_target("请选择目标:",3);
			System.out.println(">> 神吕蒙 发动【攻心】，目标是"+target.name);
			
			target.show(false);
			do {index=input("请选择处理的手牌:",target.cards.size());}while(index>=0&&target.cards.get(index).color!=1);			
			if (index==-1) System.out.println();
			else
			{
				c=target.cards.get(index);
				target.cards.remove(index);
				index=input_posi("0.置于牌堆顶\n1.弃置",2);
				System.out.print(">> 神吕蒙 观看了"+target.name+"的手牌");
				if (index==0)
				{
					System.out.println("，并将"+c.get_printname()+"置于牌堆顶");
					all_cards.add(0,c);
				}
				else
				{
					System.out.println("，并将"+c.get_printname()+"弃置");
					c.drop(target,1);
				}
			}
				
			
		}
	}
	
	void luanwu()
	{
		fail++;
	}
	
	void tiaoxin() throws Exception  //实现bot选牌？
	{
		Player target;
		Card c;
		int index;
		int rnd;
		System.out.print(">> 姜维 发动了【挑衅】");
		if (bot)
		{
			ArrayList<Player>ps=get_players(1);
			if (search("闪")!=null)
				rnd=3;
			else 
				rnd=2;
			
			if (!ps.isEmpty()&&new Random().nextInt(rnd)>0)
			{
				target=random_player(ps);
				c=target.search_kill();
				if (c==null) 
				{ 
					System.out.println(">> "+target.name+" 放弃出杀");
					c=target.get_card();
					System.out.print(">> 姜维 弃置了目标的"+c.get_equip()+c.get_printname());
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
						System.out.println("请出【杀】:");
						target.show(false);
						do{index=target.input("",target.cards.size());}while (index>=0&&!target.cards.get(index).is_kill());
						if (index<0) 
						{
							System.out.println(">> "+target.name+" 放弃出杀");
							c=target.get_card();
							System.out.print(">> 姜维 弃置了目标的"+c.get_equip()+c.get_printname());
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
			target=players.get(input_other("\n请选择目标:",players.size()));
			c=target.search_kill();
			if (c==null) 
			{ 
				System.out.println(">> "+target.name+" 放弃出杀");
				c=input_card(target,0);
				System.out.print(">> 姜维 弃置了目标的"+c.get_equip()+c.get_printname());
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
			target1=input_target("请选择一个目标:",2);
			d1=input("请输入分配的伤害:",4);
			if (d1<3) 
			{
				do{target2=input_target("是否再选择一个目标:",2);}while (target2.equals(target1));
				if (target2!=null)
				{
					d2=input("请输入分配的伤害:",4-d1);
					if (d1+d2<3)
						do{target3=input_target("是否再选择一个目标:",2);}while (target3.equals(target1)||target3.equals(target2));
					if (target3!=null) d3=1;
				}
			}
			if (d1>1||d2>1||d3>1)
			{
				c=input_colored("请弃一张黑桃手牌:",0);
				if (c==null) return; else drops.add(c);
				c=input_colored("请弃一张红桃手牌:",1);
				if (c==null) return; else drops.add(c);
				c=input_colored("请弃一张梅花手牌:",2);
				if (c==null) return; else drops.add(c);
				c=input_colored("请弃一张方片手牌:",3);
				if (c==null) return; else drops.add(c);
				for (int i=0;i<4;i++)
				{
					System.out.println(">> 神周瑜 弃置了"+drops.get(i).get_printname());
					drops.get(i).drop(this,1);
				}
				loss(3);
			}
			System.out.println(">> 神周瑜 发动【业炎】");
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
			c=input_colored("请选择一张红桃手牌:",1);
			if (c!=null)
			{
				target=input_target("请选择目标:",2);
				target.cards.add(c);
				cards.remove(c);
				System.out.println(">> 法正 发动【眩惑】，将"+c.get_printname()+"交给了"+target.name);
				c=input_card(target,0);
				f.update(this);
				System.out.print(">> 法正 获取了目标的");
				if (c.equip==null)
					System.out.println("手牌"+c.get_printname());
				else
					System.out.println(c.equip+c.get_printname());
				c.drop(target,2);
				f.update(target);
				target=input_target("请选择交给的目标:",6);
				if (target==null) target=this;
				System.out.println(">> 法正 将此牌交给"+target.name);
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
			if (c.num>d.num&&!d.name.equals("桃")&&!d.name.equals("无中生有")) d=c;
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
