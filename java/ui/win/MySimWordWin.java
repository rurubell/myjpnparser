package src.ui.win;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;

import src.ui.panel.MyAnalyzedSentensePanel;
import src.ui.button.*;
import src.jap.simwords.MySimWords;


public class MySimWordWin extends JFrame
{
	//Вертикальный оффсет на котором помещаем окно, относительно курсора
	private int H_OFFSET = 100;
	//Число кнопок в строке
	private int N_BUTTONS_IN_ROW = 10;
	//Ссылка на себя
	private JFrame jf_self = this;
	
	//Поле ввода текста для поиска
	private JTextField tf_search = new JTextField();
	//Кнопка поиска
	private JButton jb_search = new JButton( "Search" );
	//Кнопка замены символа или группы символов на *
	private JButton jb_replace = new JButton( "*" );
	//Кнопка подтверждения
	private JButton jb_ok = new JButton( "OK" );
	//Список со словами
	JList jl_words = null;
	//Панель со списокм со словами
	JPanel jp_words = null;
	
	
	JTextPane mrse_link = null;
	JFrame this_link = this;
	
	
	public MySimWordWin( String s_word, JTextPane mrse_link )
	{
		this.mrse_link = mrse_link;
		this.setTitle( "Replace any symbol with * and press \"Search\"" );
		this.setLayout( new GridBagLayout() );
		this.setMinimumSize( new Dimension( 500, 500 ) );
		
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point p = pi.getLocation();
		int x = (int) p.getX();
		int y = (int) p.getY();
		this.setLocation( x, y - H_OFFSET );
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		this.add( this.makeSearchPanel( s_word ), c );
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.weighty = 999.0;
		
		this.jp_words = this.makeSearchResultPanel();
		this.add( this.jp_words, c );
		
		this.pack();
	}
	
	
	//Создать панель поиска
	private JPanel makeSearchPanel( String s_word )
	{
		JPanel res_panel = new JPanel( new GridBagLayout() );
		Border b_titled = BorderFactory.createTitledBorder( "Search" );
		res_panel.setBorder( b_titled );
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 5, 5, 5, 5 );
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		this.jb_replace.addActionListener( new ALReplace() );
		res_panel.add( this.jb_replace, c );
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 20.0;
		c.weighty = 1.0;
		
		this.tf_search.setFont( new Font( "SansSerif", Font.BOLD, 20 ) );
		this.tf_search.setText( s_word );
		res_panel.add( this.tf_search, c );
		
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		this.jb_search.addActionListener( new ALSearch() );
		res_panel.add( this.jb_search, c );
		
		c.gridx = 3;
		c.gridy = 0;
		this.jb_ok.addActionListener( new ALOk() );
		this.jb_ok.setFont( new Font( "SansSerif", Font.BOLD, 12 ) );
		res_panel.add( this.jb_ok, c );
		
		return res_panel;
	}
	
	
	//Создать панель с результатами
	private JPanel makeSearchResultPanel()
	{
		JPanel res_panel = new JPanel();
		res_panel.setLayout( new BoxLayout( res_panel, BoxLayout.Y_AXIS ) );
		Border b_titled = BorderFactory.createTitledBorder( "Search result" );
		res_panel.setBorder( b_titled );
		
		return res_panel;
	}
	
	
	//Создать лист со списком похожих слов
	private JList makeListWithWords( ArrayList< String > al_words )
	{
		DefaultListModel< String > list_model = new DefaultListModel<>();
		for( String s_pos : al_words ) { list_model.addElement( s_pos ); }
		JList res_list = new JList<>( list_model );
		res_list.setFont( new Font( "SansSerif", Font.BOLD, 20 ) );
		res_list.setLayoutOrientation( JList.HORIZONTAL_WRAP );
		res_list.setVisibleRowCount( al_words.size()/8 );
		
		return res_list;
	}
	
	
	//Замена символа ( символов на * )
	public class ALReplace implements ActionListener 
	{
		public void actionPerformed( ActionEvent ae )
		{
			String s_selected = tf_search.getSelectedText();
			if( ( s_selected != null ) && ( s_selected.length() > 0 ) && ( s_selected.length() != tf_search.getText().length() ) )
			{
				String s_dot_str = new String();
				for( int i = 0; i < s_selected.length(); i++ ) { s_dot_str += "*"; }
				tf_search.replaceSelection( s_dot_str );
			}
		}
	}
	
	
	//Поиск
	public class ALSearch implements ActionListener 
	{
		public void actionPerformed( ActionEvent ae )
		{
			MySimWords msw = new MySimWords();
			ArrayList< String > s_strings_al = msw.getSimilarWords( tf_search.getText() );
			
			if( jp_words != null )
			{
				jp_words.removeAll();
				jp_words.revalidate();
				jp_words.repaint();
			}
			
			jl_words = makeListWithWords( s_strings_al );
			JScrollPane jsp_words = new JScrollPane( jl_words );
			//jsp_words.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
			//jsp_words.setHorizontalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
			jp_words.add( jsp_words );
			jp_words.revalidate();
			jp_words.repaint();
			jf_self.pack();
		}
	}
	
	
	//Ок
	public class ALOk implements ActionListener 
	{
		public void actionPerformed( ActionEvent ae )
		{
			if( jl_words == null ) { return; }
			String s_value = ( String ) jl_words.getSelectedValue();
			if( s_value != null )
			{
				mrse_link.replaceSelection( s_value );
				jf_self.dispose();
			}
		}
	}
}
