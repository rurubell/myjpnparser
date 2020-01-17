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


public class MyAddAddAutoReplacementWin extends JFrame
{
	JFrame this_link = this;
	//Вертикальный оффсет на котором помещаем окно, относительно курсора
	private int H_OFFSET = 100;
	//Ширина одной буквы, для растягивания по длинне текста
	private int N_CHAR_WIDTH = 20;
	
	MyAutoReplacement mar;
	
	public MyAddAddAutoReplacementWin( String s_str, MyAutoReplacement mar )
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
		JLabel sims_label = new JLabel( "Add Replacement:" );
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
		JTextField sim_field = new JTextField();
		sim_field.setFont( new Font( "Sans", Font.BOLD, 25 ) );
		main_panel.add( sim_field, c );
		
		//Кнопка
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1.0;
		c.weighty = 1.0;
		JButton save_button = new JButton( "Save" );
		save_button.addActionListener( new ALSave( s_str, sim_field ) );
		main_panel.add( save_button, c );
		
		this.add( main_panel );
		this.pack();
		
		this.setMinimumSize( new Dimension( 370, 0 ) );
		
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
		JTextField sim_field = null;
		
		ALSave( String s_str, JTextField sim_field ) 
		{ 
			this.s_str = s_str; 
			this.sim_field = sim_field;
		}
		
		public void actionPerformed( ActionEvent ae )
		{
			String s_similar = this.sim_field.getText();
			if( s_similar.length() <= 0 ) { return; }
			
			mar.changeOrAddSimilar( this.s_str, s_similar );
			mar.saveToFile();
			this_link.setVisible( false );
		}
	}
}
