package src.ui.win;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;

import src.ui.panel.MyAnalyzedSentensePanel;
import src.ui.button.*;
import src.jap.simkanji.MySimKanji;


public class MyAddNewSimKanjiWin extends JFrame
{
	JFrame this_link = this;
	//Вертикальный оффсет на котором помещаем окно, относительно курсора
	private int H_OFFSET = 100;
	//Ширина одной буквы, для растягивания по длинне текста
	private int N_CHAR_WIDTH = 20;
	
	
	public MyAddNewSimKanjiWin( String s_str )
	{
		MySimKanji msk = new MySimKanji();
		ArrayList< String > s_similar = msk.getSimilar( s_str );
		
		JPanel main_panel = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		main_panel.setLayout(gbl);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 5, 5, 5, 5 );
		
		//Метка
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 20.0;
		JLabel main_label = new JLabel( "<html><span style='font-size:60px'>" + s_str + "</span></html>" );
		JPanel label_panel = new JPanel( new BorderLayout() );
		main_label.setHorizontalAlignment(JLabel.CENTER);
		main_label.setVerticalAlignment(JLabel.CENTER);
		label_panel.add( main_label, BorderLayout.CENTER );
		main_panel.add( label_panel, c );
		
		//Метка Similar Symbols
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		JLabel sims_label = new JLabel( "Edit Similar:" );
		sims_label.setHorizontalAlignment(JLabel.RIGHT);
		sims_label.setVerticalAlignment(JLabel.CENTER);
		main_panel.add( sims_label, c );
		
		//Поле ввода
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 20.0;
		c.weighty = 1.0;
		String s_text = "";
		
		if( s_similar == null ) {}
		else
		{
			for( int i = 0; i < s_similar.size(); i++ )
			{
				if( ( i + 1 ) < s_similar.size() ) { s_text += s_similar.get(i) + "\n"; }
				else { s_text += s_similar.get(i); }
			}
		}
		
		JTextPane sim_pane = new JTextPane();
		sim_pane.setText( s_text );
		sim_pane.setFont( new Font( "Sans", Font.BOLD, 25 ) );
		main_panel.add( sim_pane, c );
		
		//Кнопка
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		JButton save_button = new JButton( "Save" );
		save_button.addActionListener( new ALSave( s_str, sim_pane ) );
		main_panel.add( save_button, c );
		
		this.add( main_panel );
		this.setMinimumSize( new Dimension( 300, 0 ) );
		this.pack();
		
		//Получаем текущие координаты курсора
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point p = pi.getLocation();
		int x = (int) p.getX();
		int y = (int) p.getY();
		this.setLocation( x, y - H_OFFSET );
	}
	
	
	//Хендлер для кнопки
	public class ALSave implements ActionListener 
	{
		String s_str = null;
		JTextPane sim_pane = null;
		
		ALSave( String s_str, JTextPane sim_pane ) 
		{ 
			this.s_str = s_str; 
			this.sim_pane = sim_pane;
		}
		
		public void actionPerformed( ActionEvent ae )
		{
			String s_similars_text = this.sim_pane.getText();
			if( s_similars_text.length() <= 0 ) { return; }
			
			String[] s_similars_arr =  s_similars_text.split( "\n" );
			ArrayList< String > s_similars_al = new ArrayList< String >();
			for( int i = 0; i < s_similars_arr.length; i++ )
			{
				s_similars_al.add( s_similars_arr[i] );
			}
			
			MySimKanji msk = new MySimKanji();
			msk.changeOrAddSimilar( this.s_str, s_similars_al );
			msk.saveToFile();
			this_link.setVisible( false );
		}
	}
}
