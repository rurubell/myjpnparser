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
import src.util.MyExec;
import src.params.MyParams;
import src.util.string.MyStringUtils;
import src.util.log.MyLogToStdout;


public class MyTranslateShellPanel extends MyPanel
{
	//Панель которую вернем
	private JPanel res_panel = null;
	//Фраза, для которой нужно получить перевод
	private String s_sent = null;
	//Список субокон главного окна
	ArrayList< JFrame > sub_frames_al = null;
	
	
	public MyTranslateShellPanel( String s_sent, ArrayList< JFrame > sub_frames_al ) 
	{ 
		this.s_sent = s_sent;
		this.sub_frames_al = sub_frames_al;
	}
	
	
	public JPanel getPanel() { return this.res_panel; }
	
	
	@Override
	public void run()
	{
		//MyLogToStdout mlts = new MyLogToStdout();
		
		res_panel = new JPanel();
		Border b_titled = BorderFactory.createTitledBorder( "Translate shell say:" );
		res_panel.setBorder( b_titled );
		res_panel.setLayout( new BorderLayout() );
		
		//String s_translation = this.translateJapToEng( this.s_sent );
		JTextPane text_pane = new JTextPane();
		text_pane.setFont( new Font( "Noto Sans", Font.BOLD, 12 ) );
		text_pane.setBackground( Color.decode( "#EEEFFF" ) );
		text_pane.setText( "...\n\n\n\n\n\n\n\n" );
		
		Border b_comp_bord = new CompoundBorder
		(
			BorderFactory.createLineBorder( Color.decode( "#444444" ) ), 
			BorderFactory.createMatteBorder( 5, 5, 5, 5, Color.decode( "#EEEFFF" ) )
		);
		text_pane.setBorder( b_comp_bord );
		
		MyTranslateShellPanelJTextPaneMenu mtspjtpm = new MyTranslateShellPanelJTextPaneMenu( text_pane, this.sub_frames_al );
		text_pane.setComponentPopupMenu( mtspjtpm );
		
		JScrollPane scroll_pane = new JScrollPane( text_pane );
		scroll_pane.setVerticalScrollBarPolicy( javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		res_panel.add( scroll_pane, BorderLayout.CENTER );
		
		MyTextPaneThread mtpt = new MyTextPaneThread( text_pane, this.s_sent );
		mtpt.start();
	}
	
	
	class MyTextPaneThread extends Thread
	{
		JTextPane text_pane = null;
		String s_text = null;
		
		MyTextPaneThread( JTextPane text_pane, String s_text ) 
		{ 
			this.text_pane = text_pane;
			this.s_text = s_text;
		}
		
		@Override
		public void run()
		{
			String s_translation = translateJapToEng( this.s_text );
			this.text_pane.setText( s_translation );
			this.text_pane.repaint();
			this.text_pane.revalidate();
		}
		
		
		//перевести с японского на английский
		private String translateJapToEng( String s_text )
		{
			String s_res = "";
			MyExec me = new MyExec();
			MyStringUtils msu = new MyStringUtils();
			
			String s_command = MyParams.getStringValue( "trans_path" ) + " -t en -s jpn \"" + s_text + "\"";
			ArrayList< String > s_transl_list = me.executeCommandAndGetList( s_command );
			ArrayList< String > s_clear_list = this.deleteUnnecessaryStrings( s_transl_list );
			
			for( String s_pos : s_clear_list ) { s_res += s_pos + "\n"; }
			s_res = s_res.replace( "\n\n\n", "\n\n" ); //Удаляем двойные пустые строки
			s_res = s_res.replace( "\", \"", "\",\n    \"" );
			
			return s_res;
		}
		
		
		//Удалить лишние строки из перевода
		private ArrayList< String > deleteUnnecessaryStrings( ArrayList< String > s_strings )
		{
			ArrayList< String > s_res_list = new ArrayList< String >();
			MyStringUtils msu = new MyStringUtils();
			
			for( String s_pos : s_strings )
			{
				if( s_pos.indexOf( "->" ) != -1 ) { continue; }
				if( s_pos.indexOf( "Translations of" ) != -1 ) { continue; }
				
				s_res_list.add( msu.deleteBashEsc( s_pos ) );
			}
			
			return s_res_list;
		}
	}
}
