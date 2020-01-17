package src.ui.panel;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;

import src.ui.panel.MyPanel;
import src.ui.menu.*;
import src.data.MyJapanWord;
import src.ui.win.*;
import src.util.string.MyStringUtils;
import src.util.log.MyLogToStdout;

import src.data.MyTranslation;
import src.jap.dict.MyDict;
import src.jap.dict.jmdict.MyJMDICT;
import src.jap.dict.edict2.MyEdict2;
import src.jap.dict.jisho.MyJisho;
import src.jap.dict.edict2_light.MyEdict2_Light;


public class MyDictionaryPosPanel extends MyPanel
{
	//Словарь
	MyDict dict = new MyEdict2_Light();
	
	MyJapanWord mjw = null;
	//Панель которую вернем
	private JPanel res_panel = null;
	
	//Цвета слова
	private Color c_alverb_col = Color.decode( "#6600E3" );
	private Color c_adverb_col = Color.decode( "#983FFF" );
	private Color c_adj_col = Color.decode( "#007F15" );
	private Color c_adnom_col = Color.decode( "#BFD000" );
	private Color c_connect_col = Color.decode( "#2C0093" );
	private Color c_noun_col = Color.decode( "#930002" );
	private Color c_particle_col = Color.decode( "#D09200" );
	private Color c_symbol_col = Color.decode( "#000000" );
	private Color c_unknown_col = Color.decode( "#000000" );
	private Color c_verb_col = Color.decode( "#2C0093" );
	
	//Основной цвет панели
	private Color c_main_color = null;
	//Цвета панелей сложного слова
	private Color c_word_color = Color.decode( "#FFF1EE" );
	private Color c_base_word_color = Color.decode( "#EEFFF6" );
	
	//Список субокон главного окна
	ArrayList< JFrame > sub_frames_al = null;
	
	
	public MyDictionaryPosPanel( MyJapanWord mjw, Color c_main_color, ArrayList< JFrame > sub_frames_al ) 
	{ 
		this.mjw = mjw;
		this.c_main_color = c_main_color;
		this.sub_frames_al = sub_frames_al;
	}
	public JPanel getPanel() { return this.res_panel; }
	
	
	@Override
	public void run()
	{
		if( this.mjw.isSimple() ) { this.res_panel = this.panelForSimpleWord( this.mjw, this.c_main_color ); }
		else { this.res_panel = this.panelForMultipleWord( this.mjw, this.c_main_color ); }
	}
	
	
	//Создать панель для слова
	private JPanel makePanelForWord( String s_word, String s_reading, String s_translation, Color c_panel_color )
	{
		//MyLogToStdout mlts = new MyLogToStdout();
		
		FlowLayout layout = new FlowLayout( FlowLayout.LEFT );
		layout.setVgap( 0 );
		layout.setHgap( 0 );
		JPanel res_panel = new JPanel( layout );
		res_panel.setBackground( c_panel_color );
		
		res_panel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
		res_panel.add( makeSmallLabelWithWord( s_word, s_reading ) );
		res_panel.add( this.makeFastTranslButtonsPanel( s_word, c_panel_color ) );
		res_panel.add( this.makePaneWithText( s_translation ) );
		
		//mlts.writeMess( "Dict panel \"" + s_word + "\" ready!" );
		return res_panel;
	}
	
	
	//Создать панель для простого слова
	private JPanel panelForSimpleWord( MyJapanWord mjw, Color c_panel_color )
	{
		MyTranslation mt = dict.getTranslation( mjw.getWord() );
		
		FlowLayout layout = new FlowLayout( FlowLayout.LEFT );
		layout.setVgap( 0 );
		JPanel res_panel = new JPanel( layout );
		res_panel.setBackground( c_panel_color );
		res_panel.setBorder( BorderFactory.createMatteBorder( 1, 1, 1, 1, Color.decode( "#7A8A99" ) ) );
		
		res_panel.add( this.makeBigLabelWithWord( this.mjw ) );
		res_panel.add( this.makePanelForWord( mjw.getWord(), mjw.getHiragana(), mt.getEnTranslation(), c_panel_color ) );
		
		return res_panel;
	}
	
	
	//Создать панель для сложного слова
	private JPanel panelForMultipleWord( MyJapanWord mjw, Color c_panel_color )
	{
		MyTranslation mt = dict.getTranslation( mjw.getWord() );
		MyTranslation mt_base = dict.getTranslation( mjw.getBaseForm() );
		
		JPanel res_panel = new JPanel();
		BoxLayout res_layout = new BoxLayout( res_panel, BoxLayout.LINE_AXIS );
		res_panel.setLayout( res_layout );
		res_panel.setBackground( c_panel_color );
		res_panel.setBorder( BorderFactory.createMatteBorder( 1, 1, 1, 1, Color.decode( "#7A8A99" ) ) );
		
		JPanel sub_panel = new JPanel();
		BoxLayout sub_layout = new BoxLayout( sub_panel, BoxLayout.Y_AXIS );
		sub_panel.setLayout( sub_layout );
		
		JPanel word_panel = this.makePanelForWord( mjw.getWord(), mjw.getHiragana(), mt.getEnTranslation(), c_panel_color );
		JPanel base_word_panel =  this.makePanelForWord( mjw.getBaseForm(), mt_base.getKana(), mt_base.getEnTranslation(), Color.decode( "#DAFFD9" ) );
		
		sub_panel.add( word_panel );
		sub_panel.add( base_word_panel );
		
		res_panel.add( this.makeBigLabelWithWord( this.mjw ) );
		res_panel.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
		res_panel.add( sub_panel );
		
		return res_panel;
	}
	
	
	//Создать JTextField с текстом
	private JTextPane makePaneWithText( String s_text )
	{
		MyStringUtils msu = new MyStringUtils();
		JTextPane res_jtp = new JTextPane();
		res_jtp.setFont (new Font( "Noto Sans", Font.PLAIN, 12 ) );
		
		//s_text = msu.splitOnTwo( s_text, ", ", 60 );
		
		res_jtp.setEditable( false );
		res_jtp.setBackground( null );
		res_jtp.setBorder( null );
		res_jtp.setText( s_text );
		
		MyJishoPostPanelJTextPaneMenu mjppjtpm = new MyJishoPostPanelJTextPaneMenu( res_jtp, this.sub_frames_al );
		res_jtp.setComponentPopupMenu( mjppjtpm );
		return res_jtp;
	}
	
	
	//Создать большую метку со словом
	private JLabel makeBigLabelWithWord( MyJapanWord mjw )
	{
		JLabel res_label = new JLabel( mjw.getWord() );
		Color col = Color.decode( "#000000" );
		
		if( mjw.getType().indexOf( "none" ) != -1 ) { col = this.c_unknown_col; }
		if( mjw.getType().indexOf( "noun" ) != -1 ) { col = this.c_noun_col; }
		if( mjw.getType().indexOf( "verb" ) != -1 ) { col = this.c_verb_col; }
		if( mjw.getType().indexOf( "adnominal" ) != -1 ) { col = this.c_adnom_col; }
		if( mjw.getType().indexOf( "particle" ) != -1 ) { col = this.c_particle_col; }
		if( mjw.getType().indexOf( "adjective" ) != -1 ) { col = this.c_adj_col; }
		if( mjw.getType().indexOf( "adverb" ) != -1 ) { col = this.c_adverb_col; }
		if( mjw.getType().indexOf( "alverb" ) != -1 ) { col = this.c_alverb_col; }
		
		res_label.setFont( new Font( "Sans", Font.BOLD, 21 ) );
		res_label.setForeground( col );
		return res_label;
	}
	
	
	//Создать маленькую метку со словом
	private JLabel makeSmallLabelWithWord( String s_word, String s_reading )
	{
		String s_text = 
			"<html>" +
			s_word + "<font color=\"#C472C3\"><i> [" + s_reading + "]</i></font></html>";
		
		JLabel res_label = new JLabel( s_text );
		res_label.setFont( new Font( "Sans", Font.BOLD, 16 ) );
		return res_label;
	}
	
	
	//Создать панель кнопок быстрого перевода для слова
	private JPanel makeFastTranslButtonsPanel( String s_word, Color c_panel_color )
	{
		JPanel res_panel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		res_panel.setBackground( c_panel_color );
		
		JButton jardic_butt = new JButton( "j" );
		jardic_butt.setFont (new Font( "Sans", Font.PLAIN, 8 ) );
		jardic_butt.setMargin( new java.awt.Insets( 1, 1, 1, 1 ) );
		jardic_butt.addActionListener( new ALJardicTransl( s_word ) );
		
		JButton ulib_butt = new JButton( "u" );
		ulib_butt.setFont( new Font( "Sans", Font.PLAIN, 8 ) );
		ulib_butt.setMargin( new java.awt.Insets( 1, 1, 1, 1) );
		ulib_butt.addActionListener( new ALULIBTransl( s_word ) );
		
		res_panel.add( jardic_butt );
		res_panel.add( ulib_butt );
		
		return res_panel;
	}
	
	
	//Перевод с помощью jardic
	public class ALJardicTransl implements ActionListener 
	{
		String s_word = null;
		public ALJardicTransl( String s_word ) { this.s_word = s_word; }
		
		public void actionPerformed( ActionEvent ae )
		{
			if( s_word != null ) 
			{
				MyJapToRuJardicTranslateWin mjtretw = new MyJapToRuJardicTranslateWin( s_word );
				mjtretw.setVisible( true );
				sub_frames_al.add( mjtretw );
			}
		}
	}
	
	
	//Перевод с помощью u-lib
	public class ALULIBTransl implements ActionListener 
	{
		String s_word = null;
		public ALULIBTransl( String s_word ) { this.s_word = s_word; }
		
		public void actionPerformed( ActionEvent ae )
		{
			if( s_word != null ) 
			{
				MyJapToRuELIBTranslateWin mjtretw = new MyJapToRuELIBTranslateWin( s_word );
				mjtretw.setVisible( true );
				sub_frames_al.add( mjtretw );
			}
		}
	}
}
