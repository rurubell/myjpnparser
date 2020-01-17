package src.ui.win;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;

import src.data.MyJapanWord;

import src.ui.button.*;
import src.ui.panel.*;

import src.jap.transhell.MyTranslateShell;


public class MyEnToRuTranslateWin extends JFrame
{
	public MyEnToRuTranslateWin( String s_text )
	{
		MyTranslateShell mts = new MyTranslateShell();
		String s_transl = mts.translateEnToRu( s_text );
		
		JPanel word_panel = this.makePanelWithWord( s_text );
		JPanel transl_panel = this.makePanelWithTranslation( s_transl );
		
		JPanel main_panel = new JPanel();
		main_panel.setLayout( new BoxLayout( main_panel, BoxLayout.PAGE_AXIS ) );
		main_panel.add( word_panel );
		main_panel.add( transl_panel );
		
		this.add( main_panel );
		this.pack();
	}
	
	
	//Создать панель с переводимым словом
	private JPanel makePanelWithWord( String s_text )
	{
		JPanel res_panel = new JPanel( new BorderLayout() );
		Border b_transl_border = BorderFactory.createTitledBorder( "English word" );
		res_panel.setBorder( b_transl_border );
		
		JTextPane text_pane = new JTextPane();
		text_pane.setFont( new Font( "Sans", Font.BOLD, 28 ) );
		text_pane.setBackground( Color.decode( "#FFFFFF" ) );
		text_pane.setText( s_text );
		
		Border b_comp_bord = new CompoundBorder
		(
			BorderFactory.createLineBorder( Color.decode( "#444444" ) ), 
			BorderFactory.createMatteBorder( 5, 5, 5, 5, Color.decode( "#FFFFFF" ) )
		);
		text_pane.setBorder( b_comp_bord );
		
		res_panel.add( text_pane );
		return res_panel;
	}
	
	
	//Создать панель с переводом
	private JPanel makePanelWithTranslation( String s_text )
	{
		JPanel res_panel = new JPanel( new BorderLayout() );
		Border b_transl_border = BorderFactory.createTitledBorder( "Russian translation" );
		res_panel.setBorder( b_transl_border );
		
		Color col = this.makeRandomBGColor();
		JTextPane text_pane = new JTextPane();
		text_pane.setFont( new Font( "Sans", Font.BOLD, 12 ) );
		text_pane.setBackground( col );
		text_pane.setText( s_text );
		
		Border b_comp_bord = new CompoundBorder
		(
			BorderFactory.createLineBorder( Color.decode( "#444444" ) ), 
			BorderFactory.createMatteBorder( 5, 5, 5, 5, col )
		);
		text_pane.setBorder( b_comp_bord );
		
		res_panel.add( text_pane );
		return res_panel;
	}
	
	
	//Создать рандомный цвет
	private Color makeRandomBGColor()
	{
		Random random = new Random();
		
		int n_red = random.nextInt( 64 ) + 192;
		int n_green = random.nextInt( 64 ) + 192;
		int n_blue = random.nextInt( 64 ) + 192;
		
		Color res_col = new Color( n_red, n_green, n_blue );
		return res_col;
	}
}
