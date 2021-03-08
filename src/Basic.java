import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Basic extends Card{
	Scanner sc;
	Basic(int c,int n,String s)
	{
		super(c,n,s);
		type=0;
	}

	Basic(Card c,String s,String sk)
	{
		super(c,s,sk);
		type=0;
	}
	
	Basic(String s,Frame f,ArrayList<Player>p,ArrayList<Card>c)
	{	
		super(s,f,p,c);
		type=0;
	}
	
	void use(Player p,Scanner sc) throws Exception
	{
		System.out.print(">> "+p.name+" 使用了"+showname());
		if (skill!=null) System.out.print("("+skill+")");
		Thread.currentThread().sleep(300);
		f.update(p);
		Card c;
		switch (name)
		{
			case "桃":origin.println();p.heal(1);f.update(p);break;
			case "酒":p.drunk=true;origin.println();break;
			default:
				int dam=1,d_type,index,nums=1;
				Player target;
				p.use_kill=true;
				switch (this.name)
				{
					case "杀":d_type=0;break;
					case "火杀":d_type=1;break;
					default: d_type=2;
				}
				p.kill++;
				if (p.cards.size()==1&&p.get_weapon("方天画戟")) 
				{
					nums=3;
					System.out.println(p.name+" 持有方天画戟，杀可以指定三个目标");
				}
				for (int i=0;i<nums;i++)
				{
					if (p.kill_target==null)
					{
						if (p.bot)
						{				
							origin.print();
							target=p.get_target(1);
						}
						else
						{
							origin.println();
							do
							{
								System.out.print("请选择目标:");
								index=sc.nextInt();			
							}while(!p.in_dist(players.get(index), true));
							target=players.get(index);
						}
					}
					else
					{
						origin.println();
						if (i>0&&new Random().nextBoolean()) break;
						target=p.kill_target;
					}
					
					if (p.name.equals("孙策")&&(color==1||color==3))
					{
						System.out.println(">> 孙策 发动【激昂】，摸起一张牌");
						p.draw_card();
					}
					
					if (target.name.equals("大乔"))
					{
						if (target.bot)
						{
							for (Player pl:players)
								if (target.in_dist(pl, true))
								{
									System.out.println(">> 大乔发动【流离】");
									c=target.get_card();
									System.out.println(">> 大乔弃置了"+c.get_printname()+"，将目标转移为"+pl.name);
									c.drop(target,1);
									target=pl;
									break;
								}
						}
						else
						{
							if (target.input("是否发动流离:",2)==1)
							{
								System.out.println(">> 大乔发动【流离】");
								Player pl=target.input_target("请选择转移的目标:", 1);
								c=target.input_card(target,0);
								System.out.println(">> 大乔弃置了"+c.get_printname()+"，将目标转移为"+pl.name);
								c.drop(target,1);
								target=pl;							
							}
						}
					}
					
					
					if (p.get_weapon("朱雀羽扇")&&type==0)
					{
						if (!p.bot)
							System.out.print("是否发动朱雀羽扇:");
						if (p.bot||sc.nextInt()==1) 
						{
							System.out.println(">> "+p.name+" 发动了朱雀羽扇，变为火焰杀");;
							d_type=1;
						}
					}
					
					if (p.get_weapon("雌雄双股剑") && p.sex!=target.sex)
					{
						if (p.bot||!p.bot&&p.input("是否发动雌雄双股剑",2)==1)
						{
							System.out.println(">> "+p.name+" 发动了雌雄双股剑");
							if (target.bot||target.input("请选择:0.弃一张牌 1.对方摸一张牌" , 2)==1)
							{
								System.out.println(">> "+target.name+"选择让"+p.name+"摸一张牌");
								p.cards.add(all_cards.get(0));
								all_cards.remove(0);
								f.update(p);
							}
							else
							{
								c=target.cards.get(target.input("请弃置一张手牌:",target.cards.size()));
								System.out.println(">> "+target.name+"弃置了"+c.get_printname());
								target.cards.remove(c);
								c.drop(target,1);
							}
						};
	
					}
					
					if (name.equals("杀")&&origin.name.equals("闪")&&p.name.equals("SP赵云"))
					{
						if (!target.cards.isEmpty()&&(p.bot||p.input("是否发动【冲阵】:",2)==1))
						{			
							System.out.println(">> SP赵云 发动【冲阵】，获得"+target.name+"一张手牌");
							c=target.cards.get(new Random().nextInt(target.cards.size()));
							target.cards.remove(c);
							c.drop(target,2);
							p.cards.add(c);
							f.update(p);
							f.update(target);
						}
					}
					if (p.name.equals("吕布")) System.out.println(">> 吕布 发动【无双】");
					
					if (target.name.equals("于禁")&&target.armor==null&&color%2==0)
					{
						System.out.println(">> 于禁 发动【毅重】，黑色【杀】无效！");
						continue;
					}
					
					if (target.name.equals("孙策")&&(color==1||color==3))
					{
						System.out.println(">> 孙策 发动【激昂】，摸起一张牌");
						target.draw_card();
					}
					
					boolean react=false,can_miss=true;
					
					if (p.name.equals("马超"))
					{
						if (p.bot||p.input("是否发动【铁骑】？",2)==1)
						{
								c=all_cards.get(0);
								System.out.print(">> 马超 对【铁骑】的判定牌为");
								c.print();
								if (c.color%2==1)
								{
									System.out.println("，判定成功，此杀不闪避！");
									can_miss=false;
								}
								else
									System.out.println(",判定失败");
														
						}
					}
					
					if (p.name.equals("黄忠")&&(target.cards.size()>=p.hp||target.cards.size()<=p.kill_dist()+p.atk_dist()-1))
					{
						if (p.bot||p.input("是否发动【烈弓】？",2)==1)
						{
									System.out.println(">> 黄忠发动【烈弓】，此杀不闪避！");
									can_miss=false;													
						}
					}
					
					if (!p.get_weapon("青钢剑"))
					{
						if (target.get_armor("藤甲")&&d_type==0) {System.out.println(">> "+target.name+" 的藤甲抵御普通杀!");continue;}
						if (target.get_armor("仁王盾")&&color%2==0) {System.out.println(">> "+target.name+" 的仁王盾抵御黑色杀!");continue;}
						if ((target.get_armor("八卦阵")&&can_miss))react=target.test("八卦阵");
						if (target.name.equals("卧龙")&&target.armor==null&&can_miss)
						{
							System.out.println(">> 卧龙 发动【八阵】");
							react=target.test("八卦阵");
						}
					}
					else if  (target.get_armor("藤甲")&&d_type==1) dam=0; 
					
					if (!react&&can_miss)
					{
						if (p.name.equals("吕布"))
						{
							if (target.search_num("闪")>=2||target.get_armor("八卦阵")||!target.bot)
							{
								react=target.react("闪",p);
								if (react) react=target.react("闪",p);
							}
						}
						else
							react=target.react("闪",p);
					}
					if (react)
					{
						if (p.get_weapon("贯石斧"))
							react=p.guanshifu(all_cards,sc);
						if (react)
						{
							if (p.name.equals("庞德"))
							{
								if (p.bot)
								{
									
								}
								else
								{
									if (p.input("是否发动【猛进】",2)==1)
									{
										c=p.input_card(target,0);
										System.out.print(">> 庞德 弃置了目标的");
										if (c.equip!=null) System.out.println(c.equip+c.get_printname());
										else System.out.println("手牌"+c.get_printname());
										c.drop(target,1);
									}
								}
							}
						}
						if (p.get_weapon("青龙偃月刀"))
							p.qinglongdao(players,all_cards,target,f,sc);
					}
					if (!react)
					{
						System.out.println(">> "+target.name+" 放弃");
						if (p.name.equals("许褚")&&p.active)
						{
							System.out.println(">> "+target.name+" 被裸衣杀 ，伤害+1");
							dam++;
						}
						if (p.get_weapon("古锭刀") && target.cards.isEmpty())
						{
							System.out.println(">> "+target.name+" 无牌接古锭，伤害+1");
							dam++;
						}
						if (p.drunk)
						{
							System.out.println(">> "+target.name+" 被酒杀，伤害+1");
							dam++;
						}
						if (p.get_weapon("寒冰剑")&&p.hanbingjian(target))
							continue;
						if (p.get_weapon("麒麟弓"))
							p.qilingong(target);
						if (p.name.equals("马岱"))
						{
							if (p.bot&&dam==1&&(target.hp<=2||target.hp==target.maxhp)||!p.bot&&p.input("是否发动潜袭？",2)==1)
							{
								System.out.println(">> 马岱 发动【潜袭】");
								if (p.test("潜袭"))
								{
									System.out.println(">> "+target.name+" 扣减了一点体力上限");
									target.maxhp--;
									target.hp=Integer.min(target.maxhp,target.hp);
									f.update(target);
									continue;
								}
							}
						}
						target.damage(dam, d_type,p,origin);
					}
					f.update(target);
				}
				p.drunk=false;
		}
		drop(p,0);
	}
}
