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
import src.jap.simkanji.MySimKanji;


public class MySimKanjiWin extends JFrame
{
	//Вертикальный оффсет на котором помещаем окно, относительно курсора
	private int H_OFFSET = 100;
	//Ссылка на себя
	private JFrame jf_self = this;
	
	
	JTextPane mrse_link = null;
	JFrame this_link = this;
	
	
	public MySimKanjiWin( ArrayList< String > s_sim_kanji, JTextPane mrse_link )
	{
		//Получаем текущие координаты курсора
		this.mrse_link = mrse_link;
		PointerInfo pi = MouseInfo.getPointerInfo();
		Point p = pi.getLocation();
		int x = (int) p.getX();
		int y = (int) p.getY();
		
		//this.addWindowFocusListener( new MyWinListener() );
		
		this.setLocation( x, y - H_OFFSET );
		this.add( this.makeSimKanjiPanel( s_sim_kanji ) );
		this.pack();
	}
	
	
	//Создать панель кнопок с походими кандзи
	private JPanel makeSimKanjiPanel( ArrayList< String > s_sim_kanji )
	{
		JPanel res_panel = new JPanel();
		
		for( int i = 0; i < s_sim_kanji.size(); i++ )
		{
			MySimKanjiButton mskb = new MySimKanjiButton( s_sim_kanji.get(i) );
			mskb.addActionListener( new ALReplaceKanji() );
			res_panel.add( mskb );
		}
		
		return res_panel;
	}
	
	
	//Хендлер для кнопки
	public class ALReplaceKanji implements ActionListener 
	{
		public void actionPerformed( ActionEvent ae )
		{
			MySimKanjiButton mskb_source = ( MySimKanjiButton ) ae.getSource();
			
			//int n_start = mrse_link.getSelectionStart();
			//int n_end = mrse_link.getSelectionEnd();
			
			mrse_link.replaceSelection( mskb_source.getKanji() );
			
			//mrse_link.setSelectionStart( n_start );
			//mrse_link.setSelectionEnd( n_end );
			//this_link.setVisible( false );
			jf_self.dispose();
		}
	}
	
	
	//Хендлер для потери фокуса
	public class MyWinListener implements WindowFocusListener
	{
		public void windowGainedFocus( WindowEvent e ) {}
		public void windowLostFocus( WindowEvent e ) 
		{
			//jf_self.setVisible( false );
			jf_self.dispose();
		}
	}
}
