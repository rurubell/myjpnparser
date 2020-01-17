package src.ui;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;

import src.ui.panel.MyAnalyzedSentensePanel;
import src.ui.panel.*;
import src.ui.button.*;
import src.ui.win.*;
import src.ui.menu.*;
import src.ui.icon.MyIconLoader;

import src.data.MyJapanWord;
import src.jap.tokenizer.MyTokenizer;
import src.jap.tokenizer.kuromoji.MyKuromoji;
import src.jap.simkanji.MySimKanji;
import src.jap.simwords.MySimWords;
import src.jap.autorepl.*;
import src.jap.wordstat.MyWordStat;

import com.mariten.kanatools.*;


public class MyMainWin extends JFrame
{
	public MyMainWin p_self_link = this;
	public JPanel main_panel = new JPanel();
	
	//Список слов после парсинга их куромодзи
	ArrayList< MyJapanWord > mjw_list = null;
	
	//Кнопки
	private JButton jb_tage_and_translate = new JButton();
	private JButton jb_crop = new JButton();
	private JButton jb_del_latin = new JButton( );
	private JButton jb_to_hiragana = new JButton();
	private JButton jb_to_katakana = new JButton();
	private JButton jb_add_sim = new JButton();
	private JButton jb_add_autorepl = new JButton();
	private JButton jb_del_autorepl = new JButton();
	private JButton jb_search_for_word = new JButton();
	
	JButton redo_butt = new JButton();
	JButton undo_butt = new JButton();
	
	//Ссылка на панель с тегированным текстом
	MyAnalyzedSentensePanel masp = null;
	//Ссылка на панель со словарем
	MyDictionaryPanel mjp = null;
	//Ссылка на панель с переводом
	MyTranslateShellPanel mtsp = null;
	//Окно, со списком похожих канзи
	MySimKanjiWin mskw = null;
	//Окно, со списком похожих слов
	MySimWordWin msww = null;
	//Окно для выделения слова
	MyUndecoratedSelectionWindow musw = null;
	
	//Список субокон главного окна
	ArrayList< JFrame > sub_frames_al = new ArrayList< JFrame >();
	
	UndoManager um = new UndoManager();
	MyAutoReplacement mar;
	
	private static int n_gc_flag = 0;
	
	
	public MyMainWin( String s_sentense, int n_width, int n_height, int n_voffset, int n_hoffset, String s_ocr_type )
	{
		this.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
		this.addWindowListener( new WAClose() );
		this.setLocation( 100, 100 );
		
		if( s_ocr_type.indexOf( "tesseract" ) != -1 ) 
		{ 
			this.setTitle( "tesseract - " + s_sentense.replace( "\n", "" ) );
			this.mar = new MyTesseractAutoRepl();
			this.musw = new MyUndecoratedSelectionWindow( n_voffset, n_hoffset, n_width, n_height, Color.decode( "#FF0000" ) ); 
		}
		else if( s_ocr_type.indexOf( "ocrspace" ) != -1 ) 
		{ 
			this.setTitle( "ocrspace - " + s_sentense.replace( "\n", "" ) );
			this.mar = new MyOCRSpaceAutoRepl();
			this.musw = new MyUndecoratedSelectionWindow( n_voffset, n_hoffset, n_width, n_height, Color.decode( "#00FF00" ) );
		}
		
		s_sentense = mar.handleText( s_sentense );
		
		JTextPane recogn_text_pane = this.makeRecognizedPanel( s_sentense );
		JPanel buttons_panel = this.makeButtonPanel( recogn_text_pane );
		
		this.main_panel.setLayout( new BoxLayout( main_panel, BoxLayout.PAGE_AXIS ) );
		this.main_panel.add( this.makeMainPanel( recogn_text_pane, buttons_panel ) );
		
		this.add( main_panel );
		this.pack();
	}
	
	
	//Создать главную панель
	private JPanel makeMainPanel( JTextPane recogn_text_pane, JPanel buttons_panel )
	{
		JPanel res_panel = new JPanel( new GridBagLayout() );
		Border b_titled = BorderFactory.createTitledBorder( "OCR say:" );
		res_panel.setBorder( b_titled );
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		//c.insets = new Insets( 5, 5, 5, 5 );
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 999.0;
		res_panel.add( recogn_text_pane, c );
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		res_panel.add( buttons_panel, c );
		
		return res_panel;
	}
	
	
	//Создать панель с полем распознанного текста
	private JTextPane makeRecognizedPanel( String s_sentense )
	{
		JTextPane recogn_text_pane = new JTextPane();
		Border b_comp_bord = new CompoundBorder
		(
			BorderFactory.createLineBorder( Color.decode( "#444444" ) ), 
			BorderFactory.createMatteBorder( 5, 5, 5, 5, Color.decode( "#FFFFFF" ) )
		);
		recogn_text_pane.setBorder( b_comp_bord );
		recogn_text_pane.setBorder( b_comp_bord );
		recogn_text_pane.addCaretListener( new CLSimKanji( recogn_text_pane ) );
		recogn_text_pane.setContentType( "text/html" );
		recogn_text_pane.setFont( new Font( "Sans", Font.BOLD, 16 ) );
		s_sentense = s_sentense.replace( "\n", "<br/>" );
		recogn_text_pane.setText( "<html><body style=\"font-size: 25pt\">" + s_sentense + "</body></html>" );
		recogn_text_pane.getDocument().addUndoableEditListener( this.um );
		recogn_text_pane.getDocument().addDocumentListener( new DLRecognTextTextPaneChange() );
		
		InputMap imap = recogn_text_pane.getInputMap();
		imap.put( KeyStroke.getKeyStroke( "ctrl Z" ), "undo" );
		imap.put( KeyStroke.getKeyStroke( "ctrl Y" ), "redo" );
		
		ActionMap amap = recogn_text_pane.getActionMap();
		amap.put( "undo", undo );
		amap.put( "redo", redo );
		
		//Меню для текстового поля
		MyRecognizedJTextPaneMenu mjtfm = new MyRecognizedJTextPaneMenu( recogn_text_pane, this.sub_frames_al );
		recogn_text_pane.setComponentPopupMenu( mjtfm );
		
		return recogn_text_pane;
	}
	
	
	//Создать панель кнопок редактирования
	private JPanel makeEditButtonsPanel( JTextPane recogn_text_pane )
	{
		JPanel res_panel = new JPanel();
		Border b_border = BorderFactory.createLineBorder( Color.decode( "#999999" ) );
		res_panel.setBorder( b_border );
		
		MyIconLoader mil = new MyIconLoader();
		
		this.jb_crop.setIcon( mil.getIcon( "crop.png" ) );
		this.jb_del_latin.setIcon( mil.getIcon( "del_latin.png" ) );
		this.jb_to_hiragana.setIcon( mil.getIcon( "to_hiragana.png" ) );
		this.jb_to_katakana.setIcon( mil.getIcon( "to_katakana.png" ) );
		this.jb_add_sim.setIcon( mil.getIcon( "add_similar.png" ) );
		this.jb_add_autorepl.setIcon( mil.getIcon( "add_autorepl.png" ) );
		this.jb_del_autorepl.setIcon( mil.getIcon( "delete_autorepl.png" ) );
		this.jb_search_for_word.setIcon( mil.getIcon( "search_for_word.png" ) );
		
		this.undo_butt.setIcon( mil.getIcon( "undo.png" ) );
		this.redo_butt.setIcon( mil.getIcon( "redo.png" ) );
		
		this.jb_crop.setToolTipText( "Crop" );
		this.jb_del_latin.setToolTipText( "Delete latin symbols" );
		this.jb_to_hiragana.setToolTipText( "To Hiragana" );
		this.jb_to_katakana.setToolTipText( "To Katakana" );
		this.jb_add_sim.setToolTipText( "Add similar symbol" );
		this.jb_add_autorepl.setToolTipText( "Add auto replacement for symbol/word" );
		this.jb_del_autorepl.setToolTipText( "Delete auto replacement" );
		this.jb_search_for_word.setToolTipText( "Search for Word" );
		
		undo_butt.setToolTipText( "undo" );
		redo_butt.setToolTipText( "redo" );
		
		this.jb_crop.addActionListener( new ALCrop( recogn_text_pane ) );
		this.jb_del_latin.addActionListener( new ALDelLatin( recogn_text_pane ) );
		this.jb_to_hiragana.addActionListener( new ALToHiragana( recogn_text_pane ) );
		this.jb_to_katakana.addActionListener( new ALToKatakana( recogn_text_pane ) );
		this.jb_add_sim.addActionListener( new ALAddSimilar( recogn_text_pane ) );
		this.jb_add_autorepl.addActionListener( new ALAddAutoReplacement( recogn_text_pane ) );
		this.jb_del_autorepl.addActionListener( new ALDeleteAutoReplacement( recogn_text_pane ) );
		this.jb_search_for_word.addActionListener( new ALSearchSimilarWord( recogn_text_pane ) );
		
		this.undo_butt.setEnabled( false );
		this.undo_butt.addActionListener( new ALUndo() );
		this.redo_butt.setEnabled( false );
		this.redo_butt.addActionListener( new ALRedo() );
		
		res_panel.add( this.jb_crop );
		res_panel.add( this.jb_to_hiragana );
		res_panel.add( this.jb_to_katakana );
		res_panel.add( new JLabel( " " ) );
		res_panel.add( this.jb_add_sim );
		res_panel.add( new JLabel( " " ) );
		res_panel.add( this.jb_add_autorepl );
		res_panel.add( this.jb_del_autorepl );
		res_panel.add( new JLabel( " " ) );
		res_panel.add( this.jb_search_for_word );
		res_panel.add( new JLabel( " " ) );
		res_panel.add( this.undo_butt );
		res_panel.add( this.redo_butt );
		
		return res_panel;
	}
	
	
	//Создать панель кнопки GO
	private JPanel makeGOButtonPanel( JTextPane recogn_text_pane )
	{
		this.jb_tage_and_translate.setText( "GO!" );
		this.jb_tage_and_translate.setFont( this.jb_tage_and_translate.getFont().deriveFont( Font.BOLD ) );
		
		JPanel go_butt_panel = new JPanel( new GridBagLayout() );
		Border b_border = BorderFactory.createLineBorder( Color.decode( "#999999" ) );
		go_butt_panel.setBorder( b_border );
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 5, 5, 5, 5 );
		
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		this.jb_tage_and_translate.addActionListener( new ALTagAndTranslate( recogn_text_pane ) );
		go_butt_panel.add( this.jb_tage_and_translate, c );
		
		return go_butt_panel;
	}
	
	
	//Создать панель кнопок
	private JPanel makeButtonPanel( JTextPane recogn_text_pane )
	{
		JPanel button_panel = new JPanel();
		
		GridBagLayout gbl = new GridBagLayout();
		button_panel.setLayout( gbl );
		
		JPanel edit_butt_panel = this.makeEditButtonsPanel( recogn_text_pane );
		JPanel go_butt_panel = this.makeGOButtonPanel( recogn_text_pane );
		
		GridBagConstraints gbc =  new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets( 5, 5, 5, 5 );
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		//gbl.setConstraints( edit_butt_panel, gbc );
		button_panel.add( edit_butt_panel, gbc );
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 99.0;
		gbc.weighty = 1.0;
		
		JButton jb = new JButton( "GO!" );
		
		//gbl.setConstraints( jb, gbc );
		button_panel.add( go_butt_panel, gbc );
		
		return button_panel;
	}
	
	
	//Событые нажатия на кнопку "Tag and Translate"
	public class ALTagAndTranslate implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALTagAndTranslate( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			String s_text = "";
			try { s_text = this.text_pane.getDocument().getText( 0, text_pane.getDocument().getLength() ); }
			catch( Exception e ) { e.printStackTrace(); }
			
			MyTokenizer mjta = new MyKuromoji();
			s_text = s_text.replace( "\n", "" );
			s_text = s_text.replace( " ", "" );
			mjw_list = mjta.getWordsArrayList( s_text );
			
			if( masp != null ) { main_panel.remove( masp.getPanel() ); masp = null; }
			if( mjp != null ) { main_panel.remove( mjp.getPanel() ); mjp = null; }
			if( mtsp != null ) { main_panel.remove( mtsp.getPanel() ); mtsp = null; }
			
			masp = new MyAnalyzedSentensePanel( mjw_list );
			mjp = new MyDictionaryPanel( mjw_list, sub_frames_al );
			mtsp = new MyTranslateShellPanel( s_text, sub_frames_al );
			this.text_pane.setText( masp.getAnalyzedText( mjw_list ) );
			
			try
			{
				masp.start();
				mjp.start();
				mtsp.start();
				masp.join();
				mjp.join();
				mtsp.join();
			}
			catch( Exception e ) { e.printStackTrace(); }
			
			main_panel.add( masp.getPanel() );
			main_panel.add( mjp.getPanel() );
			main_panel.add( mtsp.getPanel() );
			main_panel.repaint();
			main_panel.revalidate();
			
			if( msww != null ) { msww.dispose(); }
			
			p_self_link.pack();
		}
	}
	
	
	//Событие при выделении текста в поле распознанныхъ кандзи
	public class CLSimKanji implements CaretListener 
	{
		private JTextPane text_pane = null;
		public CLSimKanji( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void caretUpdate( CaretEvent ce )
		{
			MySimKanji msk = new MySimKanji();
			ArrayList< String > s_similar = msk.getSimilar( this.text_pane.getSelectedText() );
			
			try{ mskw.setVisible( false ); } catch ( Exception e ) {}
			
			if( ( s_similar != null ) && ( s_similar.size() > 0 ) ) //Если есть похожие кандзи, показываем их
			{
				mskw = new MySimKanjiWin( s_similar, this.text_pane );
				mskw.setVisible( true );
			}
		}
	}
	
	
	//Событие при закрытии окна
	public class WAClose extends WindowAdapter
	{
		public void windowClosing( WindowEvent we )
		{
			if( mjw_list != null )
			{
				MyWordStat mws = new MyWordStat();
				mws.putWordsToStaticFile( mjw_list );
			}
			
			try { mskw.dispose(); } catch ( Exception e ) {}
			try
			{
				for( JFrame jf_pos : sub_frames_al ) { jf_pos.dispose(); }
			}
			catch ( Exception e ) {}
			//p_self_link.setVisible( false );
			
			if( masp != null ) { p_self_link.remove( masp.getPanel() ); }
			if( mjp != null ) { p_self_link.remove( mjp.getPanel() ); }
			if( mtsp != null ) { p_self_link.remove( mtsp.getPanel() ); }
			if( mskw != null ) { mskw.dispose(); }
			if( musw != null ) { musw.dispose(); }
			if( msww != null ) { msww.dispose(); }
			
			p_self_link.dispose();
			
			//Раз в 10 распознаваний, вызываем GC
			if( n_gc_flag == 10 ) 
			{
				System.gc(); 
				n_gc_flag = 0; 
			}
			n_gc_flag++;
		}
	}
	
	
	//Хендлер кнопки - Del Latin
	public class ALDelLatin implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALDelLatin( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			String s_text = "";
			try { s_text = this.text_pane.getDocument().getText( 0, text_pane.getDocument().getLength() ); }
			catch( Exception e ) { e.printStackTrace(); }
			String s_latin = "!?.,\'\"\\|+=-)({}[]<>!@#$%^&*~`′";
			
			for( int i = 0; i < s_latin.length(); i++ )
			{
				s_text = s_text.replace( s_latin.substring( i, i + 1 ), "" );
			}
			
			this.text_pane.setText( "<html><body style=\"font-size: 25pt\">" + s_text + "</body></html>" );
		}
	}
	
	
	//Crop
	public class ALCrop implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALCrop( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			String s_text = this.text_pane.getSelectedText();
			if( s_text != null ) { this.text_pane.setText( "<html><body style=\"font-size: 25pt\">" + s_text + "</body></html>" ); }
		}
	}
	
	
	//Хендлер кнопки - Добавить замен для выделенного символа/Строки
	public class ALAddSimilar implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALAddSimilar( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			try{ mskw.setVisible( false ); } catch ( Exception e ) {}
			String s_text = this.text_pane.getSelectedText();
			if( s_text != null )
			{
				MyAddNewSimKanjiWin manskw = new MyAddNewSimKanjiWin( s_text );
				manskw.setVisible( true );
			}
		}
	}
	
	
	//Хендлер кнопки - Добавить автозамену для выделенного символа/Строки
	public class ALAddAutoReplacement implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALAddAutoReplacement( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			try{ mskw.setVisible( false ); } catch ( Exception e ) {}
			String s_text = this.text_pane.getSelectedText();
			if( s_text != null )
			{
				MyAddAddAutoReplacementWin manskw = new MyAddAddAutoReplacementWin( s_text, mar );
				manskw.setVisible( true );
			}
		}
	}
	
	
	//Хендлер кнопки - Удалить автозамену для выделенного символа/Строки
	public class ALDeleteAutoReplacement implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALDeleteAutoReplacement( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			String s_text = this.text_pane.getSelectedText();
			if( s_text != null )
			{
				String s_key = mar.getKeyForAutoReplacementValue( s_text );
				
				if( s_key == null ) {}
				else
				{
					MyDeleteAutoReplacementWin mdarw = new MyDeleteAutoReplacementWin( s_key, s_text, mar );
					mdarw.setVisible( true );
				}
			}
		}
	}
	
	
	//Найти похожие по набору символов слова в словаре
	public class ALSearchSimilarWord implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALSearchSimilarWord( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			String s_text = this.text_pane.getSelectedText();
			if( ( s_text == null ) || ( s_text.length() <= 0 ) ) {}
			else //Иначе ищем похожие слова в MySimWords
			{
				try{ msww.setVisible( false ); } catch ( Exception e ) {}
				msww = new MySimWordWin( s_text, this.text_pane );
				msww.setVisible( true );
			}
		}
	}
	
	
	//Хендлер кнопки - вернуть назад
	public class ALUndo implements ActionListener 
	{
		public void actionPerformed( ActionEvent ae ) 
		{
			if( um.canUndo() ) { um.undo(); }
			if( !um.canUndo() ) { undo_butt.setEnabled( false ); }
			
			if( um.canRedo() ) { redo_butt.setEnabled( true ); }
		}
	}
	
	
	//Хендлер кнопки - вернуть вперед
	public class ALRedo implements ActionListener 
	{
		public void actionPerformed( ActionEvent ae ) 
		{
			if( um.canRedo() ) { um.redo(); }
			if( !um.canRedo() ) { redo_butt.setEnabled( false ); }
			
			if( um.canUndo() ) { undo_butt.setEnabled( true ); }
		}
	}
	
	
	//Хендлер кнопки - перегнать в хирагану
	public class ALToHiragana implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALToHiragana( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			int n_start = this.text_pane.getSelectionStart();
			int n_end = this.text_pane.getSelectionEnd();
			String s_fragment_text = this.text_pane.getSelectedText();
			
			String s_text = "";
			try { s_text = this.text_pane.getDocument().getText( 0, text_pane.getDocument().getLength() ); }
			catch( Exception e ) { e.printStackTrace(); }
			
			if( s_fragment_text != null ) 
			{
				String s_start_fragment = s_text.substring( 0, n_start );
				String s_end_fragment = s_text.substring( n_end );
				
				s_fragment_text = KanaConverter.convertKana( s_fragment_text, KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA );
				this.text_pane.setText
				( 
					"<html><body style=\"font-size: 25pt\">" + 
					s_start_fragment + 
					s_fragment_text + 
					s_end_fragment + 
					"</body></html>" 
				); 
			}
		}
	}
	
	
	//Хендлер кнопки - перегнать в катакану
	public class ALToKatakana implements ActionListener 
	{
		private JTextPane text_pane = null;
		public ALToKatakana( JTextPane text_pane ) { this.text_pane = text_pane; }
		
		public void actionPerformed( ActionEvent ae )
		{
			int n_start = this.text_pane.getSelectionStart();
			int n_end = this.text_pane.getSelectionEnd();
			String s_fragment_text = this.text_pane.getSelectedText();
			
			String s_text = "";
			try { s_text = this.text_pane.getDocument().getText( 0, text_pane.getDocument().getLength() ); }
			catch( Exception e ) { e.printStackTrace(); }
			
			if( s_fragment_text != null ) 
			{
				String s_start_fragment = s_text.substring( 0, n_start );
				String s_end_fragment = s_text.substring( n_end );
				
				s_fragment_text = KanaConverter.convertKana( s_fragment_text, KanaConverter.OP_ZEN_HIRA_TO_ZEN_KATA );
				this.text_pane.setText
				( 
					"<html><body style=\"font-size: 25pt\">" + 
					s_start_fragment + 
					s_fragment_text + 
					s_end_fragment + 
					"</body></html>" 
				); 
			}
		}
	}
	
	
	//Действия при изменении текста в текстовой панели
	public class DLRecognTextTextPaneChange implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e) 
		{
			undo_butt.setEnabled( true );
			if( um.canRedo() ) { redo_butt.setEnabled( true ); } 
		}
		
		public void removeUpdate(DocumentEvent e)
		{
			undo_butt.setEnabled( true );
			if( um.canRedo() ) { redo_butt.setEnabled( true ); }
		}
		
		public void insertUpdate(DocumentEvent e)
		{
			undo_butt.setEnabled( true );
			if( um.canRedo() ) { redo_butt.setEnabled( true ); }
		}
	}
	
	
	Action undo = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e) { if( um.canUndo() ) { um.undo(); } }
	};
	
	
	Action redo = new AbstractAction()
	{
		public void actionPerformed(ActionEvent e) { if( um.canRedo() ) { um.redo(); } }
	};
}
