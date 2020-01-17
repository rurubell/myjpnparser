package src.ui.menu;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;

import src.ui.win.*;
import src.data.MyJapanWord;
import src.jap.dict.elib.MyELIB;
import src.jap.dict.jardic.MyJardic;


public class MyAnalyzedJTextPaneMenu extends JPopupMenu
{
	//Ссвлка на JTextPane, с которым работаем
	JTextPane jtf_link = null;
	
	
	public MyAnalyzedJTextPaneMenu( JTextPane jtf_link )
	{
		this.jtf_link = jtf_link;
		
		JMenuItem jmi_copy = new JMenuItem( "Copy" );
		jmi_copy.setAccelerator( KeyStroke.getKeyStroke("control C") );
		jmi_copy.addActionListener( new ALCopy() );
		
		JMenuItem jmi_japtoru_jardic = new JMenuItem( "Jap -> Ru (jardic)" );
		jmi_japtoru_jardic.addActionListener( new ALJapToRuJardic() );
		
		JMenuItem jmi_japtoru_elib = new JMenuItem( "Jap -> Ru (e-lib)" );
		jmi_japtoru_elib.addActionListener( new ALJapToRuELIB() );
		
		this.add( jmi_copy );
		this.addSeparator();
		this.add( jmi_japtoru_jardic );
		this.add( jmi_japtoru_elib );
	}
	
	
	//Copy
	class ALCopy implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) { jtf_link.copy(); }
	}
	
	
	//Перевести выделение на английский
	class ALJapToEn implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			String s_text = jtf_link.getSelectedText();
			if( s_text != null ) 
			{
				MyJapToEnTranslateWin mjtrtw = new MyJapToEnTranslateWin( s_text );
				mjtrtw.setVisible( true );
			}
		}
	}
	
	
	//Перевести выделение на русский
	class ALJapToRu implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			String s_text = jtf_link.getSelectedText();
			if( s_text != null ) 
			{
				MyJapToRuTranslateWin mjtrtw = new MyJapToRuTranslateWin( s_text );
				mjtrtw.setVisible( true );
			}
		}
	}
	
	
	//Перевести выделение на русский (e-lib)
	class ALJapToRuELIB implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			String s_text = jtf_link.getSelectedText();
			if( s_text != null ) 
			{
				MyJapToRuELIBTranslateWin mjtretw = new MyJapToRuELIBTranslateWin( s_text );
				mjtretw.setVisible( true );
			}
		}
	}
	
	
	//Перевести выделение на русский (jardic)
	class ALJapToRuJardic implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			String s_text = jtf_link.getSelectedText();
			if( s_text != null ) 
			{
				MyJapToRuJardicTranslateWin mjtretw = new MyJapToRuJardicTranslateWin( s_text );
				mjtretw.setVisible( true );
			}
		}
	}
}
