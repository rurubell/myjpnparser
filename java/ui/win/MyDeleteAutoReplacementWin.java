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
import src.jap.autorepl.*;


public class MyDeleteAutoReplacementWin extends JFrame
{
	JFrame this_link = this;
	//Вертикальный оффсет на котором помещаем окно, относительно курсора
	private int H_OFFSET = 100;
	//Ширина одной буквы, для растягивания по длинне текста
	private int N_CHAR_WIDTH = 20;
	
	MyAutoReplacement mar; 
	
	
	public MyDeleteAutoReplacementWin( String s_key, String s_value, MyAutoReplacement mar )
	{
		this.mar = mar;
		
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
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 20.0;
		JLabel main_label = new JLabel( "<html><span style='font-size:20px'>Delete \"" + s_key + "\" -> \"" + s_value + "\"?</span></html>" );
		JPanel label_panel = new JPanel( new BorderLayout() );
		main_label.setHorizontalAlignment(JLabel.CENTER);
		main_label.setVerticalAlignment(JLabel.CENTER);
		label_panel.add( main_label, BorderLayout.CENTER );
		main_panel.add( label_panel, c );
		
		//Кнопка YES
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		JButton yes_button = new JButton( "Yes" );
		yes_button.addActionListener( new ALYes( s_key ) );
		main_panel.add( yes_button, c );
		
		//Кнопка NO
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		JButton no_button = new JButton( "No" );
		no_button.addActionListener( new ALNo() );
		main_panel.add( no_button, c );
		
		this.add( main_panel );
		this.pack();
		
		this.setMinimumSize( new Dimension( 370, 0 ) );
		
		//Получаем текущие координаты курсора
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point p = pi.getLocation();
		int x = (int) p.getX();
		int y = (int) p.getY();
		this.setLocation( x, y - H_OFFSET );
		
		no_button.requestFocus();
	}
	
	
	//Хендлер для No
	public class ALNo implements ActionListener 
	{
		public void actionPerformed( ActionEvent ae )
		{
			this_link.dispose();
		}
	}
	
	
	//Хендлер для Yes
	public class ALYes implements ActionListener 
	{
		private String s_key = null;
		
		public ALYes( String s_key ) { this.s_key = s_key; }
		
		public void actionPerformed( ActionEvent ae )
		{
			mar.deleteAutoReplacement( this.s_key );
			
			this_link.dispose();
		}
	}
}
