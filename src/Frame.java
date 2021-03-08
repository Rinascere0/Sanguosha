package sanguosha;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

public class Frame extends JFrame {
	private static final int DEFAULT_WIDTH=800; //600	
	private static final int DEFAULT_HEIGHT=350;
	JLabel []lab=new JLabel[10];
	JLabel left=new JLabel();
	JPanel panel=new JPanel();
	int current;
	
	void update(Player p)
	{
		String s="<html><p>";
		if (p.alive) s+=String.valueOf(p.no)+"��λ "; else s+="(������)";
		s+=p.name+"&emsp&emsp&emsp&emsp&emsp&emsp<p><p>��HP��"+String.valueOf(p.hp)+"/"+String.valueOf(p.maxhp)+"<p><p>�����ơ�"+String.valueOf(p.cards.size())+"<p><p>��������"+p.get_weapon()+"<p><p>�����ߡ�"+p.get_armor()+"<p><p>��������"+p.get_def()+"<p><p>��������"+p.get_atk()+"<p><p>";
		if (p.pan_le!=null) s+="�� ";
		if (p.pan_duan!=null) s+="�� ";
		if (p.pan_shan!=null) s+="�� ";
		if (p.under) s+="�� ";
		if (p.link) s+="�� ";
		if (!p.extra.isEmpty()) s+=p.extra_name+String.valueOf(p.extra.size());
		s+="<p></html>";
		lab[p.pos].setText(s);
		s="<html>";
		for (int i=0;i<6;i++)
			s+="<p>"+p.all_cards.get(p.all_cards.size()-i-1).get_printname()+"<p>";
		left.setText(s);
	}
	
	
	Frame(ArrayList<Player> players)
	{
		setTitle("����ɱ");
		setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		for (int i=0;i<players.size();i++)
		{
			lab[i]=new JLabel();
			panel.add(lab[i]);
		}
		panel.add(left);
		add(panel);
	}
}