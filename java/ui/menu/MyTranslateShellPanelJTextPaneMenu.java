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


public class MyTranslateShellPanelJTextPaneMenu extends JPopupMenu
{
	//Ссвлка на JTextPane, с которым работаем
	JTextPane jtf_link = null;
	//Список субокон главного окна
	ArrayList< JFrame > sub_frames_al = null;
	
	
	public MyTranslateShellPanelJTextPaneMenu( JTextPane jtf_link, ArrayList< JFrame > sub_frames_al )
	{
		this.jtf_link = jtf_link;
		this.sub_frames_al = sub_frames_al;
		
		JMenuItem jmi_copy = new JMenuItem( "Copy" );
		jmi_copy.setAccelerator( KeyStroke.getKeyStroke("control C") );
		jmi_copy.addActionListener( new ALCopy() );
		
		JMenuItem jmi_entoru = new JMenuItem( "En -> Ru" );
		jmi_entoru.addActionListener( new ALEnToRu() );
		
		this.add( jmi_copy );
		this.addSeparator();
		this.add( jmi_entoru );
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
				sub_frames_al.add( mjtrtw );
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
				sub_frames_al.add( mjtrtw );
			}
		}
	}
	
	
	//Перевести выделение на русский
	class ALEnToRu implements ActionListener 
	{
		public void actionPerformed(ActionEvent e) 
		{
			String s_text = jtf_link.getSelectedText();
			if( s_text != null ) 
			{
				MyEnToRuTranslateWin mjtrtw = new MyEnToRuTranslateWin( s_text );
				mjtrtw.setVisible( true );
				sub_frames_al.add( mjtrtw );
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
				sub_frames_al.add( mjtretw );
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
				sub_frames_al.add( mjtretw );
			}
		}
	}
}
