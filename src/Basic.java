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
		System.out.print(">> "+p.name+" ʹ����"+showname());
		if (skill!=null) System.out.print("("+skill+")");
		Thread.currentThread().sleep(300);
		f.update(p);
		Card c;
		switch (name)
		{
			case "��":origin.println();p.heal(1);f.update(p);break;
			case "��":p.drunk=true;origin.println();break;
			default:
				int dam=1,d_type,index,nums=1;
				Player target;
				p.use_kill=true;
				switch (this.name)
				{
					case "ɱ":d_type=0;break;
					case "��ɱ":d_type=1;break;
					default: d_type=2;
				}
				p.kill++;
				if (p.cards.size()==1&&p.get_weapon("���컭�")) 
				{
					nums=3;
					System.out.println(p.name+" ���з��컭ꪣ�ɱ����ָ������Ŀ��");
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
								System.out.print("��ѡ��Ŀ��:");
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
					
					if (p.name.equals("���")&&(color==1||color==3))
					{
						System.out.println(">> ��� ������������������һ����");
						p.draw_card();
					}
					
					if (target.name.equals("����"))
					{
						if (target.bot)
						{
							for (Player pl:players)
								if (target.in_dist(pl, true))
								{
									System.out.println(">> ���Ƿ��������롿");
									c=target.get_card();
									System.out.println(">> ����������"+c.get_printname()+"����Ŀ��ת��Ϊ"+pl.name);
									c.drop(target,1);
									target=pl;
									break;
								}
						}
						else
						{
							if (target.input("�Ƿ񷢶�����:",2)==1)
							{
								System.out.println(">> ���Ƿ��������롿");
								Player pl=target.input_target("��ѡ��ת�Ƶ�Ŀ��:", 1);
								c=target.input_card(target,0);
								System.out.println(">> ����������"+c.get_printname()+"����Ŀ��ת��Ϊ"+pl.name);
								c.drop(target,1);
								target=pl;							
							}
						}
					}
					
					
					if (p.get_weapon("��ȸ����")&&type==0)
					{
						if (!p.bot)
							System.out.print("�Ƿ񷢶���ȸ����:");
						if (p.bot||sc.nextInt()==1) 
						{
							System.out.println(">> "+p.name+" ��������ȸ���ȣ���Ϊ����ɱ");;
							d_type=1;
						}
					}
					
					if (p.get_weapon("����˫�ɽ�") && p.sex!=target.sex)
					{
						if (p.bot||!p.bot&&p.input("�Ƿ񷢶�����˫�ɽ�",2)==1)
						{
							System.out.println(">> "+p.name+" �����˴���˫�ɽ�");
							if (target.bot||target.input("��ѡ��:0.��һ���� 1.�Է���һ����" , 2)==1)
							{
								System.out.println(">> "+target.name+"ѡ����"+p.name+"��һ����");
								p.cards.add(all_cards.get(0));
								all_cards.remove(0);
								f.update(p);
							}
							else
							{
								c=target.cards.get(target.input("������һ������:",target.cards.size()));
								System.out.println(">> "+target.name+"������"+c.get_printname());
								target.cards.remove(c);
								c.drop(target,1);
							}
						};
	
					}
					
					if (name.equals("ɱ")&&origin.name.equals("��")&&p.name.equals("SP����"))
					{
						if (!target.cards.isEmpty()&&(p.bot||p.input("�Ƿ񷢶�������:",2)==1))
						{			
							System.out.println(">> SP���� ���������󡿣����"+target.name+"һ������");
							c=target.cards.get(new Random().nextInt(target.cards.size()));
							target.cards.remove(c);
							c.drop(target,2);
							p.cards.add(c);
							f.update(p);
							f.update(target);
						}
					}
					if (p.name.equals("����")) System.out.println(">> ���� ��������˫��");
					
					if (target.name.equals("�ڽ�")&&target.armor==null&&color%2==0)
					{
						System.out.println(">> �ڽ� ���������ء�����ɫ��ɱ����Ч��");
						continue;
					}
					
					if (target.name.equals("���")&&(color==1||color==3))
					{
						System.out.println(">> ��� ������������������һ����");
						target.draw_card();
					}
					
					boolean react=false,can_miss=true;
					
					if (p.name.equals("��"))
					{
						if (p.bot||p.input("�Ƿ񷢶��������",2)==1)
						{
								c=all_cards.get(0);
								System.out.print(">> �� �ԡ�������ж���Ϊ");
								c.print();
								if (c.color%2==1)
								{
									System.out.println("���ж��ɹ�����ɱ�����ܣ�");
									can_miss=false;
								}
								else
									System.out.println(",�ж�ʧ��");
														
						}
					}
					
					if (p.name.equals("����")&&(target.cards.size()>=p.hp||target.cards.size()<=p.kill_dist()+p.atk_dist()-1))
					{
						if (p.bot||p.input("�Ƿ񷢶����ҹ�����",2)==1)
						{
									System.out.println(">> ���ҷ������ҹ�������ɱ�����ܣ�");
									can_miss=false;													
						}
					}
					
					if (!p.get_weapon("��ֽ�"))
					{
						if (target.get_armor("�ټ�")&&d_type==0) {System.out.println(">> "+target.name+" ���ټ׵�����ͨɱ!");continue;}
						if (target.get_armor("������")&&color%2==0) {System.out.println(">> "+target.name+" �������ܵ�����ɫɱ!");continue;}
						if ((target.get_armor("������")&&can_miss))react=target.test("������");
						if (target.name.equals("����")&&target.armor==null&&can_miss)
						{
							System.out.println(">> ���� ����������");
							react=target.test("������");
						}
					}
					else if  (target.get_armor("�ټ�")&&d_type==1) dam=0; 
					
					if (!react&&can_miss)
					{
						if (p.name.equals("����"))
						{
							if (target.search_num("��")>=2||target.get_armor("������")||!target.bot)
							{
								react=target.react("��",p);
								if (react) react=target.react("��",p);
							}
						}
						else
							react=target.react("��",p);
					}
					if (react)
					{
						if (p.get_weapon("��ʯ��"))
							react=p.guanshifu(all_cards,sc);
						if (react)
						{
							if (p.name.equals("�ӵ�"))
							{
								if (p.bot)
								{
									
								}
								else
								{
									if (p.input("�Ƿ񷢶����ͽ���",2)==1)
									{
										c=p.input_card(target,0);
										System.out.print(">> �ӵ� ������Ŀ���");
										if (c.equip!=null) System.out.println(c.equip+c.get_printname());
										else System.out.println("����"+c.get_printname());
										c.drop(target,1);
									}
								}
							}
						}
						if (p.get_weapon("�������µ�"))
							p.qinglongdao(players,all_cards,target,f,sc);
					}
					if (!react)
					{
						System.out.println(">> "+target.name+" ����");
						if (p.name.equals("����")&&p.active)
						{
							System.out.println(">> "+target.name+" ������ɱ ���˺�+1");
							dam++;
						}
						if (p.get_weapon("�Ŷ���") && target.cards.isEmpty())
						{
							System.out.println(">> "+target.name+" ���ƽӹŶ����˺�+1");
							dam++;
						}
						if (p.drunk)
						{
							System.out.println(">> "+target.name+" ����ɱ���˺�+1");
							dam++;
						}
						if (p.get_weapon("������")&&p.hanbingjian(target))
							continue;
						if (p.get_weapon("���빭"))
							p.qilingong(target);
						if (p.name.equals("���"))
						{
							if (p.bot&&dam==1&&(target.hp<=2||target.hp==target.maxhp)||!p.bot&&p.input("�Ƿ񷢶�ǱϮ��",2)==1)
							{
								System.out.println(">> ��� ������ǱϮ��");
								if (p.test("ǱϮ"))
								{
									System.out.println(">> "+target.name+" �ۼ���һ����������");
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
