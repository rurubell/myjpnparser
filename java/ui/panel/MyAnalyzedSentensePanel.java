package src.ui.panel;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import javax.swing.border.*;
import java.awt.event.*;

import src.ui.menu.*;
import src.data.MyJapanWord;
import src.jap.tokenizer.MyTokenizer;
import src.jap.tokenizer.kuromoji.MyKuromoji;

import src.params.MyParams;


public class MyAnalyzedSentensePanel extends MyPanel
{
	//Панель которую вернем
	private JPanel res_panel = null;
	//Список слов, для которых нужно получить перевод
	ArrayList< MyJapanWord > mjw_list = null;
	//Флаг для инициализации
	private static boolean b_flag = true;
	
	//Цвета слова
	private static String s_alverb_col = null;
	private static String s_adverb_col = null;
	private static String s_adj_col = null;
	private static String s_adnom_col = null;
	private static String s_connect_col = null;
	private static String s_noun_col = null;
	private static String s_particle_col = null;
	private static String s_symbol_col = null;
	private static String s_unknown_col = null;
	private static String s_verb_col = null;
	
	private static Color c_alverb_col = null;
	private static Color c_adverb_col = null;
	private static Color c_adj_col = null;
	private static Color c_adnom_col = null;
	private static Color c_connect_col = null;
	private static Color c_noun_col = null;
	private static Color c_particle_col = null;
	private static Color c_symbol_col = null;
	private static Color c_unknown_col = null;
	private static Color c_verb_col = null;
	
	
	private void init()
	{
		if( this.b_flag )
		{
			this.s_alverb_col = "#" + MyParams.getStringValue( "alverb_col" );
			this.s_adverb_col = "#" + MyParams.getStringValue( "adverb_col" );
			this.s_adj_col = "#" + MyParams.getStringValue( "adj_col" );
			this.s_adnom_col = "#" + MyParams.getStringValue( "adnom_col" );
			this.s_connect_col = "#" + MyParams.getStringValue( "connect_col" );
			this.s_noun_col = "#" + MyParams.getStringValue( "noun_col" );
			this.s_particle_col = "#" + MyParams.getStringValue( "particle_col" );
			this.s_symbol_col = "#" + MyParams.getStringValue( "symbol_col" );
			this.s_unknown_col = "#" + MyParams.getStringValue( "unknown_col" );
			this.s_verb_col = "#" + MyParams.getStringValue( "verb_col" );
			
			this.c_alverb_col = Color.decode( this.s_alverb_col );
			this.c_adverb_col = Color.decode( this.s_adverb_col );
			this.c_adj_col = Color.decode( this.s_adj_col );
			this.c_adnom_col = Color.decode( this.s_adnom_col );
			this.c_connect_col = Color.decode( this.s_connect_col );
			this.c_noun_col = Color.decode( this.s_noun_col );
			this.c_particle_col = Color.decode( this.s_particle_col );
			this.c_symbol_col = Color.decode( this.s_symbol_col );
			this.c_unknown_col = Color.decode( this.s_unknown_col );
			this.c_verb_col = Color.decode( this.s_verb_col );
			
			this.b_flag = false;
		}
	}
	
	
	public MyAnalyzedSentensePanel( ArrayList< MyJapanWord > mjw_list ) 
	{ 
		this.mjw_list = mjw_list; 
		this.init();
	}
	
	
	public JPanel getPanel() { return this.res_panel; }
	
	
	@Override
	public void run()
	{
		this.res_panel = new JPanel();
		this.res_panel.setLayout( new BoxLayout( this.res_panel, BoxLayout.PAGE_AXIS ) );
		Border b_titled = BorderFactory.createTitledBorder( "Kuromoji say:" ); 
		this.res_panel.setBorder( b_titled );
		
		JPanel tagged_panel = this.getTaggedSentensePanelWithFurigana( mjw_list );
		
		this.res_panel.add( tagged_panel );
	}
	
	
	//Создать панель кнопок с тегированным текстом и фуриганой
	private JPanel getTaggedSentensePanelWithFurigana( ArrayList< MyJapanWord > mjw_list )
	{
		JPanel res_panel = new JPanel();
		res_panel.setBackground( Color.WHITE );
		res_panel.setBorder( BorderFactory.createLineBorder( Color.decode( "#444444" ) ) );
		
		for( MyJapanWord mjw_pos : mjw_list )
		{
			JPanel jp_pos = new JPanel();
			jp_pos.setLayout( new BoxLayout( jp_pos, BoxLayout.PAGE_AXIS ) );
			jp_pos.setBackground( Color.WHITE );
			
			JLabel furigana_label = new JLabel( mjw_pos.getHiragana() );
			if( furigana_label.getText().length() <= 0 ) { furigana_label.setText( " " ); }
			furigana_label.setFont( new Font( "Sans", Font.PLAIN, 13 ) );
			furigana_label.setAlignmentX( Component.CENTER_ALIGNMENT );
			
			JLabel wtype_label = new JLabel( mjw_pos.getType() );
			wtype_label.setFont( new Font( "Sans", Font.PLAIN, 11 ) );
			wtype_label.setAlignmentX( Component.CENTER_ALIGNMENT );
			
			JLabel word_label = new JLabel( mjw_pos.getWord() );
			word_label.setForeground( this.c_unknown_col );
			word_label.setFont( new Font( "Sans", Font.BOLD, 22 ) );
			word_label.setAlignmentX( Component.CENTER_ALIGNMENT );
			
			if( mjw_pos.getType().indexOf( "adnominal" ) != -1 ) { word_label.setForeground( this.c_adnom_col ); }
			if( mjw_pos.getType().indexOf( "noun" ) != -1 ) { word_label.setForeground( this.c_noun_col ); }
			if( mjw_pos.getType().indexOf( "verb" ) != -1 ) { word_label.setForeground( this.c_verb_col ); }
			if( mjw_pos.getType().indexOf( "alverb" ) != -1 ) { word_label.setForeground( this.c_alverb_col ); }
			if( mjw_pos.getType().indexOf( "adverb" ) != -1 ) { word_label.setForeground( this.c_adverb_col ); }
			if( mjw_pos.getType().indexOf( "adjective" ) != -1 ) { word_label.setForeground( this.c_adj_col ); }
			if( mjw_pos.getType().indexOf( "particle" ) != -1 ) { word_label.setForeground( this.c_particle_col ); }
			if( mjw_pos.getType().indexOf( "symbol" ) != -1 ) { word_label.setForeground( this.c_symbol_col ); }
			
			jp_pos.add( furigana_label );
			jp_pos.add( word_label );
			jp_pos.add( wtype_label );
			
			res_panel.add( jp_pos );
		}
		
		return res_panel;
	}
	
	
	//Создать тегированный текст
	public String getAnalyzedText( ArrayList< MyJapanWord > mjw_list )
	{
		String s_mtse_text = "<html><body style=\"font-size: 25pt\">";
		
		for( MyJapanWord mjw_pos : mjw_list ) 
		{
			if( mjw_pos.getType().indexOf( "adnominal" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_adnom_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else if( mjw_pos.getType().indexOf( "noun" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_noun_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else if( mjw_pos.getType().indexOf( "adverb" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_adverb_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else if( mjw_pos.getType().indexOf( "alverb" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_alverb_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else if( mjw_pos.getType().indexOf( "verb" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_connect_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else if( mjw_pos.getType().indexOf( "adjective" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_adj_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else if( mjw_pos.getType().indexOf( "particle" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_particle_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else if( mjw_pos.getType().indexOf( "symbol" ) != -1 ) { s_mtse_text += "<font color=\"" + this.s_symbol_col + "\">" + mjw_pos.getWord() + "</font>"; }
			else { s_mtse_text += "<font color=\"#000000\">" + mjw_pos.getWord() + "</font>"; }
		}
		
		return s_mtse_text + "</body></html>";
	}
}
