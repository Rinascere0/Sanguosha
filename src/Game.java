package sanguosha;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class Game {

	static int num;
	static ArrayList<Player> players;
	static ArrayList<Character> characters;
	static HashMap<String,Integer> info;
	static ArrayList<Card> all_cards;
	public static void init_card() throws IOException
	{
		all_cards=new ArrayList<Card>();
		int x;
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\server\\card.txt"),"UTF-8"));  
		for (int i=0;i<160;i++)
		{
			x=Integer.valueOf(br.readLine());
			switch (x)
			{
				case 0:all_cards.add(new Basic(Integer.valueOf(br.readLine()),Integer.valueOf(br.readLine()),br.readLine()));break;
				case 1:all_cards.add(new Special(Integer.valueOf(br.readLine()),Integer.valueOf(br.readLine()),br.readLine()));break;
				case 2:all_cards.add(new Weapon(Integer.valueOf(br.readLine()),Integer.valueOf(br.readLine()),br.readLine(),Integer.valueOf(br.readLine())));break;
				case 3:all_cards.add(new Armor(Integer.valueOf(br.readLine()),Integer.valueOf(br.readLine()),br.readLine()));break;
				case 4:all_cards.add(new Atk(Integer.valueOf(br.readLine()),Integer.valueOf(br.readLine()),br.readLine()));break;
				case 5:all_cards.add(new Def(Integer.valueOf(br.readLine()),Integer.valueOf(br.readLine()),br.readLine()));
			}
		}
		
		Collections.shuffle(all_cards,new Random());

	}
	
	public static void init_character(Scanner sc) throws IOException
	{
		characters=new ArrayList<Character>();
		Character c;
		String s;
		int x;
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\server\\character.txt"),"UTF-8"));  
		for (int i=0;i<66;i++)
		{
			s=br.readLine();
			c=new Character(s);
			c.maxhp=Integer.valueOf(br.readLine());
			if(Integer.valueOf(br.readLine())==0) c.sex=true;else c.sex=false;
			x=Integer.valueOf(br.readLine());
			if (x==-1)
				c.skill=null;
			else
			{
				c.stype=x;
				c.skill=(br.readLine());
			}
			if (i<17) c.team=0;
			else if (i<37) c.team=1;
			else if (i<51) c.team=2;
			else c.team=3;
			characters.add(c);
		}
		Collections.shuffle(characters,new Random());
	}
	
	public static void init_player(Scanner sc)
	{
		int i;
		Character c;
		players=new ArrayList<Player>();
		System.out.print("请输入游戏人数:");
		num=sc.nextInt();
		System.out.println("请选择武将:");
		for (i=0;i<60;i++)
			System.out.println(String.valueOf(i)+"."+characters.get(i).name);
		c=characters.get(sc.nextInt());
		System.out.println("■ 你选择了 "+c.name);
		players.add(new Player(c,num,0,false));

		players.get(0).init_card(all_cards);
		for (i=0;i<num-1;i++)
		{
			c=characters.get(i+50);
			players.add(new Player(c,num,i+1,true));
			System.out.println("■ "+String.valueOf(i+1)+"号位选择了 "+c.name);
		}
	}
	
	static void set(Frame f)
	{
		Player p;
		
		for (int i=0;i<all_cards.size();i++)
		{
			all_cards.get(i).f=f;
			all_cards.get(i).all_cards=all_cards;
			all_cards.get(i).players=players;
		}
		
		for (int i=0;i<players.size();i++)
		{
			p=players.get(i);
			p.f=f;
			p.all_cards=all_cards;
			p.players=players;
			players.get(i).init_card(all_cards);
			f.update(p);
		}
		

	}
	
	public static void main(String[] args) throws Exception
	{
		Scanner sc = new Scanner(System.in); 
		init_card();
		init_character(sc);
		init_player(sc);
		
		Frame frame=new Frame(players);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setExtendedState(0);
		frame.toFront();
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		set(frame);
		
		while (players.size()>1)
		{
			for (int i=0;i<players.size();i++)
			{
				if (players.get(i).alive)
					players.get(i).turn();
			}
		}
		System.out.print(players.get(0).name+ "获得了胜利!");
		sc.close();
		
	}
}
