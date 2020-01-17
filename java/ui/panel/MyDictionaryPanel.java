package src.ui.panel;


import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.border.*;
import java.awt.event.*;

import src.ui.panel.*;
import src.data.MyJapanWord;

import src.data.MyTranslation;
import src.jap.dict.MyDict;
import src.jap.dict.jmdict.MyJMDICT;


public class MyDictionaryPanel extends MyPanel
{
	private String s_dict_name = "Edict2";
	//Панель которую вернем
	private JPanel res_panel = null;
	//Список слов, для которых нужно получить перевод
	ArrayList< MyJapanWord > mjw_list = null;
	
	//Цвета панелей
	Color[] col_arr = 
	{
		Color.decode( "#FFFFFF" ),
		Color.decode( "#E1F6FF" )
	};
	
	//Список субокон главного окна
	ArrayList< JFrame > sub_frames_al = null;
	
	
	public MyDictionaryPanel( ArrayList< MyJapanWord > mjw_list, ArrayList< JFrame > sub_frames_al ) 
	{ 
		this.mjw_list = mjw_list;
		this.sub_frames_al = sub_frames_al;
	}
	
	public JPanel getPanel() { return this.res_panel; }
	
	
	@Override
	public void run()
	{
		this.res_panel = new JPanel();
		Border b_titled = BorderFactory.createTitledBorder( this.s_dict_name + " say:" );
		this.res_panel.setBorder( b_titled );
		this.res_panel.setLayout( new BorderLayout() );
		
		JPanel panel = this.getPanelOfPanels( this.mjw_list );
		JScrollPane jsp = new JScrollPane( panel );
		
		this.res_panel.add( jsp, BorderLayout.CENTER );
	}
	
	
	//Заполнить панель, панелями - словами
	public JPanel getPanelOfPanels( ArrayList< MyJapanWord > mjw_list )
	{
		JPanel res_panel = new JPanel();
		res_panel.setLayout( new BoxLayout( res_panel, BoxLayout.PAGE_AXIS ) );
		
		try
		{
			ArrayList< MyDictionaryPosPanel > mjpp_list = new ArrayList< MyDictionaryPosPanel >();
			
			int i = 0;
			for( MyJapanWord mjw_pos : mjw_list )
			{
				if( this.isCorrectWord( mjw_pos ) )
				{
					MyDictionaryPosPanel mjpp = new MyDictionaryPosPanel( mjw_pos, this.col_arr[i], this.sub_frames_al );
					mjpp_list.add( mjpp );
					mjpp.start();
					
					i++;
					if( i >= 2 ) { i = 0; }
				}
			}
			
			for( MyDictionaryPosPanel mjpp_pos : mjpp_list ) { mjpp_pos.join(); }
			
			for( MyDictionaryPosPanel mjpp_pos : mjpp_list ) 
			{
				JPanel panel_pos = mjpp_pos.getPanel();
				res_panel.add( panel_pos );
			}
		} 
		catch( Exception e ) { e.printStackTrace(); }
		
		return res_panel;
	}
	
	
	//Это корректное слово, для перевода?
	private boolean isCorrectWord( MyJapanWord mjw )
	{
		//Если все слово - один знак каны, не переводим
		if( mjw.isOnlyHiragana() && ( mjw.getWord().length() <= 1 ) ) { return false; }
		if( mjw.getBaseForm().indexOf( "*" ) != -1 ) { return false; }
		if
		( 
			( mjw.getType().indexOf( "none" ) != -1 ) || //А шо бы нет?
			( mjw.getType().indexOf( "noun" ) != -1 ) ||
			( mjw.getType().indexOf( "verb" ) != -1 ) ||
			( mjw.getType().indexOf( "adnominal" ) != -1 ) ||
			( mjw.getType().indexOf( "particle" ) != -1 ) ||
			( mjw.getType().indexOf( "adjective" ) != -1 )
		)
		{
			return true;
		}
		
		return false;
	}
}
